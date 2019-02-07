package lib.utils.tuples;

import java.util.Map;

public class Pair<A, B> extends Tuple implements Map.Entry<A, B> {

    public A a;
    public B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getLeft() {
        return a;
    }

    public B getRight() {
        return b;
    }

    public void setLeft(A a) {
        this.a = a;
    }

    public void setRight(B b) {
        this.b = b;
    }

    @Override
    public A getKey() {
        return a;
    }

    public A setKey(A a) {
        A old = this.a;
        this.a = a;
        return old;
    }

    @Override
    public B getValue() {
        return b;
    }

    @Override
    public B setValue(B b) {
        B old = this.b;
        this.b = b;
        return old;
    }

    @Override
    public Object[] toArray() {
        return new Object[] {a, b};
    }
}
