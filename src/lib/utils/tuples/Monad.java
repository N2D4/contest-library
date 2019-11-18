package lib.utils.tuples;

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

    public <T> Monad<T> map(Function<A, T> mappingFunction) {
        return new Monad<T>(mappingFunction.apply(this.value));
    }

    @Override
    public Object[] toArray() {
        return new Object[] {value};
    }
}
