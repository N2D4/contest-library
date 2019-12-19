package lib.utils.various;

import lib.generated.*;
import lib.utils.Arr;
import lib.utils.function.Cons;
import lib.utils.function.Func;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/* GENERIFY-THIS */
class ExtendedStreamImpl<T> implements ExtendedStream<T> {
    private final Lazy<? extends Spliterator<T>> lazySpliterator;
    private boolean isStreamParallel;
    private Runnable closeHandler;

    ExtendedStreamImpl(Lazy<? extends Spliterator<T>> lazySpliterator, boolean isParallel) {
        this.lazySpliterator = lazySpliterator;
        this.isStreamParallel = isParallel;
        this.closeHandler = () -> {};
    }


    private static IntStream normieStream(Spliterator.OfInt spliterator, boolean isParallel) {
        return StreamSupport.intStream(spliterator, isParallel);
    }

    private static LongStream normieStream(Spliterator.OfLong spliterator, boolean isParallel) {
        return StreamSupport.longStream(spliterator, isParallel);
    }

    private static DoubleStream normieStream(Spliterator.OfDouble spliterator, boolean isParallel) {
        return StreamSupport.doubleStream(spliterator, isParallel);
    }

    private static <U> Stream<U> normieStream(Spliterator<U> spliterator, boolean isParallel) {
        return StreamSupport.stream(spliterator, isParallel);
    }


    private IntExtendedStream ofLazyIntegerSpliterator(Lazy<Spliterator.OfInt> spliterator) {
        Runnable closeHandler = this.closeHandler;
        IntExtendedStream stream = IntExtendedStream.ofLazySpliterator(spliterator, isParallel()).onClose(closeHandler);
        this.closeHandler = stream::close;
        return stream;
    }

    private LongExtendedStream ofLazyLongSpliterator(Lazy<Spliterator.OfLong> spliterator) {
        Runnable closeHandler = this.closeHandler;
        LongExtendedStream stream = LongExtendedStream.ofLazySpliterator(spliterator, isParallel()).onClose(closeHandler);
        this.closeHandler = stream::close;
        return stream;
    }

    private DoubleExtendedStream ofLazyDoubleSpliterator(Lazy<Spliterator.OfDouble> spliterator) {
        Runnable closeHandler = this.closeHandler;
        DoubleExtendedStream stream = DoubleExtendedStream.ofLazySpliterator(spliterator, isParallel()).onClose(closeHandler);
        this.closeHandler = stream::close;
        return stream;
    }

    private <U> ExtendedStream<U> ofLazyObjectSpliterator(Lazy<Spliterator<U>> spliterator) {
        Runnable closeHandler = this.closeHandler;
        ExtendedStream<U> stream = ExtendedStream.ofLazySpliterator(spliterator, isParallel()).onClose(closeHandler);
        this.closeHandler = stream::close;
        return stream;
    }


    private ExtendedStream<T> arrOpF(Func</*BOX T*/Object[], /*BOX T*/Object[]> serial, Func</*BOX T*/Object[], /*BOX T*/Object[]> parallel, IntIntFunc characteristics) {
        return l(orig -> {
            boolean isParallel = isParallel();
            int charas = orig.characteristics();

            /*BOX T*/Object[] arr = normieStream(orig, isParallel).toArray();
            arr = (isParallel ? parallel : serial).apply(arr);

            Spliterator<T> spliterator = Spliterators.spliterator(arr, Spliterator.ORDERED | Spliterator.IMMUTABLE | characteristics.apply(charas));
            return spliterator;
        });
    }

    private ExtendedStream<T> arrOpC(Cons</*BOX T*/Object[]> serial, Cons</*BOX T*/Object[]> parallel, IntIntFunc characteristics) {
        return arrOpF(a -> {
            serial.accept(a);
            return a;
        }, a -> {
            parallel.accept(a);
            return a;
        }, characteristics);
    }

    private IntExtendedStream li(Func<Spliterator<T>, Spliterator.OfInt> func) {
        return ofLazyIntegerSpliterator(() -> func.apply(lazySpliterator.get()));
    }

    private LongExtendedStream ll(Func<Spliterator<T>, Spliterator.OfLong> func) {
        return ofLazyLongSpliterator(() -> func.apply(lazySpliterator.get()));
    }

    private DoubleExtendedStream ld(Func<Spliterator<T>, Spliterator.OfDouble> func) {
        return ofLazyDoubleSpliterator(() -> func.apply(lazySpliterator.get()));
    }

    private <U> ExtendedStream<U> lo(Func<Spliterator<T>, Spliterator<U>> func) {
        return ofLazyObjectSpliterator(() -> func.apply(lazySpliterator.get()));
    }

    /**
     * Helper function for intermediate stream operations.
     */
    private IntExtendedStream ci(Func<Stream<T>, IntStream> func) {
        return li(s -> func.apply(normieStream(s, isParallel())).spliterator());
    }

    /**
     * Helper function for intermediate stream operations.
     */
    private LongExtendedStream cl(Func<Stream<T>, LongStream> func) {
        return ll(s -> func.apply(normieStream(s, isParallel())).spliterator());
    }

    /**
     * Helper function for intermediate stream operations.
     */
    private DoubleExtendedStream cd(Func<Stream<T>, DoubleStream> func) {
        return ld(s -> func.apply(normieStream(s, isParallel())).spliterator());
    }

    /**
     * Helper function for intermediate stream operations.
     */
    private <U> ExtendedStream<U> co(Func<Stream<T>, Stream<U>> func) {
        return lo(s -> func.apply(normieStream(s, isParallel())).spliterator());
    }

    /*IS-PRIMITIVE T./
    private ExtendedStream<T> l(Func<Spliterator<T>, Spliterator<T>> func) {
        return ofLazy/*OBJT* /TSpliterator(() -> func.apply(lazySpliterator.get()));
    }

    private ExtendedStream<T> c(Func<Stream<T>, Stream<T>> func) {
        return l(s -> func.apply(normieStream(s, isParallel())).spliterator());
    }
    /.ELSE*/
    private <R> ExtendedStream<R> l(Func<Spliterator<T>, Spliterator<R>> func) {
        return ofLazyObjectSpliterator(() -> func.apply(lazySpliterator.get()));
    }

    /**
     * Helper function for intermediate stream operations.
     */
    private <R> ExtendedStream<R> c(Func<Stream<T>, Stream<R>> func) {
        return l(s -> func.apply(normieStream(s, isParallel())).spliterator());
    }
    /*END*/

    /**
     * Helper function for terminal stream operations.
     */
    private Stream<T> s() {
        return normieStream(lazySpliterator.get(), isParallel());
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
        return co((s -> s.mapToObj(mapper)));
    }

    @Override
    public ExtendedStream</*OBJT* /T> boxed() {
        return co((s -> s.boxed()));
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
        return cd((s -> s.asDoubleStream()));
    }

    @Override
    public LongExtendedStream asLongStream() {
        return cl((s -> s.asLongStream()));
    }
    /.ELSE*/
    @Override
    public IntExtendedStream mapToInt(ToIntFunction<? super T> mapper) {
        return ci(s -> s.mapToInt(mapper));
    }
    /*END*/

    /*IS-Long T./
    @Override
    public DoubleExtendedStream asDoubleStream() {
        return cd((s -> s.asDoubleStream()));
    }
    /.ELSE*/
    @Override
    public LongExtendedStream mapToLong(ToLongFunction<? super T> mapper) {
        return cl(s -> s.mapToLong(mapper));
    }
    /*END*/

    /*IS-Double T./
    /.ELSE*/
    @Override
    public DoubleExtendedStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return cd(s -> s.mapToDouble(mapper));
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
        return ci((s -> s.flatMapToInt(mapper)));
    }

    @Override
    public LongExtendedStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        return cl((s -> s.flatMapToLong(mapper)));
    }

    @Override
    public DoubleExtendedStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        return cd((s -> s.flatMapToDouble(mapper)));
    }
    /*END*/

    @Override
    public ExtendedStream<T> distinct() {
        return c(Stream<T>::distinct);
    }

    /*IS-PRIMITIVE T./
    @Override
    public ExtendedStream<T> sorted() {
        return arrOpC(Arr::sort, Arr::parallelSort, c -> c | Spliterator.SORTED);
    }
    /.ELSE*/
    @Override
    public ExtendedStream<T> sorted() {
        return sorted((a, b) -> ((Comparable)a).compareTo(b));
    }
    @Override
    public ExtendedStream<T> sorted(Comparator<? super T> comparator) {
        return arrOpC(a -> Arr.sort(a, (Comparator<Object>) comparator), a -> Arr.parallelSort(a, (Comparator<Object>) comparator), c -> c | Spliterator.SORTED);
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
        return isStreamParallel;
    }

    @Override
    public ExtendedStream<T> sequential() {
        this.isStreamParallel = false;
        return this;
    }

    @Override
    public ExtendedStream<T> parallel() {
        this.isStreamParallel = true;
        return this;
    }

    @Override
    public ExtendedStream<T> unordered() {
        return c(Stream<T>::unordered);
    }

    @Override
    public ExtendedStream<T> onClose(Runnable closeHandler) {
        this.closeHandler = () -> {
            this.closeHandler.run();
            closeHandler.run();
        };
        return this;
    }

    @Override
    public void close() {
        closeHandler.run();
        closeHandler = null;
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
