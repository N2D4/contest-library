package lib.utils.tuples;

import lib.utils.function.Cons;
import lib.utils.function.Func;

import java.util.function.Function;

/* GENERIFY-THIS */
public class Monad<A> extends Tuple {

    public A value;

    public Monad(A value) {
        this.value = value;
    }

    public A getValue() {
        return value;
    }

    public void setValue(A value) {
        this.value = value;
    }

    public <T> Monad<T> map(Func<A, T> mappingFunction) {
        return new Monad<T>(mappingFunction.apply(this.value));
    }

    public <R> R match(Func<A, R> func) {
        return func.apply(value);
    }

    public void match(Cons<A> func) {
        func.accept(value);
    }

    @Override
    public Object[] toArray() {
        return new Object[] {value};
    }
}
