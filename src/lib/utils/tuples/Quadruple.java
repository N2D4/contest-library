package lib.utils.tuples;

import java.util.function.Function;

public class Quadruple<A, B, C, D> extends Tuple {

    public A a;
    public B b;
    public C c;
    public D d;

    public Quadruple(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public <A1, B1, C1, D1> Quadruple<A1, B1, C1, D1> map(Function<A, A1> mapA, Function<B, B1> mapB, Function<C, C1> mapC, Function<D, D1> mapD) {
        return new Quadruple<>(mapA.apply(this.a), mapB.apply(this.b), mapC.apply(this.c), mapD.apply(this.d));
    }


    @Override
    public Object[] toArray() {
        return new Object[] {a, b, c, d};
    }

}
