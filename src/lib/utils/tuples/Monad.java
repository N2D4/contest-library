package lib.utils.tuples;

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

    @Override
    public Object[] toArray() {
        return new Object[] {value};
    }
}
