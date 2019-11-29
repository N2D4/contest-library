package lib.contest;

import lib.ml.optimize.GeneticOptimizer;
import lib.utils.Utils;
import lib.utils.tuples.Pair;
import lib.utils.tuples.Triple;
import lib.utils.various.FastScanner;
import lib.utils.various.VoidOutputStream;
import lib.utils.various.VoidPrintStream;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public abstract class AbstractSubmission {
    /**
     * A FastScanner instance, similar to normal scanners but faster.
     */
    public FastScanner sc;
    /**
     * The input stream, that the scanner uses. Generally, the scanner should be preferred
     */
    public InputStream in;
    public PrintStream out;
    /**
     * The debug print stream. May have different behaviour from normal print streams and out streams; in particular,
     * it may decide not to call .toString on objects (eg. if there is no stderr output), or print arrays using
     * Arrays.deepToString(...). This behaviour is not guaranteed.
     */
    public PrintStream debug;
    public volatile double score = 0;
    public volatile int testCaseCount = -1;
    public volatile int testCaseIndex;
    /**
     * A double from 0 to 1 indicating the current progress of this test case (0 = 0%, 1 = 100%).
     */
    public volatile double progress = 0;


    public void runSubmission() {
        runSubmission(false);
    }

    public void runSubmission(boolean debug) {
        runSubmission(System.in, System.out, debug);
    }

    public void runSubmission(InputStream in, PrintStream out) {
        runSubmission(in, out,  false);
    }

    public void runSubmission(InputStream in, PrintStream out, boolean debug) {
        runSubmission(in, out,  debug ? System.err : new VoidPrintStream());
    }

    public void runSubmission(InputStream in, OutputStream out, OutputStream debug) {
        this.in = in;
        this.sc = new FastScanner(this.in);
        this.out = new PrintStream(out);
        // If we have a void output stream, shortcut a void print stream - this is faster as it doesn't convert objects
        // passed to it to strings first
        this.debug = debug instanceof VoidOutputStream || debug instanceof VoidPrintStream ? new VoidPrintStream() : new DebugPrintStream(debug);

        ContestType type = getType();

        testCaseCount = type.testCaseCount;

        init();

        if (testCaseCount <= 0) {
            // Read the number of test cases to follow
            testCaseCount = sc.nextInt();
            sc.nextLine();
        }

        testCaseIndex = 0;

        // Iterate over the testcases and solve the problem
        for (testCaseIndex = 1; testCaseIndex <= testCaseCount; testCaseIndex++) {
            progress = 0;
            this.out.printf(type.caseString, testCaseIndex, testCaseCount);
            testCase();
        }
    }

    private class DebugPrintStream extends PrintStream {
        public DebugPrintStream(OutputStream debug) {
            super(debug);
        }

        private Object conv(Object obj) {
            if (obj instanceof byte[]) return Arrays.toString((byte[]) obj);
            else if (obj instanceof char[]) return Arrays.toString((char[]) obj);
            else if (obj instanceof short[]) return Arrays.toString((short[]) obj);
            else if (obj instanceof int[]) return Arrays.toString((int[]) obj);
            else if (obj instanceof long[]) return Arrays.toString((long[]) obj);
            else if (obj instanceof float[]) return Arrays.toString((float[]) obj);
            else if (obj instanceof double[]) return Arrays.toString((double[]) obj);
            else if (obj instanceof boolean[]) return Arrays.toString((int[]) obj);
            else if (obj instanceof Object[]) return Arrays.deepToString((Object[]) obj);
            else return obj;
        }

        @Override
        public void print(Object obj) {
            super.print(conv(obj));
        }

        @Override
        public void println(Object obj) {
            super.println(conv(obj));
        }
    }

    public ContestType getType() {
        return getType(getClass());
    }

    public static ContestType getType(Class<? extends AbstractSubmission> clss) {
        return getAnnotation(clss).value();
    }

    private static ContestSubmission getAnnotation(Class<? extends AbstractSubmission> clss) {
        if (!clss.isAnnotationPresent(ContestSubmission.class)) {
            throw new RuntimeException("ContestSubmission annotation not present on class " + clss.getCanonicalName() + "!");
        }

        return clss.getAnnotation(ContestSubmission.class);
    }

    /* BEGIN-OPTIMIZER */ // we only want to include this with optimizers as only optimizers use these methods during runtime
    public static <T extends AbstractSubmission> T create(Class<T> clss) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return create(clss, null);
    }

    public static <T extends AbstractSubmission> T create(Class<T> clss, SubmissionCache cache) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (cache == null) cache = new SubmissionCache();

        Constructor<T> constructor = clss.getConstructor();
        constructor.setAccessible(true);
        T submission = constructor.newInstance();


        Class<?> c = submission.getClass();
        do {
            for (Field field : c.getDeclaredFields()) {
                Triple<Class<?>, String, Integer> name = getFieldTriple(field);
                if (name == null) continue;
                if (Modifier.isStatic(field.getModifiers())) throw new RuntimeException("Static fields can't be cached!");

                field.setAccessible(true);

                if (cache.containsKey(name)) {
                    Object val = cache.get(name);
                    if (val == null || p2w(field.getType()).isAssignableFrom(p2w(val.getClass()))) {
                        field.set(submission, val);
                    }
                }
            }
        } while ((c = c.getSuperclass()) != null);


        return submission;
    }

    private static Class<?> p2w(Class<?> clss) {
        return Utils.primitiveToWrapper(clss);
    }

    public SubmissionCache getCache() throws IllegalAccessException {
        SubmissionCache result = new SubmissionCache();

        Class<?> c = this.getClass();
        do {
            for (Field field : c.getDeclaredFields()) {
                Triple<Class<?>, String, Integer> name = getFieldTriple(field);
                if (name == null) continue;
                if (Modifier.isStatic(field.getModifiers())) throw new RuntimeException("Static fields can't be cached!");

                field.setAccessible(true);
                result.put(name, field.get(this));
            }
        } while ((c = c.getSuperclass()) != null);

        return result;
    }

    public static Pair<Class<?>, String> getCacheVersion(Class<? extends AbstractSubmission> clss) {
        int i = 0;
        Class<?> c = clss;
        do {
            i++;
        } while ((c = c.getSuperclass()) != null);

        StringBuilder res = new StringBuilder();
        c = clss;
        do {
            res.append("v");
            CacheVersion version = c.getAnnotation(CacheVersion.class);
            if (version != null) {
                res.append(version.value());
            }
        } while ((c = c.getSuperclass()) != null);

        return new Pair<>(clss, res.toString());
    }

    private static Triple<Class<?>, String, Integer> getFieldTriple(Field field) {
        Cached annot = field.getAnnotation(Cached.class);
        if (annot == null) return null;
        return new Triple<>(field.getDeclaringClass(), field.getName(), annot.value());
    }
    /* END-OPTIMIZER */

    /* BEGIN-NO-BUNDLE */
    protected static void buildAndRun(Class<? extends AbstractSubmission> clss, String path, String identifier) throws Exception {
        buildAndRun(clss, path, identifier, false);
    }

    protected static void buildAndRun(Class<? extends AbstractSubmission> clss, String path, String identifier, boolean skipRun) throws Exception {
        if (System.getProperty("org.openjdk.java.util.stream.tripwire") == null) System.setProperty("org.openjdk.java.util.stream.tripwire", "true");

        ContestType contestType = getType(clss);

        if (!contestType.isOptimizer) FileTest.testFrom(path, clss, b64Hash(clss.getName() + " " + identifier));
        BuildOutput.buildFromProblem(contestType, path, clss.getSimpleName(), identifier);

        if (skipRun) {
            System.out.println("Run skipped! The code has been saved to disk");
        } else {
            if (contestType.isOptimizer) {
                optimizeFromPath(clss, "/Users/konstantinwohlwend/Downloads", true);
            } else {
                System.out.println("\n\n\n\n\n\n\nWaiting on stdin for a testcase");
                AbstractSubmission.create(clss).runSubmission(true);
            }
        }
    }

    private static String b64Hash(String s) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = Arrays.copyOf(digest.digest(s.getBytes(StandardCharsets.UTF_8)), 3);
        return Base64.getEncoder().encodeToString(encodedhash);
    }
    /* END-NO-BUNDLE */


    /* BEGIN-OPTIMIZER */
    public static <T extends AbstractSubmission> void optimizeFromPath(Class<T> clss, String inDir, boolean debug) throws IOException {
        try (Stream<Path> files = Files.list(Paths.get(inDir))) {
            files.filter(inPath -> inPath.toString().endsWith(".in.txt") || inPath.toString().endsWith(".in")).forEachOrdered(inPath -> {
                try {
                    System.out.println();
                    System.out.println();
                    System.out.println("===== Processing input file: " + inPath.getFileName() + " ======");
                    String inStr = new String(Files.readAllBytes(inPath));

                    Path cacheDir = Paths.get(inPath + ".cache");
                    AtomicReference<SubmissionCache> cache = new AtomicReference<>(SubmissionCache.readCache(clss, cacheDir));
                    Pair<Double, ByteArrayOutputStream> res = GeneticOptimizer.runLoudly(clss, Utils.nonThrowing(() -> AbstractSubmission.create(clss, cache.get())), Utils.nonThrowing(a -> {
                        InputStream in = new ByteArrayInputStream(inStr.getBytes(StandardCharsets.UTF_8));
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        a.runSubmission(in, new PrintStream(out), debug);
                        cache.set(a.getCache());
                        return new Pair<>(a.score, out);
                    }));
                    SubmissionCache.writeCache(clss, cache.get(), cacheDir);

                    if (res != null) {
                        String scorestr = new DecimalFormat("#.###").format(res.a);
                        System.out.println();
                        System.out.println("Best score for " + inPath.getFileName() + ": " + scorestr);
                        Path outPath = Paths.get(inPath.toString() + " sc" + scorestr + "         " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + ";" + Math.floor(Math.random() * 1000) + ".out.txt");
                        System.out.println("Saving best output in: " + outPath);
                        Files.write(outPath, res.b.toByteArray(), StandardOpenOption.CREATE);
                    }
                } catch (IOException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        System.out.println("Went through all input files!");
    }
    /* END-OPTIMIZER */



    public abstract void testCase();
    public abstract void init();
}
