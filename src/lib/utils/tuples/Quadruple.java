package lib.utils.tuples;

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


    @Override
    public Object[] toArray() {
        return new Object[] {a, b, c, d};
    }

}
