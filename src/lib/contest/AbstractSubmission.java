package lib.contest;

import lib.utils.Utils;
import lib.utils.tuples.Pair;
import lib.utils.tuples.Triple;
import lib.utils.various.VoidPrintStream;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.security.*;
import java.util.*;

public abstract class AbstractSubmission {
    public Scanner sc;
    public PrintStream out;
    public PrintStream debug;
    public volatile double score = 0;
    public volatile int testCaseCount = -1;
    public volatile int testCaseIndex;
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
        runSubmission(in, out,  debug ? out : new VoidPrintStream());
    }

    public void runSubmission(InputStream in, PrintStream out, PrintStream debug) {
        this.sc = new Scanner(in);
        this.out = out;
        this.debug = debug;

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
            out.printf(type.caseString, testCaseIndex, testCaseCount);
            testCase();
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

    /* BEGIN-NO-BUNDLE */
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

                Object val = cache.get(name);
                if (val == null || p2w(field.getType()).isAssignableFrom(p2w(val.getClass()))) {
                    field.set(submission, val);
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


    protected static void buildAndRun(Class<? extends AbstractSubmission> clss, String path, String identifier) throws Exception {
        ContestType contestType = getType(clss);

        if (!contestType.isOptimizer) FileTest.testFrom(path, clss, b64Hash(clss.getName() + " " + identifier.hashCode()));
        BuildOutput.buildFromProblem(contestType, path, clss.getSimpleName(), identifier);

        if (contestType.isOptimizer) {
            System.out.println("Created Main.java. Compile and run it to start the optimizer");
        } else {
            System.out.println("\n\n\n\n\n\n\nWaiting on stdin for a testcase");
            AbstractSubmission.create(clss).runSubmission(true);
        }
    }
    /* END-NO-BUNDLE */

    private static String b64Hash(String s) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = Arrays.copyOf(digest.digest(s.getBytes(StandardCharsets.UTF_8)), 3);
        return Base64.getEncoder().encodeToString(encodedhash);
    }



    public abstract void testCase();
    public abstract void init();
}
