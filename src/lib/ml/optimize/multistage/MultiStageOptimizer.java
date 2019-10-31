package lib.ml.optimize.multistage;

import lib.graphs.DirectedAdjacencyListGraph;
import lib.graphs.DirectedGraph;
import lib.utils.Utils;
import lib.utils.tuples.Pair;

import java.io.Closeable;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * A multi-stage optimizer. This implementation is not thread-safe.
 */
public class MultiStageOptimizer implements Closeable, AutoCloseable {
    private DirectedGraph graph;
    private Map<String, Integer> stageIds;
    private Stage<?, ?>[] stages;
    private Map<String, PriorityQueue<Pair<Double, Object>>> queues;
    private Semaphore tasks = new Semaphore(0);
    private final Object queueLock = new Object();
    private Thread runningThread = null;
    private volatile boolean shouldStop = false;

    public MultiStageOptimizer(Stage... stages) {
        if (stages.length != Arrays.stream(stages).map(Stage::getName).distinct().count()) {
            throw new IllegalArgumentException("Stage names must be unique! (Rename the classes or override getName())");
        }

        this.graph = new DirectedAdjacencyListGraph(stages.length + 1);
        this.stageIds = new HashMap<>();
        this.stages = Arrays.copyOf(stages, stages.length);
        this.queues = new HashMap<>();
        for (int i = 0; i < stages.length; i++) {
            this.stageIds.put(stages[i].getName(), i);
            this.queues.put(stages[i].getName(), new PriorityQueue<>(11, Comparator.comparingDouble(a -> -a.a)));
        }
    }

    private int getId(Stage stage) {
        return stageIds.get(stage.getName());
    }

    public <I extends Serializable, M extends Serializable, O extends Serializable> void addEdge(Stage<I, ? extends M> fromStage, Stage<M, O> toStage) {
        if (isRunning()) throw new RuntimeException("Can't modify/read from the optimizer while it's running!");

        this.graph.addEdge(getId(fromStage), getId(toStage));
    }

    public boolean isRunning() {
        return runningThread != null;
    }

    /**
     * Alias for queueForStage().
     */
    public <T extends Serializable> void addInput(Stage<T, ?> stage, T input) {
        queueForStage(stage, input);
    }

    /**
     * Queues the given object to be passed on as input to the stage given.
     */
    public <T extends Serializable> void queueForStage(Stage<T, ?> stage, T obj) {
        if (isRunning()) throw new RuntimeException("Can't modify/read from the optimizer while it's running!");

        queueForStageInternal(stage, obj);
    }

    private <T extends Serializable> void queueForStageInternal(Stage<T, ?> stage, T obj) {
        queues.get(stage.getName()).add(new Pair<>(stage.evaluateInput(obj), obj));
    }

    private <T extends Serializable> Optional<T> popForStage(Stage<T, ?> stage) {
        PriorityQueue<Pair<Double, Object>> q = queues.get(stage.getName());
        return q.isEmpty() ? Optional.empty() : Optional.of((T) q.remove().b);
    }




    // The spicy algorithms are in here
    private Pair<Stage, Supplier<Serializable>> popNextTask() throws InterruptedException {
        synchronized (queueLock) {
            while (true) {
                tasks.acquire();
                Stage stage = stages[(int) (Math.random() * (stages.length - 1))];
                Optional<? extends Serializable> op = popForStage(stage);
                if (op.isPresent()) return new Pair<>(stage, () -> stage.compute(op.get()));
            }
        }
    }

    private <O extends Serializable> void queueStageOutput(Stage<?, O> stage, O output) {
        synchronized (queueLock) {
            graph.getNeighbours(getId(stage)).forEachRemaining((int i) -> {
                queueForStage((Stage<O, ?>) stages[i], output);
            });
        }
    }




    public void start() {
        if (isRunning()) throw new RuntimeException("Can't start the optimizer as it's already running!");

        shouldStop = false;
        runningThread = new Thread(Utils.nonThrowing(() -> {
            Semaphore procs = new Semaphore(Runtime.getRuntime().availableProcessors());
            ExecutorService exec = Executors.newCachedThreadPool();
            while (!shouldStop) {
                procs.acquire();
                exec.execute(Utils.nonThrowing(() -> {
                    try {
                        Pair<Stage, Supplier<Serializable>> etsk = popNextTask();
                        Serializable out = etsk.b.get();
                        queueStageOutput(etsk.a, out);
                    } finally {
                        procs.release();
                    }
                }));
            }
            while (exec.awaitTermination(5, TimeUnit.SECONDS));
        }));
    }

    /**
     * Attempts to stop the optimizer, blocking the current thread the optimizer has finished. This might block
     * indefinitely if the optimizer never finishes. Throws a runtime exception if the thread was interrupted
     */
    public void stop() {
        if (!isRunning()) throw new RuntimeException("Can't stop the optimizer if it's not running!");

        shouldStop = true;
        try {
            runningThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        runningThread = null;
    }

    /**
     * Closes this optimizer, stopping it if it was running. Blocks until it has finished. Any operations after
     * calling close cause undefined behaviour; they may or may not throw an exception.
     */
    @Override
    public void close() {
        if (isRunning()) this.stop();
        graph = null;
        stageIds = null;
        stages = null;
        queues = null;
    }
}
