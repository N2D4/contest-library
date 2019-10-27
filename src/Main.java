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
        Class<? extends AbstractSubmission> clss = /* MAIN-CLASS-NAME */.class;
/* BEGIN-OPTIMIZER */
        if (true) {
            String inDir;
            if (args.length >= 1) {
                inDir = args[0];
            } else {
                System.out.println("Please enter a working directory which is then searched for .in and .in.txt files: (empty for kwohlwend/Downloads)");
                inDir = new Scanner(System.in).nextLine();
                if (inDir.trim().equals("")) inDir = "/Users/konstantinwohlwend/Downloads";
            }
            AbstractSubmission.optimizeFromPath(clss, inDir, false);
        } else {
            // should not trigger anymore
            System.out.println("Optimizing disabled. Running as a normal submission");
/* END-OPTIMIZER */
            AbstractSubmission.create(clss).runSubmission();
/* BEGIN-OPTIMIZER */
        }
/* END-OPTIMIZER */
    }
}