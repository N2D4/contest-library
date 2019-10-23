import lib.utils.various.FastScanner;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FastScannerTests {

    @Test
    public void scannerFastScannerCompatibility() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String next = "next",
                nextInt = "nextInt",
                nextLong = "nextLong",
                nextLine = "nextLine",
                nextDouble = "nextDouble",
                nextBigInteger = "nextBigInteger",
                nextBigDecimal = "nextBigDecimal";

        String[][] testCases = {
                {"  5 7 12 999999\n", nextInt, next, nextLong, nextInt, nextLine},
                {"1 ", nextInt},
                {"1000000000000 ", nextLong},
                {"1.0  ", nextDouble},
                {"100000000000000000000000000000000 ", nextBigInteger},
                {"100000000000000000000000000000000.1", nextBigDecimal},
                {"1\n2\n\n3 \n 4", nextInt, nextLine, nextLine, nextLine, nextBigInteger, nextInt},
                {"1 2 \n3\n4", nextInt, nextLine, nextLine, nextInt},
        };

        for (String[] tc : testCases) {
            Object sc = new Scanner(new ByteArrayInputStream(tc[0].getBytes()));
            Object fsc = new FastScanner(new ByteArrayInputStream(tc[0].getBytes()));
            System.out.println(Arrays.toString(tc));
            for (int i = 1; i < tc.length; i++) {
                System.out.println(i);
                assertEquals(sc.getClass().getMethod(tc[i]).invoke(sc), fsc.getClass().getMethod(tc[i]).invoke(fsc), i + " @ " + Arrays.toString(tc));
            }
        }
    }

}
