/* Generated by Generificator on Fri Nov 08 00:29:31 CET 2019 from Pair.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import lib.utils.tuples.*;

import lib.utils.Utils;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;


public class ObjIntPair<Obj1__> extends Tuple implements ObjIntMap.Entry<Obj1__> {

    public Obj1__ a;
    public int b;

    public ObjIntPair(Obj1__ a, int b) {
        this.a = a;
        this.b = b;
    }

    public Obj1__ getLeft() {
        return a;
    }

    public int getRight() {
        return b;
    }

    public void setLeft(Obj1__ a) {
        this.a = a;
    }

    public void setRight(int b) {
        this.b = b;
    }

    public <A1, B1> Pair<A1, B1> map(Function<Obj1__, A1> mapA, IntFunction<B1> mapB) {
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
    public int getValue() {
        return b;
    }

    @Override
    public int setValue(int b) {
        int old = this.b;
        this.b = b;
        return old;
    }

    @Override
    public Object[] toArray() {
        return new Object[] {a, b};
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ObjIntPair)) return super.equals(other);
        Pair pair = (Pair) other;
        return Utils.equals(this.a, pair.a) && Utils.equals(this.b, pair.b);
    }

    @Override
    public int hashCode() {
        // Override hash code so we get slightly faster hashing (without having to allocate an array)
        return 961 + 31 * Utils.hashCode(a) + Utils.hashCode(b);
    }
}