/* BEGIN-NO-BUNDLE */

package lib.contest;

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;
import java.util.regex.*;

public class BuildOutput {
    public static int javaVersion;
    public static boolean compress;
    public static boolean removeUnused;
    public static String launcher;
    public static ContestType type;
    public static final String[] defaultImports6 = {"java.io.*", "java.util.*", "java.util.concurrent.*", "java.lang.annotation.*", "java.util.concurrent.*", "java.lang.reflect.*"};
    public static final String[] defaultImports8 = {"java.util.function.*", "java.util.stream.*"};
    public static final String[] ignoredPackages = {"lib.*"};
    public static final String[] entryFiles = {"Solution", "Main"};
    public static final String libDir = System.getProperty("user.home") + "/Library/Mobile Documents/com~apple~CloudDocs/Projects/Contests/Contest Libraries/Contest Library";      // yes, my workspace IS on iCloud Drive
    public static final String launchersDir = libDir + "/launchers";
    public static final String[] globalDirs = new String[] {libDir + "/src"};
    public static final Map<String, String> globalSources = getGlobalSources();



    public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException {
        // TODO Rebuild all
        buildFromProblem(null, null, null, null);
    }



    public static void buildFromProblem(ContestType type, String copyPath, String className, String identifier) throws IOException, InterruptedException, NoSuchAlgorithmException {
        BuildOutput.type = type;
        if (type != null) {
            javaVersion = type.javaVersion;
            compress = !type.disableCompression;
            removeUnused = !type.keepUnusedDependencies;
            launcher = type.launcher;
        }

        try (DirectoryStream<Path> subdirs = Files.newDirectoryStream(Paths.get(""))) {
            buildOutput(subdirs, true, copyPath, className, identifier);
        }
    }


    public static void buildOutput(Iterable<Path> roots, boolean runTests, String copyPath, String className, String identifier) throws IOException, InterruptedException, NoSuchAlgorithmException {
        ArrayList<Path> sroots = new ArrayList<>();
        for (Path root : roots) {
            if (buildOutput(root, copyPath, className, identifier)) {
                sroots.add(root);
            }
        }

        if (runTests) {
            for (Path root : sroots) {
                runTest(root, className);
            }
        }
    }

    public static boolean buildOutput(Path root, String copyPath, String className, String identifier) throws IOException, NoSuchAlgorithmException {
        if (!Files.isDirectory(root)) return false;
        if (!Files.exists(root.resolve("src/" + className + ".java"))) return false;


        long startNs = System.nanoTime();
        System.out.println("Building file for " + className + " in " + root);

        try {
            TreeMap<String, String> sources = new TreeMap<>(Comparator.comparing(a -> a.equals(className) ? "" : a));

            String[] localDirs = new String[] {"src"};
            for (String path : localDirs) {
                sources.putAll(getSources(root.resolve(path)));
            }

            String searchin = sources.firstEntry().getValue();
            if (!searchin.contains(identifier) || !searchin.contains(type.name())) {
                System.out.println("Main file does not contain type " + type.name() + " or identifier " + identifier);
                return false;
            }

            Pattern pattern = Pattern.compile("\\/\\*\\s*JAVA-6-COMPATIBILITY-MODE:\\s*TRUE\\s*\\*\\/");
            if (pattern.matcher(sources.firstEntry().getValue().toUpperCase()).find()) {
                javaVersion = Math.min(6, javaVersion);
                System.out.println("Enabled Java 6 compatibility mode");
            }

            pattern = Pattern.compile("\\/\\*\\s*GLOBAL-DISABLE-CODE-COMPRESSION:\\s*TRUE\\s*\\*\\/");
            if (pattern.matcher(sources.firstEntry().getValue().toUpperCase()).find()) {
                compress = false;
                System.out.println("Disabled code compression globally");
            }

            pattern = Pattern.compile("\\/\\*\\s*KEEP-UNUSED-DEPENDENCIES:\\s*TRUE\\s*\\*\\/");
            if (pattern.matcher(sources.firstEntry().getValue().toUpperCase()).find()) {
                removeUnused = false;
                System.out.println("Disabled removal of unused dependencies");
            }

            sources.putAll(globalSources);

            String result = concatCode(sources, className);

            if (launcher != null) {
                result += "\n\n\n\n" + new String(Files.readAllBytes(Paths.get(launchersDir + "/" + launcher)));
            }


            Path outfol = root.resolve("out");
            if (!Files.exists(outfol)) Files.createDirectory(outfol);

            Path hashp = outfol.resolve(className + ".hash.meta");
            byte[] hash = hash(result);
            if (Files.exists(hashp)) {
                byte[] rhash = Files.readAllBytes(hashp);
                if (Arrays.equals(hash, rhash)) {
                    System.out.println("Hashes match; file not modified");
                    return false;
                }
            }
            Files.write(hashp, hash, StandardOpenOption.CREATE);

            Path out = outfol.resolve(className + ".java");
            if (Files.exists(out)) Files.delete(out);
            Files.write(out, result.getBytes(), StandardOpenOption.CREATE);
            if (copyPath != null) {
                Files.copy(out, Paths.get(copyPath + "/" + root.getFileName() + " " + className + ".java"), StandardCopyOption.REPLACE_EXISTING);
            }

            System.out.println("Created merged file at " + out.toString());
            System.out.println("Total size: " + out.toFile().length() + " bytes");

            return true;

        } finally {
            long endNs = System.nanoTime();

            System.out.println("Time taken: " + (endNs - startNs) / 1_000_000_000.0 + "s");
            System.out.println();
        }
    }

    public static byte[] hash(String s) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(s.getBytes());
    }

    public static Map<String, String> getGlobalSources() {
        try {
            Map<String, String> globalSources = new HashMap<>();
            for (String s : globalDirs) {
                globalSources.putAll(getSources(Paths.get(s)));
            }
            return globalSources;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> getSources(Path path) throws IOException {
        Map<String, String> sources = new HashMap<>();
        for (Map.Entry<String, Path> p : getSourceFiles(path).entrySet()) {
            byte[] bytes = Files.readAllBytes(p.getValue());
            sources.put(p.getKey(), new String(bytes));
        }
        return sources;
    }

    public static Map<String, Path> getSourceFiles(Path path) throws IOException {
        Map<String, Path> list = new HashMap<>();
        getSourceFiles(path, list);
        return list;
    }

    private static void getSourceFiles(Path path, Map<String, Path> list) throws IOException {
        if (!Files.exists(path)) return;
        try (DirectoryStream<Path> subdirs = Files.newDirectoryStream(path)) {
            for (Path file : subdirs) {
                if (Files.isDirectory(file)) {
                    getSourceFiles(file, list);
                } else if (file.toString().endsWith(".java")) {
                    list.put(file.getFileName().toString().replace(".java", ""), file);
                }
            }
        }
    }

    public static String concatCode(Map<String, String> code, String className) {
        StringBuilder res = new StringBuilder();
        HashSet<String> imports = new HashSet<>();

        for (String s : defaultImports6) {
            imports.add("import " + s + ";");
        }
        if (javaVersion >= 8) {
            for (String s : defaultImports8) {
                imports.add("import " + s + ";");
            }
        }

        Set<String> entrySet = new HashSet<>(Arrays.asList(entryFiles));
        String tr = null;
        outer: while (true) {
            if (tr != null) code.remove(tr);
            for (Map.Entry<String, String> c : code.entrySet()) {
                if (!removeUnused || res.indexOf(c.getKey()) >= 0 || entrySet.contains(c.getKey())) {
                    concat(c.getValue(), res, imports, className);
                    tr = c.getKey();
                    continue outer;
                }
            }
            break;
        }

        StringBuilder rBuilder = new StringBuilder();
        for (String s : imports) {
            rBuilder.append(s + "\n");
        }
        rBuilder.append("\n\n\n// Solution can be found in Submission.testCase(). psvm method can be found in Main.main(String[] args)\n// Please note that the code isn't obfuscated; only compressed");
        rBuilder.append(res);
        return compress(rBuilder.toString());
    }

    public static void openOutput(Path path) throws IOException {
        Desktop.getDesktop().open(path.toFile());
    }


    public static void runTest(Path root, String className) throws IOException, InterruptedException {
        Path outDir = root.resolve("out/compile.temp.nosync");
        Files.createDirectories(outDir);

        System.out.println("Running javac for " + className + " in " + root + "...");
        ProcessBuilder javacBuilder = new ProcessBuilder("javac", root.resolve("out/" + className + ".java").toString(), "-d", outDir.toString(), "-source", "1." + javaVersion);
        javacBuilder.redirectErrorStream(true);
        Process process = javacBuilder.start();
        boolean noOutput = true;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                noOutput = false;
                System.out.println("javac> " + line);
            }
        }
        process.waitFor();
        if (noOutput) System.out.println("javac> Success!");
        System.out.println("javac finished with exit code " + process.exitValue());

        System.out.println("Cleaning up...");
        deleteDirectory(outDir);
    }

    public static String compress(String s) {
        if (!compress) return s;
        Pattern pattern = Pattern.compile("\\/\\*\\s*DISABLE-CODE-COMPRESSION:\\s*TRUE\\s*\\*\\/");
        if (pattern.matcher(s.toUpperCase()).find()) return s;


        // Comments
        s = removeComments(s);

        // Unnecessary stuff
        s = s.replaceAll( "(@Override|@Documented)|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")", "$2");
        if (javaVersion >= 8)
            s = s.replaceAll( "(final)([^l])|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")", "$2$3");

        // Multiple whitespaces
        s = s.replaceAll("\\s+", " ");

        // Unnecessary whitespaces between operators
        s = s.replaceAll( "\\s?(;|\\{|\\}|\\(|\\)|\\[|\\]|\\,|\\=|\\+|\\-|\\*|\\/|\\%|\\<|\\>|\\.\\.\\.|\\&|\\||\\:|\\^|\\!|\\?)\\s?|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")", "$1$2");

        return s;
    }

    private static String removeComments(String s) {
        return s.replaceAll( "//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/", "$1");
    }



    public static String pudCode(String code, String className) {
        code = code.replaceAll("/\\.\\.\\*", "/*");
        code = code.replaceAll("\\*\\.\\./", "*/");
        String replacerRegex;
        if (javaVersion < 8) {
            replacerRegex = "(?s)\\/\\*\\s*BEGIN-JAVA-8\\s*\\*\\/.*?\\/\\*\\s*END-JAVA-8\\s*\\*\\/";
        } else {
            replacerRegex = "(?s)\\/\\*\\s*BEGIN-POLYFILL-6\\s*\\*\\/.*?\\/\\*\\s*END-POLYFILL-6\\s*\\*\\/";
        }
        code = code.replaceAll(replacerRegex, "");
        if (type.isOptimizer) {
            replacerRegex += "(?s)\\/\\*\\s*BEGIN-NO-OPTIMIZER\\s*\\*\\/.*?\\/\\*\\s*END-NO-OPTIMIZER\\s*\\*\\/";
        } else {
            replacerRegex += "(?s)\\/\\*\\s*BEGIN-OPTIMIZER\\s*\\*\\/.*?\\/\\*\\s*END-OPTIMIZER\\s*\\*\\/";
        }
        code = code.replaceAll(replacerRegex, "");
        code = code.replaceAll("(?s)\\/\\*\\s*BEGIN-NO-BUNDLE\\s*\\*\\/.*?\\/\\*\\s*END-NO-BUNDLE\\s*\\*\\/", "");
        code = code.replaceAll("(?s)\\/\\*\\s*MAIN-CLASS-NAME\\s*\\*\\/", className);
        code = compress(code);

        return code.trim();
    }

    public static void concat(String code, StringBuilder res, HashSet<String> imports, String className) {
        code = pudCode(code, className);
        if (code.equals("")) return;

        res.append("\n\n");
        boolean hasBegun = false;
        outer: for (String k : code.split(";")) {
            String s = k.replaceAll("    ", "\t");

            String l = removeComments(s).trim();
            if (l.isEmpty()) {
                if (!compress) res.append("\n");
                continue;
            }

            if (!hasBegun && l.startsWith("import")) {
                for (String u : defaultImports6) {
                    if (l.matches("^import\\s+" + u.replaceAll("\\.", "\\\\.").replaceAll("\\*", "\\[\\^\\\\.\\]\\*"))) {
                        continue outer;
                    }
                }
                for (String u : defaultImports8) {      // no check whether we're on 8; still remove the import cuz of polyfill
                    if (l.matches("^import\\s+" + u.replaceAll("\\.", "\\\\.").replaceAll("\\*", "\\[\\^\\\\.\\]\\*"))) {
                        continue outer;
                    }
                }

                for (String u : ignoredPackages) {
                    if (l.matches("^import\\s+" + u.replaceAll("\\.", "\\."))) {
                        continue outer;
                    }
                }
                imports.add(l + ";");
            } else if (!hasBegun && l.startsWith("package")) {
                // do nothing...
            } else {
                s = s.replaceFirst("public\\s+(|final\\s*|abstract\\s*)(class|enum|interface|@interface)", "$1$2");
                /*if (!hasBegun && (l.startsWith("public ") || l.startsWith("final public ") || l.startsWith("abstract public "))) {
                    s = s.replaceFirst("public ", "");
                }*/
                hasBegun = true;
                res.append(s + ";");
            }
        }
    }


    public static void deleteDirectory(Path path) throws IOException {
        if (!Files.exists(path)) return;

        if (Files.isDirectory(path)) {
            for (Path p : Files.newDirectoryStream(path)) {
                deleteDirectory(p);
            }
        }

        Files.delete(path);
    }
}




/* END-NO-BUNDLE */