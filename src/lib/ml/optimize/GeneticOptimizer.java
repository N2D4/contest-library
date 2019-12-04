package lib.ml.optimize;

import lib.utils.Arr;
import lib.utils.Utils;
import lib.utils.function.Func;
import lib.utils.function.Supp;
import lib.utils.tuples.Pair;
import lib.utils.tuples.Triple;
import lib.utils.various.VoidPrintStream;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.*;

public class GeneticOptimizer<T, U> {

    private static long subjectUidCounter = 0;
    private final Class<T> clss;
    private final Supp<T> constructor;
    private final Func<T, Pair<Double, U>> runFunc;
    private final int keepBest;
    private final boolean parallel;
    private List<Subject> lastGeneration;
    private final NavigableSet<Subject> allSubjects = new TreeSet<>(Comparator.comparingDouble((Subject a) -> -a.score).thenComparingDouble(a -> a.uid));
    private final NavigableSet<Triple<Double, U, Long>> best = new TreeSet<>(Comparator.comparingDouble((Triple<Double, U, Long> a) -> -a.a).thenComparingDouble(a -> a.c)); // best contains also the metadata, while it gets nuked for the elements of allSubjects
    private Random random = new Random();
    private int totalGenerations = 0;


    public GeneticOptimizer(Class<T> clss, Supp<T> constructor, Func<T, Pair<Double, U>> evaluateFunc) {
        this(clss, constructor, evaluateFunc, 1, false);
    }

    public GeneticOptimizer(Class<T> clss, Supp<T> constructor, Func<T, Pair<Double, U>> evaluateFunc, int keepBest, boolean parallel) {
        this.clss = clss;
        this.constructor = constructor;
        this.runFunc = evaluateFunc;
        this.keepBest = keepBest;
        this.parallel = parallel;
    }


    public static <T, U> Pair<Double, U> runLoudly(Class<T> clss, Supp<T> constructor, Func<T, Pair<Double, U>> runFunc) {
        if (GeneticOptimizer.canOptimize(clss)) {
            Scanner sc = new Scanner(System.in);
            GeneticOptimizer<T, U> optimizer = new GeneticOptimizer<>(clss, constructor, runFunc, 1, true);
            while (true) {
                System.out.println("How many generations? (0 to exit)");
                String in = sc.nextLine();
                int i;
                try {
                    i = Integer.parseInt(in);
                } catch (NumberFormatException e) {
                    System.out.println("Not a valid number, I'm gonna do 5");
                    i = 5;
                }
                if (i <= 0) {
                    System.out.println("Bye!");
                    return optimizer.getBest();
                }
                System.out.println("Doing " + i + " iterations!");
                optimizer.run(i, System.out);
                System.out.println("Current best: " + optimizer.getBest().a);
            }
        } else {
            System.out.println("Optimizer flag enabled but submission can't be optimized. Running it as normal submission instead");
            return runFunc.apply(constructor.get());
        }
    }


    public Pair<Double, U> getBest() {
        Iterator<Pair<Double, U>> iter = getAllBest();
        return !iter.hasNext() ? null : iter.next();
    }

    public Iterator<Pair<Double, U>> getAllBest() {
        return best.stream().map(a -> new Pair<>(a.a, a.b)).iterator();
    }



    public static boolean canOptimize(Class<?> clss) {
        return getOptimizableProperties(clss).size() >= 1;
    }

    public static List<Field> getOptimizableProperties(Class<?> clss) {
        return Arr.stream(clss.getDeclaredFields())
                .filter(f -> { f.setAccessible(true); return f.getAnnotation(Optimize.class) != null; })
                .collect(Collectors.toList());
    }




    public void run(int iterations) {
        run(iterations, new VoidPrintStream());
    }

    public void run(int iterations, PrintStream log) {
        run(iterations, -1, log);
    }

    public void run(int iterations, int generationSize) {
        run(iterations, generationSize, new VoidPrintStream());
    }


    public void run(int iterations, int generationSize, PrintStream log) {
        if (generationSize < 0) {
            generationSize = Runtime.getRuntime().availableProcessors();
            while (generationSize < 3) generationSize *= 2;
        }
        for (int it = 1; it <= iterations; it++) {
            final int randomsPerGen = lastGeneration == null ? generationSize : generationSize / 3;
            final int breededPerGen = lastGeneration == null ? 0 : (generationSize - randomsPerGen) / 2;
            final int mutatedPerGen = lastGeneration == null ? 0 : generationSize - randomsPerGen - breededPerGen;


            totalGenerations++;
            String genstr = "Generation " + totalGenerations + "/" + (totalGenerations - it + iterations) + ": ";
            log.print(genstr);

            List<Subject> newGeneration = new ArrayList<>(generationSize);

            if (lastGeneration != null) {
                // Do the mutations
                for (int i = 0; i < mutatedPerGen; i++) {
                    newGeneration.add(new Subject(chooseGoodSubject()));
                }

                // Do the breeding
                for (int i = 0; i < breededPerGen; i++) {
                    newGeneration.add(new Subject(chooseGoodSubject(), chooseGoodSubject()));
                }
            }

            // Add randomized subjects
            for (int i = 0; i < randomsPerGen; i++) {
                newGeneration.add(new Subject());
            }

            // Evaluate
            double time = Utils.timing(() -> newGeneration.parallelStream().forEach(Subject::evaluate));

            // Update best scores and subject list
            for (Subject sub : newGeneration) {
                best.add(new Triple<>(sub.score, sub.meta, sub.uid));
                while (best.size() > keepBest) {
                    best.remove(best.last());
                }
                allSubjects.add(sub);
                while (allSubjects.size() > 50) {
                    allSubjects.remove(allSubjects.last());
                }
            }

            // Remove metadata to be more memory-efficient
            newGeneration.stream().forEach(Subject::nukeMeta);

            // Print the scores
            log.println(newGeneration + " (" + time + "s)");

            // Sort the new generation
            newGeneration.sort(Comparator.comparingDouble(a -> -a.score));

            // Set the last generation to the new generation
            lastGeneration = newGeneration;
        }
    }




    private class Subject {
        public final long uid = ++subjectUidCounter;

        public double score = Double.NaN;
        public U meta = null;
        public T value;

        // Random
        public Subject() {
            value = constructor.get();
            for (Field field : getOptimizableProperties(clss)) {
                setF(field, value, getRandomFVal(field));
            }
        }

        // Mutate
        public Subject(Subject from) {
            value = constructor.get();
            for (Field field : getOptimizableProperties(clss)) {
                setF(field, value, getMutatedFVal(field, getF(field, from.value)));
            }
        }

        // Breed
        public Subject(Subject one, Subject two) {
            value = constructor.get();
            for (Field field : getOptimizableProperties(clss)) {
                setF(field, value, getBredFVal(field, getF(field, one.value), getF(field, two.value)));
            }
        }

        public void evaluate() {
            Pair<Double, U> pair = runFunc.apply(value);
            this.score = pair.a;
            this.meta = pair.b;
        }

        /**
         * Nukes metadata after it's no longer needed so the garbage collector can collect it.
         */
        public void nukeMeta() {
            this.meta = null;
        }

        public String toString() {
            return "" + (float) score;
        }
    }


    private void setF(Field f, T val, Object obj) {
        try {
            f.set(val, obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getF(Field f, T val) {
        try {
            return f.get(val);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }



    private long nextLong(long b) {
        // TODO replace this shitty code for random longs that I made because I was too lazy to implement it decently
        return (b <= Integer.MAX_VALUE ? random.nextInt((int) b) : ((random.nextLong() % b) + b) % b);
    }


    private Object getRandomFVal(Field field) {
        Class<?> type = field.getType();
        Optimize opt = field.getAnnotation(Optimize.class);
        if (type == Random.class) {
            return new Random(random.nextLong());
        } else if (type == long.class || type == int.class || type == short.class || type == char.class || type == byte.class) {
            long b = opt.imax() - opt.imin();
            long r = nextLong(b) + opt.imin();
            return  type ==  long.class ? (Object) (long)  r :
                    type ==   int.class ? (Object) (int)   r :
                    type == short.class ? (Object) (short) r :
                    type ==  char.class ? (Object) (char)  r :
                                          (Object) (byte)  r;
        } else if (type ==  float.class || type == double.class) {
            double b = opt.fmax() - opt.fmin();
            double r = (random.nextDouble() * b + opt.fmin());
            return type == double.class ? (Object) (double) r : (Object) (float) r;
        } else if (type == boolean.class) {
            return random.nextBoolean();
        }
        return null;
    }

    private Object getMutatedFVal(Field field, Object val) {
        if (random.nextInt(3) == 0) return val;
        if (random.nextInt(2) == 0) return getRandomFVal(field);

        Class<?> type = field.getType();
        Optimize opt = field.getAnnotation(Optimize.class);
        if (type == Random.class) {
            return new Random(random.nextLong());
        } else if (type == long.class || type == int.class || type == short.class || type == char.class || type == byte.class) {
            long b = opt.imax() - opt.imin();
            long p = Math.max(b / 20 * 2 + 1, 3);
            long r = Math.min(opt.imax(), Math.max(opt.imin(), Long.parseLong(""+val) /* <-- lol ugly */ + nextLong(p) - p / 2));
            return  type ==  long.class ? (Object) (long)  r :
                    type ==   int.class ? (Object) (int)   r :
                    type == short.class ? (Object) (short) r :
                    type ==  char.class ? (Object) (char)  r :
                    (Object) (byte)  r;
        } else if (type ==  float.class || type == double.class) {
            double b = opt.fmax() - opt.fmin();
            double r = Double.parseDouble(""+val) /* <-- lol ugly */ + random.nextGaussian() * b * 0.05;
            return type == double.class ? (Object) (double) r : (Object) (float) r;
        } else if (type == boolean.class) {
            return val;
        }
        return null;
    }

    private Object getBredFVal(Field field, Object val1, Object val2) {
        // TODO add actual breeding of individual values (eg. average)
        Object val = random.nextBoolean() ? val1 : val2;
        return random.nextBoolean() ? val : getMutatedFVal(field, val);
    }




    private <K> K chooseRandomizedWeighted(Iterable<K> iterable) {
        K first = null;
        for (K k : iterable) {
            if (first == null) first = k;
            if (random.nextBoolean()) return k;
        }
        return first;
    }

    private Subject chooseGoodSubject() {
        return random.nextBoolean() ? chooseRandomizedWeighted(allSubjects) : chooseRandomizedWeighted(lastGeneration);
    }

}
