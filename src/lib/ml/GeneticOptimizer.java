package lib.ml;

import lib.utils.tuples.Pair;
import lib.utils.various.VoidPrintStream;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class GeneticOptimizer<T, U> {

    private final Class<T> clss;
    private final Supplier<T> constructor;
    private final Function<T, Pair<Double, U>> runFunc;
    private List<Subject> lastGeneration;
    private SortedSet<Subject> allSubjects = new TreeSet<>(Comparator.comparingDouble(a -> -a.score));
    private Pair<Double, U> best = null;
    private Random random = new Random();
    private int totalGenerations = 0;

    public GeneticOptimizer(Class<T> clss, Supplier<T> constructor, Function<T, Pair<Double, U>> runFunc) {
        this.clss = clss;
        this.constructor = constructor;
        this.runFunc = runFunc;
    }


    public static <T, U> Pair<Double, U> runLoudly(Class<T> clss, Supplier<T> constructor, Function<T, Pair<Double, U>> runFunc) {
        if (GeneticOptimizer.canOptimize(clss)) {
            Scanner sc = new Scanner(System.in);
            GeneticOptimizer<T, U> optimizer = new GeneticOptimizer<>(clss, constructor, runFunc);
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
        return best;
    }



    public static boolean canOptimize(Class<?> clss) {
        return getOptimizableProperties(clss).size() >= 1;
    }

    public static List<Field> getOptimizableProperties(Class<?> clss) {
        return Arrays.stream(clss.getDeclaredFields())
                .filter(f -> { f.setAccessible(true); return f.getAnnotation(Optimize.class) != null; })
                .collect(Collectors.toList());
    }





    public void run(int iterations) {
        run(iterations, new VoidPrintStream());
    }


    public void run(int iterations, PrintStream log) {

        final int generationSize = Math.max(9, Runtime.getRuntime().availableProcessors() + 2);
        final int randomsPerGen = generationSize / 3;
        final int breededPerGen = (generationSize - randomsPerGen) / 2;
        final int mutatedPerGen = generationSize - randomsPerGen - breededPerGen;



        for (int it = 1; it <= iterations; it++) {
            totalGenerations++;
            log.print("Generation " + totalGenerations + "/" + (totalGenerations - it + iterations) + ": ");

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
            newGeneration.parallelStream().forEach(Subject::evaluate);

            // Update best score
            newGeneration.stream().forEach(a -> {
                if (best == null || a.score > best.a) best = new Pair<>(a.score, a.meta);
            });

            // Remove metadata to be more memory-efficient
            newGeneration.stream().forEach(Subject::nukeMeta);

            // Print the scores
            log.println(newGeneration);

            // Sort the new generation
            newGeneration.sort(Comparator.comparingDouble(a -> -a.score));
            lastGeneration = newGeneration;

            for (Subject sub : newGeneration) {
                allSubjects.add(sub);
                while (allSubjects.size() >= 20) {
                    allSubjects.remove(allSubjects.last());
                }
            }
        }
    }




    private class Subject {
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
