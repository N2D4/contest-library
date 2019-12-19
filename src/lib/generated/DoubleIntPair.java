/* Generated by Generificator on Thu Dec 19 21:12:35 CET 2019 from Pair.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import lib.utils.tuples.*;

import lib.utils.Utils;
import lib.utils.function.BiCons;
import lib.utils.function.BiFunc;
import lib.utils.function.Func;

import java.util.Map;


public class DoubleIntPair extends Tuple implements DoubleIntMap.Entry {

    public double a;
    public int b;

    public DoubleIntPair(double a, int b) {
        this.a = a;
        this.b = b;
    }

    public double getLeft() {
        return a;
    }

    public int getRight() {
        return b;
    }

    public void setLeft(double a) {
        this.a = a;
    }

    public void setRight(int b) {
        this.b = b;
    }

    public <A1, B1> Pair<A1, B1> map(DoubleObjFunc<A1> mapA, IntObjFunc<B1> mapB) {
        return new Pair<A1, B1>(mapA.apply(this.a), mapB.apply(this.b));
    }

    
    public Pair<Double, Integer> boxed() {
        return new Pair<Double, Integer>(a, b);
    }
    

    public <R> R match(DoubleIntObjBiFunc<R> func) {
        return func.apply(a, b);
    }

    public void match(DoubleIntBiCons func) {
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
        if (!(other instanceof DoubleIntPair)) return super.equals(other);
        // Short-circuit if both elements are tuples
        DoubleIntPair pair = (DoubleIntPair) other;
        return Utils.equals(this.a, pair.a) && Utils.equals(this.b, pair.b);
    }

    @Override
    public int hashCode() {
        // Override hash code so we get slightly faster hashing (without having to allocate an array)
        return 961 + 31 * Utils.hashCode(a) + Utils.hashCode(b);
    }
}
