/* Generated by Generificator on Mon Dec 02 16:10:32 CET 2019 from Pair.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import lib.utils.tuples.*;

import lib.utils.Utils;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;


public class ObjDoublePair<Obj1__> extends Tuple implements ObjDoubleMap.Entry<Obj1__> {

    public Obj1__ a;
    public double b;

    public ObjDoublePair(Obj1__ a, double b) {
        this.a = a;
        this.b = b;
    }

    public Obj1__ getLeft() {
        return a;
    }

    public double getRight() {
        return b;
    }

    public void setLeft(Obj1__ a) {
        this.a = a;
    }

    public void setRight(double b) {
        this.b = b;
    }

    public <A1, B1> Pair<A1, B1> map(Function<Obj1__, A1> mapA, DoubleFunction<B1> mapB) {
        return new Pair<A1, B1>(mapA.apply(this.a), mapB.apply(this.b));
    }

    @Override
    public Obj1__ getKey() {
        return a;
    }

    public Obj1__ setKey(Obj1__ a) {
        Obj1__ old = this.a;
        this.a = a;
        return old;
    }

    @Override
    public double getValue() {
        return b;
    }

    @Override
    public double setValue(double b) {
        double old = this.b;
        this.b = b;
        return old;
    }

    @Override
    public Object[] toArray() {
        return new Object[] {a, b};
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ObjDoublePair)) return super.equals(other);
        Pair pair = (Pair) other;
        return Utils.equals(this.a, pair.a) && Utils.equals(this.b, pair.b);
    }

    @Override
    public int hashCode() {
        // Override hash code so we get slightly faster hashing (without having to allocate an array)
        return 961 + 31 * Utils.hashCode(a) + Utils.hashCode(b);
    }
}
