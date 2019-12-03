package lib.utils.tuples;

import lib.utils.function.Func;

public class Triple<A, B, C> extends Tuple {

    public A a;
    public B b;
    public C c;

    public Triple(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A getLeft() {
        return a;
    }

    public B getMiddle() {
        return b;
    }

    public C getRight() {
        return c;
    }

    public void setLeft(A a) {
        this.a = a;
    }

    public void setMiddle(B b) {
        this.b = b;
    }

    public void setRight(C c) {
        this.c = c;
    }

    public <A1, B1, C1> Triple<A1, B1, C1> map(Func<A, A1> mapA, Func<B, B1> mapB, Func<C, C1> mapC) {
        return new Triple<>(mapA.apply(this.a), mapB.apply(this.b), mapC.apply(this.c));
    }



    @Override
    public Object[] toArray() {
        return new Object[] {a, b, c};
    }

}
