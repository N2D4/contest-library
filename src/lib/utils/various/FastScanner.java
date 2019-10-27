package lib.utils.various;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.StringTokenizer;

/**
 * Java's Scanner class is very slow so this is a faster implementation which for our purposes is sufficient
 */
public class FastScanner {
    private static final String spaceDelimiters = " \t\n\r\f";
    private BufferedReader buffer;
    private StringTokenizer tokenizer;
    private final InputStream stream;

    public FastScanner(InputStream stream) {
        this.stream = stream;
    }

    public String nextLine() {
        if (this.buffer == null) this.buffer = new BufferedReader(new InputStreamReader(stream));
        while (tokenizer == null) {
            try {
                tokenizer = new StringTokenizer(buffer.readLine() + "\n");
            } catch (IOException e) {
                throw new RuntimeException("IO exception occured!", e);
            }
        }
        String res = tokenizer.nextToken("");

        if (!res.endsWith("\n")) throw new RuntimeException("waddaheq just heppenenened");
        res = res.substring(0, res.length() - 1);
        if (res.contains("\n")) throw new RuntimeException("oke wat lamo");

        tokenizer = null;
        return res;
    }

    public String next() {
        if (this.buffer == null) this.buffer = new BufferedReader(new InputStreamReader(stream));
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            try {
                tokenizer = new StringTokenizer(buffer.readLine() + "\n");
            } catch (IOException e) {
                throw new RuntimeException("IO exception occured!", e);
            }
        }
        return tokenizer.nextToken(spaceDelimiters);
    }

    public BigDecimal nextBigDecimal() {
        return new BigDecimal(next());
    }

    public BigInteger nextBigInteger() {
        return new BigInteger(next());
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }

    public long nextLong() {
        return Long.parseLong(next());
    }

    public double nextDouble() {
        return Double.parseDouble(next());
    }

}
