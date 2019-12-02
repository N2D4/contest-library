package lib.utils.various;

import lib.utils.Arr;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Java's Scanner class is very slow so this is a faster implementation which for our purposes is sufficient
 */
public class FastScanner {
    private final String CHARSET_NAME = "utf8";

    private static final String spaceDelimiters = " \t\n\r\f";
    private BufferedReader buffer;
    private StringTokenizer tokenizer;
    private final InputStream stream;

    public FastScanner(InputStream stream) {
        this.stream = stream;
    }

    /**
     * Reads all lines until EOF. Note that this does not read a line that is terminated by EOF instead of a line
     * terminator
     */
    public List<String> readAllLines() {
        ArrayList<String> res = new ArrayList<>();
        while (true) {
            String s = nextLine();
            if (s == null) break;
            res.add(s);
        }
        return Collections.unmodifiableList(res);
    }

    public List<String> readAllNonEmptyLines() {
        return Collections.unmodifiableList(readAllLines().stream().filter(a -> !a.trim().isEmpty()).collect(Collectors.toList()));
    }

    public String readAll() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        try {
            while ((nRead = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            return new String(buffer.toByteArray(), CHARSET_NAME);
        } catch (IOException e) {
            throw new RuntimeException("IO exception occured!", e);
        }
    }

    public String nextLine() {
        if (this.buffer == null) {
            try {
                this.buffer = new BufferedReader(new InputStreamReader(stream, CHARSET_NAME));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        while (tokenizer == null) {
            try {
                String rl = buffer.readLine();
                if (rl == null) return null;
                tokenizer = new StringTokenizer(rl + "\n");
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
                String rl = buffer.readLine();
                if (rl == null) return null;
                tokenizer = new StringTokenizer(rl + "\n");
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
