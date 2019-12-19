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


public class LongIntPair extends Tuple implements LongIntMap.Entry {

    public long a;
    public int b;

    public LongIntPair(long a, int b) {
        this.a = a;
        this.b = b;
    }

    public long getLeft() {
        return a;
    }

    public int getRight() {
        return b;
    }

    public void setLeft(long a) {
        this.a = a;
    }

    public void setRight(int b) {
        this.b = b;
    }

    public <A1, B1> Pair<A1, B1> map(LongObjFunc<A1> mapA, IntObjFunc<B1> mapB) {
        return new Pair<A1, B1>(mapA.apply(this.a), mapB.apply(this.b));
    }

    
    public Pair<Long, Integer> boxed() {
        return new Pair<Long, Integer>(a, b);
    }
    

    public <R> R match(LongIntObjBiFunc<R> func) {
        return func.apply(a, b);
    }

    public void match(LongIntBiCons func) {
        func.accept(a, b);
    }

    @Override
    public long getKey() {
        return a;
    }

    public long setKey(long a) {
        long old = this.a;
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
        if (!(other instanceof LongIntPair)) return super.equals(other);
        // Short-circuit if both elements are tuples
        LongIntPair pair = (LongIntPair) other;
        return Utils.equals(this.a, pair.a) && Utils.equals(this.b, pair.b);
    }

    @Override
    public int hashCode() {
        // Override hash code so we get slightly faster hashing (without having to allocate an array)
        return 961 + 31 * Utils.hashCode(a) + Utils.hashCode(b);
    }
}
