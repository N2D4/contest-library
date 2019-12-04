package lib.utils.various;

import lib.generated.DoubleExtendedStream;
import lib.generated.IntExtendedStream;
import lib.generated.LongExtendedStream;
import lib.utils.Arr;
import lib.utils.function.Cons;
import lib.utils.function.Func;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/* GENERIFY-THIS */
class ExtendedStreamImpl<T> implements ExtendedStream<T> {
    private final Lazy<Stream<T>> lazyStream;

    ExtendedStreamImpl(Lazy<Stream<T>> lazyStream) {
        this.lazyStream = lazyStream;
    }



    private Stream<T> portStream(Stream<T> stream, Stream<T> from) {
        return stream.onClose(from::close);
    }

    private ExtendedStream<T> arrOpF(Func</*BOX T*/Object[], /*BOX T*/Object[]> func) {
        return c(s -> {
            /*BOX T*/Object[] arr = s.toArray();
            arr = func.apply(arr);
            return portStream((Stream<T>) Arr.stream(arr), s);
        });
    }

    private ExtendedStream<T> arrOpC(Cons</*BOX T*/Object[]> func) {
        return arrOpF(a -> {
            func.accept(a);
            return a;
        });
    }

    private Lazy<IntStream> li(Func<Stream<T>, IntStream> func) {
        return () -> func.apply(lazyStream.get());
    }

    private Lazy<LongStream> ll(Func<Stream<T>, LongStream> func) {
        return () -> func.apply(lazyStream.get());
    }

    private Lazy<DoubleStream> ld(Func<Stream<T>, DoubleStream> func) {
        return () -> func.apply(lazyStream.get());
    }

    private <U> Lazy<Stream<U>> lo(Func<Stream<T>, Stream<U>> func) {
        return () -> func.apply(lazyStream.get());
    }

    /*IS-PRIMITIVE T./
    private Lazy<Stream<T>> l(Func<Stream<T>, Stream<T>> func) {
        return () -> func.apply(lazyStream.get());
    }

    private ExtendedStreamImpl<T> c(Func<Stream<T>, Stream<T>> func) {
        return new ExtendedStreamImpl<T>(l(func));
    }
    /.ELSE*/
    private <R> Lazy<Stream<R>> l(Func<Stream<T>, Stream<R>> func) {
        return () -> func.apply(lazyStream.get());
    }

    private <R> ExtendedStreamImpl<R> c(Func<Stream<T>, Stream<R>> func) {
        return new ExtendedStreamImpl<>(l(func));
    }
    /*END*/

    private Stream<T> s() {
        return lazyStream.get();
    }





    @Override
    public ExtendedStream<T> filter(Predicate<? super T> predicate) {
        return c(s -> s.filter(predicate));
    }

    /*IS-PRIMITIVE T./
    @Override
    public ExtendedStream<T> map(Function<? super T, ? extends T> mapper) {
        return c(s -> s.map(mapper));
    }

    @Override
    public <U> ExtendedStream<U> mapToObj(Function<T, ? extends U> mapper) {
        return ExtendedStream.ofLazyStream(lo(s -> s.mapToObj(mapper)));
    }

    @Override
    public ExtendedStream</*OBJT* /T> boxed() {
        return ExtendedStream.ofLazyStream(lo(s -> s.boxed()));
    }
    /.ELSE*/
    @Override
    public <R> ExtendedStream<R> map(Function<? super T, ? extends R> mapper) {
        return c(s -> s.map(mapper));
    }
    /*END*/

    /*IS-Int T./
    @Override
    public DoubleExtendedStream asDoubleStream() {
        return DoubleExtendedStream.ofLazyStream(ld(s -> s.asDoubleStream()));
    }

    @Override
    public LongExtendedStream asLongStream() {
        return LongExtendedStream.ofLazyStream(ll(s -> s.asLongStream()));
    }
    /.ELSE*/
    @Override
    public IntExtendedStream mapToInt(ToIntFunction<? super T> mapper) {
        return IntExtendedStream.ofLazyStream(li(s -> s.mapToInt(mapper)));
    }
    /*END*/

    /*IS-Long T./
    @Override
    public DoubleExtendedStream asDoubleStream() {
        return DoubleExtendedStream.ofLazyStream(ld(s -> s.asDoubleStream()));
    }
    /.ELSE*/
    @Override
    public LongExtendedStream mapToLong(ToLongFunction<? super T> mapper) {
        return LongExtendedStream.ofLazyStream(ll(s -> s.mapToLong(mapper)));
    }
    /*END*/

    /*IS-Double T./
    /.ELSE*/
    @Override
    public DoubleExtendedStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return DoubleExtendedStream.ofLazyStream(ld(s -> s.mapToDouble(mapper)));
    }
    /*END*/

    /*IS-PRIMITIVE T./
    @Override
    public ExtendedStream<T> flatMap(Function<? super T, ? extends Stream<? extends T>> mapper) {
        return c(s -> s.flatMap(mapper));
    }
    /.ELSE*/
    @Override
    public <R> ExtendedStream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return c(s -> s.flatMap(mapper));
    }

    @Override
    public IntExtendedStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        return IntExtendedStream.ofLazyStream(li(s -> s.flatMapToInt(mapper)));
    }

    @Override
    public LongExtendedStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        return LongExtendedStream.ofLazyStream(ll(s -> s.flatMapToLong(mapper)));
    }

    @Override
    public DoubleExtendedStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        return DoubleExtendedStream.ofLazyStream(ld(s -> s.flatMapToDouble(mapper)));
    }
    /*END*/

    @Override
    public ExtendedStream<T> distinct() {
        return c(Stream<T>::distinct);
    }

    @Override
    public ExtendedStream<T> sorted() {
        return arrOpC(Arr::sort);
    }

    /*IS-PRIMITIVE T./
    /.ELSE*/
    @Override
    public ExtendedStream<T> sorted(Comparator<? super T> comparator) {
        return arrOpC(a -> Arr.sort(a, (Comparator<Object>) comparator));
    }
    /*END*/

    @Override
    public ExtendedStream<T> peek(Consumer<? super T> action) {
        return c(s -> s.peek(action));
    }

    @Override
    public ExtendedStream<T> limit(long maxSize) {
        return c(s -> s.limit(maxSize));
    }

    @Override
    public ExtendedStream<T> skip(long n) {
        return c(s -> s.skip(n));
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        s().forEach(action);
    }

    @Override
    public void forEachOrdered(Consumer<? super T> action) {
        s().forEachOrdered(action);
    }

    @Override
    public /*BOX T*/Object[] toArray() {
        return s().toArray();
    }

    /*IS-PRIMITIVE T./
    /.ELSE*/
    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        return s().toArray(generator);
    }
    /*END*/

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return s().reduce(identity, accumulator);
    }

    @Override
    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        return s().reduce(accumulator);
    }

    /*IS-PRIMITIVE T./
    /.ELSE*/
    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return s().reduce(identity, accumulator, combiner);
    }
    /*END*/

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return s().collect(supplier, accumulator, combiner);
    }

    /*IS-PRIMITIVE T./
    /.ELSE*/
    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return s().collect(collector);
    }
    /*END*/

    /*IS-PRIMITIVE T./
    @Override
    public Optional<T> min() {
        return s().min();
    }

    @Override
    public Optional<T> max() {
        return s().max();
    }
    /.ELSE*/
    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        return s().min(comparator);
    }

    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        return s().max(comparator);
    }
    /*END*/

    @Override
    public long count() {
        return s().count();
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        return s().anyMatch(predicate);
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return s().allMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        return s().noneMatch(predicate);
    }

    @Override
    public Optional<T> findFirst() {
        return s().findFirst();
    }

    @Override
    public Optional<T> findAny() {
        return s().findAny();
    }

    /*IS-PRIMITIVE T./
    @Override
    public PrimitiveIterator.Of<T> iterator() {
        return s().iterator();
    }

    @Override
    public Spliterator.Of<T> spliterator() {
        return s().spliterator();
    }
    /.ELSE*/
    @Override
    public Iterator<T> iterator() {
        return s().iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return s().spliterator();
    }
    /*END*/

    @Override
    public boolean isParallel() {
        return s().isParallel();
    }

    @Override
    public ExtendedStream<T> sequential() {
        return c(Stream<T>::sequential);
    }

    @Override
    public ExtendedStream<T> parallel() {
        return c(Stream<T>::parallel);
    }

    @Override
    public ExtendedStream<T> unordered() {
        return c(Stream<T>::unordered);
    }

    @Override
    public ExtendedStream<T> onClose(Runnable closeHandler) {
        return c(s -> s.onClose(closeHandler));
    }

    @Override
    public void close() {
        s().close();
    }

    /*IS-PRIMITIVE T./
    @Override
    public T sum() {
        return s().sum();
    }

    @Override
    public OptionalDouble average() {
        return s().average();
    }

    @Override
    public SummaryStatistics<T> summaryStatistics() {
        return s().summaryStatistics();
    }
    /.ELSE*/
    /*END*/
}
