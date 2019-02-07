package lib.utils.various;

public class Either<A, B> extends Structure {

    private final A a;
    private final B b;
    private final boolean isLeft;

    private Either(A a, Void v) {
        this.a = a;
        this.b = null;
        this.isLeft = true;
    }

    private Either(Void v, B b) {
        this.a = null;
        this.b = b;
        this.isLeft = false;
    }


    public boolean isLeft() {
        return isLeft;
    }

    public boolean isRight() {
        return !isLeft;
    }

    public A getLeft() {
        return a;
    }

    public B getRight() {
        return b;
    }




    public static <A, B> Either<A, B> left(A a) {
        return new Either<A, B>(a, null);
    }

    public static <A, B> Either<A, B> right(B b) {
        return new Either<A, B>(null, b);
    }

}
