/* Generated by Generificator on Tue Dec 03 22:33:01 CET 2019 from ExtendedStream.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import lib.utils.various.*;

import java.util.function.Predicate;
import java.util.stream.*;


public interface DoubleExtendedStream extends DoubleStream {
    static   DoubleExtendedStream ofStream(DoubleStream stream) {
        if (stream instanceof DoubleExtendedStream) return (DoubleExtendedStream) stream;
        return ofLazyStream(() -> stream);
    }

    static   DoubleExtendedStream ofLazyStream(Lazy<DoubleStream> stream) {
        return new DoubleExtendedStreamImpl(stream);
    }
}
