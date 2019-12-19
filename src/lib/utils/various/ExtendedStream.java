package lib.utils.various;

import lib.generated.DoubleExtendedStream;
import lib.generated.IntExtendedStream;
import lib.generated.LongExtendedStream;
import lib.utils.Arr;
import lib.utils.tuples.Monad;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/* GENERIFY-THIS */
public interface ExtendedStream<T> extends Stream<T> {

    @Override ExtendedStream<T> filter(Predicate<? super T> predicate);
    /*IS-PRIMITIVE T./
    @Override ExtendedStream<T> map(Function<? super T, ? extends T> mapper);
    @Override <U> ExtendedStream<U> mapToObj(Function<T, ? extends U> mapper);
    @Override ExtendedStream</*OBJT* /T> boxed();
    /.ELSE*/
    @Override <R> ExtendedStream<R> map(Function<? super T, ? extends R> mapper);
    /*END*/
    /*IS-Int T./
    @Override DoubleExtendedStream asDoubleStream();
    @Override LongExtendedStream asLongStream();
    /.ELSE*/
    @Override IntExtendedStream mapToInt(ToIntFunction<? super T> mapper);
    /*END*/
    /*IS-Long T./
    @Override DoubleExtendedStream asDoubleStream();
    /.ELSE*/
    @Override LongExtendedStream mapToLong(ToLongFunction<? super T> mapper);
    /*END*/
    /*IS-Double T./
    /.ELSE*/
    @Override DoubleExtendedStream mapToDouble(ToDoubleFunction<? super T> mapper);
    /*END*/
    /*IS-PRIMITIVE T./
    @Override ExtendedStream<T> flatMap(Function<? super T, ? extends Stream<? extends T>> mapper);
    /.ELSE*/
    @Override <R> ExtendedStream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
    @Override IntExtendedStream flatMapToInt(Function<? super T, ? extends IntStream> mapper);
    @Override LongExtendedStream flatMapToLong(Function<? super T, ? extends LongStream> mapper);
    @Override DoubleExtendedStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper);
    /*END*/
    @Override ExtendedStream<T> distinct();
    @Override ExtendedStream<T> sorted();
    /*IS-PRIMITIVE T./
    /.ELSE*/
    @Override ExtendedStream<T> sorted(Comparator<? super T> comparator);
    /*END*/
    @Override ExtendedStream<T> peek(Consumer<? super T> action);
    @Override ExtendedStream<T> limit(long maxSize);
    @Override ExtendedStream<T> skip(long n);
    @Override void forEach(Consumer<? super T> action);
    @Override void forEachOrdered(Consumer<? super T> action);
    @Override /*BOX T*/Object[] toArray();
    /*IS-PRIMITIVE T./
    /.ELSE*/
    @Override <A> A[] toArray(IntFunction<A[]> generator);
    /*END*/
    @Override T reduce(T identity, BinaryOperator<T> accumulator);
    @Override Optional<T> reduce(BinaryOperator<T> accumulator);
    /*IS-PRIMITIVE T./
    /.ELSE*/
    @Override <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);
    /*END*/
    @Override <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner);
    /*IS-PRIMITIVE T./
    /.ELSE*/
    @Override <R, A> R collect(Collector<? super T, A, R> collector);
    /*END*/
    /*IS-PRIMITIVE T./
    @Override Optional<T> min();
    @Override Optional<T> max();
    /.ELSE*/
    @Override Optional<T> min(Comparator<? super T> comparator);
    @Override Optional<T> max(Comparator<? super T> comparator);
    /*END*/
    @Override long count();
    @Override boolean anyMatch(Predicate<? super T> predicate);
    @Override boolean allMatch(Predicate<? super T> predicate);
    @Override boolean noneMatch(Predicate<? super T> predicate);
    @Override Optional<T> findFirst();
    @Override Optional<T> findAny();
    /*IS-PRIMITIVE T./
    @Override PrimitiveIterator.Of<T> iterator();
    @Override Spliterator.Of<T> spliterator();
    /.ELSE*/
    @Override Iterator<T> iterator();
    @Override Spliterator<T> spliterator();
    /*END*/
    @Override boolean isParallel();
    @Override ExtendedStream<T> sequential();
    @Override ExtendedStream<T> parallel();
    @Override ExtendedStream<T> unordered();
    @Override ExtendedStream<T> onClose(Runnable closeHandler);
    @Override void close();
    /*IS-PRIMITIVE T./
    @Override T sum();
    @Override OptionalDouble average();
    @Override SummaryStatistics<T> summaryStatistics();
    /.ELSE*/
    /*END*/

    /**
     * Returns a parallel stream if parallel is true, or sequential if it's not.
     */
    default ExtendedStream<T> setParallel(boolean parallel) {
        if (parallel) {
            return parallel();
        } else {
            return sequential();
        }
    }


    /**
     * Returns this stream. Can be used to ensure that this stream is of type ExtendedStream at compile-time (useful for
     * code generators).
     */
    default ExtendedStream<T> ensureExtended() {
        return this;
    }


    static /*IS-PRIMITIVE T./ /.ELSE*/<T>/*END*/ ExtendedStream<T> ofStream(Stream<T> stream) {
        if (stream instanceof /*PREFIX T*/ExtendedStream) return (ExtendedStream<T>) stream;
        return ofLazySpliterator(stream::spliterator, stream.isParallel()).onClose(stream::close);
    }

    static /*IS-PRIMITIVE T./ /.ELSE*/<T>/*END*/ ExtendedStream<T> ofLazySpliterator(Lazy<? extends Spliterator<T>> spliterator, boolean parallel) {
        return new ExtendedStreamImpl<T>(spliterator, parallel);
    }
}
