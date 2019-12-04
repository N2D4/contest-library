package lib.utils.various;

import lib.utils.Utils;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IterativeRecursionIterable<T, R> implements Iterable<R> {

    private final BiFunction<T, Consumer<T>, ?> pre;
    private final Function<Object, R> post;
    private final List<Either<T, Object>> startingVals;

    public <E> IterativeRecursionIterable(List<T> startingValues, BiFunction<T, Consumer<T>, E> preProcess, Function<E, R> postProcess) {
        this.pre = preProcess;
        this.post = (Function<Object, R>) postProcess;
        this.startingVals = new ArrayList<>(startingValues.size());
        for (T t : startingValues) {
            this.startingVals.add(Either.left(t));
        }
    }

    @Override
    public Iterator<R> iterator() {
        return new Iterator<R>() {
            R nextObj = null;
            ArrayDeque<Either<T, Object>> stack = new ArrayDeque<Either<T, Object>>(startingVals);

            @Override
            public boolean hasNext() {
                while (nextObj == null) {
                    if (stack.isEmpty()) return false;

                    Either<T, Object> next = stack.removeLast();
                    if (next.isLeft()) {
                        T t = next.getLeft();
                        Deque<T> st = new ArrayDeque<T>();
                        Object obj = pre.apply(t, stackConsumer(st));
                        stack.add(Either.right(obj));
                        while (!st.isEmpty()) {
                            stack.add(Either.left(st.removeLast()));
                        }
                    } else {
                        Object e = next.getRight();
                        nextObj = post.apply(e);
                    }
                }
                return true;
            }

            @Override
            public R next() {
                if (!hasNext()) throw new NoSuchElementException();

                R next = nextObj;
                nextObj = null;
                return next;
            }
        };
    }

    private static <T> Consumer<T> stackConsumer(final Deque<T> st) {
        /* BEGIN-JAVA-8 */
        return st::add;
        /* END-JAVA-8 */
        /* BEGIN-POLYFILL-6 *../
        return new Consumer<T>() {
            @Override
            public void accept(T t) {
                st.add(t);
            }
        };
        /..* END-POLYFILL-6 */
    }

    public void runAll() {
        for (Iterator<R> iterator = iterator(); iterator().hasNext(); iterator.next());
    }

    public Stream<R> stream() {
        return Utils.stream(spliterator());
    }

    public Stream<R> parallelStream() {
        return stream().parallel();
    }
}
