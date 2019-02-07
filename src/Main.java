import lib.contest.AbstractSubmission;
import lib.ml.GeneticOptimizer;
import lib.utils.tuples.Pair;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.text.*;
import java.util.*;

class Main {
    public static void main(String[] args) throws Exception {
/* BEGIN-OPTIMIZER */
        if (GeneticOptimizer.canOptimize(/* MAIN-CLASS-NAME */.class)) {
            String inDir;
            if (args.length >= 1) {
                inDir = args[0];
            } else {
                System.out.println("Please enter a working directory which is then searched for .in and .in.txt files:");
                inDir = new Scanner(System.in).nextLine();
            }
            Files.list(Paths.get(inDir)).filter(inPath -> inPath.toString().endsWith(".in.txt") || inPath.toString().endsWith(".in")).forEachOrdered(inPath -> {
                try {
                    System.out.println("===== Processing input file: " + inPath.getFileName() + " ======");
                    String inStr = new String(Files.readAllBytes(inPath));

                    Pair<Double, String> res = GeneticOptimizer.runLoudly(/* MAIN-CLASS-NAME */.class, /* MAIN-CLASS-NAME */::new, (/* MAIN-CLASS-NAME */ a) -> {
                        InputStream in = new ByteArrayInputStream(inStr.getBytes(StandardCharsets.UTF_8));
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        a.runSubmission(in, new PrintStream(out));
                        return new Pair<Double, String>(a.score, out.toString());
                    });

                    if (res != null) {
                        System.out.println("Best score for " + inPath.getFileName() + ": " + res.a);
                        Path outPath = Paths.get(inPath.toString() + " sc" + res.a + " " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + " " + Math.floor(Math.random() * 100000) + ".out.txt");
                        System.out.println("Saving best output in: " + outPath);
                        Files.write(outPath, res.b.getBytes(), StandardOpenOption.CREATE);
                    }
                } catch (IOException e) { throw new RuntimeException(e); }
            });
            System.out.println("Went through all input files!");
        } else {
            System.out.println("Optimizer flag enabled but submission can't be optimized. Running it as normal submission instead");
/* END-OPTIMIZER */
            new /* MAIN-CLASS-NAME */().runSubmission();
/* BEGIN-OPTIMIZER */
        }
/* END-OPTIMIZER */
    }
}