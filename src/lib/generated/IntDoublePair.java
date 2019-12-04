/* Generated by Generificator on Wed Dec 04 00:49:24 CET 2019 from Pair.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import lib.utils.tuples.*;

import lib.utils.Utils;
import lib.utils.function.BiCons;
import lib.utils.function.BiFunc;
import lib.utils.function.Func;

import java.util.Map;


public class IntDoublePair extends Tuple implements IntDoubleMap.Entry {

    public int a;
    public double b;

    public IntDoublePair(int a, double b) {
        this.a = a;
        this.b = b;
    }

    public int getLeft() {
        return a;
    }

    public double getRight() {
        return b;
    }

    public void setLeft(int a) {
        this.a = a;
    }

    public void setRight(double b) {
        this.b = b;
    }

    public <A1, B1> Pair<A1, B1> map(IntObjFunc<A1> mapA, DoubleObjFunc<B1> mapB) {
        return new Pair<A1, B1>(mapA.apply(this.a), mapB.apply(this.b));
    }

    
    public Pair<Integer, Double> boxed() {
        return new Pair<Integer, Double>(a, b);
    }
    

    public <R> R match(IntDoubleObjBiFunc<R> func) {
        return func.apply(a, b);
    }

    public void match(IntDoubleBiCons func) {
        func.accept(a, b);
    }

    @Override
    public int getKey() {
        return a;
    }

    public int setKey(int a) {
        int old = this.a;
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
        if (!(other instanceof IntDoublePair)) return super.equals(other);
        // Short-circuit if both elements are tuples
        IntDoublePair pair = (IntDoublePair) other;
        return Utils.equals(this.a, pair.a) && Utils.equals(this.b, pair.b);
    }

    @Override
    public int hashCode() {
        // Override hash code so we get slightly faster hashing (without having to allocate an array)
        return 961 + 31 * Utils.hashCode(a) + Utils.hashCode(b);
    }
}
