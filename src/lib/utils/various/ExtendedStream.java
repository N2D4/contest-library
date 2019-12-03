package lib.utils.various;

import java.util.function.Predicate;
import java.util.stream.*;

/* GENERIFY-THIS */
public interface ExtendedStream<T> extends Stream<T> {
    static /*IS-PRIMITIVE T./ /.ELSE*/<T>/*END*/ ExtendedStream<T> ofStream(Stream<T> stream) {
        if (stream instanceof /*PREFIX T*/ExtendedStream) return (ExtendedStream<T>) stream;
        return ofLazyStream(() -> stream);
    }

    static /*IS-PRIMITIVE T./ /.ELSE*/<T>/*END*/ ExtendedStream<T> ofLazyStream(Lazy<Stream<T>> stream) {
        return new ExtendedStreamImpl<T>(stream);
    }
}
