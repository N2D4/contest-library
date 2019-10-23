package lib.utils.various;

import java.io.IOException;
import java.io.OutputStream;

public final class VoidOutputStream extends OutputStream {
    @Override
    public void write(int b) {
        return;
    }

    @Override
    public void write(byte b[]) {
        return;
    }

    @Override
    public void write(byte b[], int off, int len) {
        return;
    }

}
