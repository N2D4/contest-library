/* Generated by Generificator from Pair.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import lib.utils.tuples.*;

import lib.utils.Utils;
import lib.utils.function.BiCons;
import lib.utils.function.BiFunc;
import lib.utils.function.Func;

import java.util.Map;


public class DoubleLongPair extends Tuple implements DoubleLongMap.Entry {

    public double a;
    public long b;

    public DoubleLongPair(double a, long b) {
        this.a = a;
        this.b = b;
    }

    public double getLeft() {
        return a;
    }

    public long getRight() {
        return b;
    }

    public void setLeft(double a) {
        this.a = a;
    }

    public void setRight(long b) {
        this.b = b;
    }

    public <A1, B1> Pair<A1, B1> map(DoubleObjFunc<A1> mapA, LongObjFunc<B1> mapB) {
        return new Pair<A1, B1>(mapA.apply(this.a), mapB.apply(this.b));
    }

    
    public Pair<Double, Long> boxed() {
        return new Pair<Double, Long>(a, b);
    }
    

    public <R> R match(DoubleLongObjBiFunc<R> func) {
        return func.apply(a, b);
    }

    public void match(DoubleLongBiCons func) {
        func.accept(a, b);
    }

    @Override
    public double getKey() {
        return a;
    }

    public double setKey(double a) {
        double old = this.a;
        this.a = a;
        return old;
    }

    @Override
    public long getValue() {
        return b;
    }

    @Override
    public long setValue(long b) {
        long old = this.b;
        this.b = b;
        return old;
    }

    @Override
    public Object[] toArray() {
        return new Object[] {a, b};
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DoubleLongPair)) return super.equals(other);
        // Short-circuit if both elements are tuples
        DoubleLongPair pair = (DoubleLongPair) other;
        return Utils.equals(this.a, pair.a) && Utils.equals(this.b, pair.b);
    }

    @Override
    public int hashCode() {
        // Override hash code so we get slightly faster hashing (without having to allocate an array)
        return 961 + 31 * Utils.hashCode(a) + Utils.hashCode(b);
    }
}
