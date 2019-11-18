package lib.utils.tuples;

import lib.utils.Utils;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

/* GENERIFY-THIS */
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

    public <A1, B1> Pair<A1, B1> map(Function<A, A1> mapA, Function<B, B1> mapB) {
        return new Pair<A1, B1>(mapA.apply(this.a), mapB.apply(this.b));
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

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof /*PREFIX A*//*PREFIX B*/Pair)) return super.equals(other);
        Pair pair = (Pair) other;
        return Utils.equals(this.a, pair.a) && Utils.equals(this.b, pair.b);
    }

    @Override
    public int hashCode() {
        // Override hash code so we get slightly faster hashing (without having to allocate an array)
        return 961 + 31 * Utils.hashCode(a) + Utils.hashCode(b);
    }
}
