/* BEGIN-NO-BUNDLE */
package lib.contest;

import lib.utils.tuples.Pair;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileTest {
    public static void testFrom(String directory, Class<? extends AbstractSubmission> clss, String taskName) throws IOException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 2);
        List<Pair<String, TestTask>> tasks = new ArrayList<>();


        Path testingToolPath = Paths.get(directory, "testing_tool.sh");
        if (Files.exists(testingToolPath)) {
            System.out.println();
            System.out.println("Found testing tool at " + testingToolPath);
            System.out.println("Note that the testing tool does not support @Cached caching");
            Process tester = new ProcessBuilder("./" + testingToolPath.getFileName().toString())
                    .directory(testingToolPath.getParent().toFile())
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .start();
            InputStream in = new InputStream() {
                @Override
                public int read() throws IOException {
                    int b = tester.getInputStream().read();
                    if (b >= 0) System.out.write(b);
                    return b;
                }

                @Override
                public int read(byte b[], int off, int len) throws IOException {
                    int res = tester.getInputStream().read(b, off, len);
                    if (res >= 0) System.out.write(b, off, res);
                    return res;
                }
            };
            OutputStream out = new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    System.out.write(b);
                    tester.getOutputStream().write(b);
                }

                @Override
                public void write(byte b[], int off, int len) throws IOException {
                    System.out.write(b, off, len);
                    tester.getOutputStream().write(b, off, len);
                    tester.getOutputStream().flush();
                }

                @Override
                public void flush() throws IOException {
                    tester.getOutputStream().flush();
                    System.out.flush();
                }
            };
            AbstractSubmission.create(clss).runSubmission(in, out, System.err);

            tester.destroy();

            System.out.println();
        } else {
            System.out.println();
            System.out.println("No testing tool found at " + testingToolPath);
            System.out.println();
        }



        try {
            Path dirpath = Paths.get(directory);
            try (DirectoryStream<Path> subdirs = Files.newDirectoryStream(dirpath)) {
                for (Path path : subdirs) {
                    String fileName = path.getFileName().toString();
                    if (!Files.isDirectory(path) && (fileName.endsWith(".txt") || fileName.endsWith(".in"))) {
                        TestTask task = new TestTask(path, clss, taskName);
                        service.submit(task);
                        tasks.add(new Pair<>(fileName, task));
                    }
                }
            }
        } finally {
            service.shutdown();
            boolean br = false;
            while (!br) {
                br = service.awaitTermination(500, TimeUnit.MILLISECONDS);
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.print("\b\r");
                }
                for (Pair<String, TestTask> task : tasks) {
                    System.out.println(task.getKey() + ": " + task.getValue().getMessage());
                }
            }
            System.out.println();
        }
    }


    private static class TestTask implements Runnable {

        private final String taskName;
        private final Path path;
        private final  Class<? extends AbstractSubmission> clss;
        private volatile boolean started = false;
        private volatile Throwable failed = null;
        private volatile double time = -1;
        private volatile boolean isDiff = false;
        private volatile boolean isDone = false;
        private volatile AbstractSubmission submission = null;

        public TestTask(Path path, Class<? extends AbstractSubmission> clss, String taskName) {
            this.path = path;
            this.clss = clss;
            this.taskName = taskName;
        }

        public String getMessage() throws InterruptedException {
            if (failed != null) return ("Failed! " + failed.toString() + "; at " + Arrays.toString(failed.getStackTrace())).replaceAll("\\n", "; ");
            if (!started) return "Queued";
            if (isDiff) return "Wrong Answer! (" + new DecimalFormat("0.###").format(time) + "s)";
            if (isDone) return "Done! (" + new DecimalFormat("0.###").format(time) + "s)";
            if (submission == null || submission.testCaseCount < 0) return "Preparing...";
            if (submission.testCaseIndex <= 0) return "Initializing...";


            int tcc = submission.testCaseCount;
            int tci = submission.testCaseIndex;
            double prog = submission.progress;
            int backoff = 1000;
            while (submission.testCaseIndex != tci) {
                int nanos = ThreadLocalRandom.current().nextInt(backoff);
                Thread.sleep(nanos / 1_000_000, nanos % 1_000_000);
                if (backoff < 10_000_000) backoff *= 2;

                tci = submission.testCaseIndex;
                prog = submission.progress;
            }

            if (tci > tcc) return "Finishing...";

            String s = (tci - 1) + "/" + tcc;
            if (prog > 0) {
                s += ", " + new DecimalFormat("0.###").format(prog * 100) + "%";
            }


            return s;
        }


        @Override
        public void run() {
            try {
                started = true;
                String inName = path.getFileName().toString();
                String name = inName.replaceFirst("[.][^.]+$", "");
                String nameOut = name + ".out";
                String nameCache = name + "." + taskName + ".cache.zip";
                Path outPath = path.getParent().resolve(nameOut);
                Path cachePath = path.getParent().resolve(nameCache);

                try (
                        InputStream in = Files.newInputStream(path);
                        OutputStream out = Files.newOutputStream(outPath);
                ) {
                    SubmissionCache cache = readCache(clss, cachePath);
                    submission = AbstractSubmission.create(clss, cache);

                    PrintStream prout = new PrintStream(out);
                    long start = System.nanoTime();
                    try {
                        submission.runSubmission(in, prout, false);
                    } catch (Throwable e) {
                        e.printStackTrace(prout);
                        throw e;
                    } finally {
                        long end = System.nanoTime();
                        time = (end - start) / 1_000_000_000.0;
                        isDone = true;
                    }

                    writeCache(submission, cachePath);
                }
            } catch (Throwable e) {
                failed = e;
            }
        }


        private static SubmissionCache readCache(Class<? extends AbstractSubmission> clss, Path path) throws IOException {
            if (!Files.exists(path)) return null;

            try (
                    ZipInputStream zip = new ZipInputStream(Files.newInputStream(path))
            ) {
                zip.getNextEntry();
                try (ObjectInputStream in = new ObjectInputStream(zip)) {
                    Object s = in.readObject();
                    if (!AbstractSubmission.getCacheVersion(clss).equals(s)) return null;
                    Object obj = in.readObject();
                    if (obj instanceof Map) {
                        return (SubmissionCache) obj;
                    } else {
                        return null;
                    }
                }
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        private static void writeCache(AbstractSubmission submission, Path path) throws IOException, IllegalAccessException {
            SubmissionCache cache = submission.getCache();
            if (cache == null || cache.isEmpty()) {
                if (Files.exists(path)) Files.delete(path);
                return;
            }

            try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(path))) {
                zip.putNextEntry(new ZipEntry("data.cache"));
                try (ObjectOutputStream out = new ObjectOutputStream(zip)) {
                    out.writeObject(AbstractSubmission.getCacheVersion(submission.getClass()));
                    out.writeObject(cache);
                    zip.closeEntry();
                }
            }
        }
    }
}

/* END-NO-BUNDLE */