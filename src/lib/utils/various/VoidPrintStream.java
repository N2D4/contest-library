package lib.utils.various;

import java.io.PrintStream;
import java.util.Locale;

public final class VoidPrintStream extends PrintStream {
    public VoidPrintStream() {
        super(new VoidOutputStream());
    }

    @Override
    public void write(int b) {
        return;
    }

    @Override
    public void print(char[] buf) {
        return;
    }

    @Override
    public void print(String s) {
        return;
    }

    @Override
    public void print(Object obj) {
        return;
    }

    @Override
    public void println(char[] buf) {
        return;
    }

    @Override
    public void println(String s) {
        return;
    }

    @Override
    public void println(Object obj) {
        return;
    }

    @Override
    public PrintStream format(Locale l, String s, Object... objs) {
        return this;
    }

    @Override
    public PrintStream format(String s, Object... objs) {
        return this;
    }



}
