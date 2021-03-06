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


public class IntIntPair extends Tuple implements IntIntMap.Entry {

    public int a;
    public int b;

    public IntIntPair(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getLeft() {
        return a;
    }

    public int getRight() {
        return b;
    }

    public void setLeft(int a) {
        this.a = a;
    }

    public void setRight(int b) {
        this.b = b;
    }

    public <A1, B1> Pair<A1, B1> map(IntObjFunc<A1> mapA, IntObjFunc<B1> mapB) {
        return new Pair<A1, B1>(mapA.apply(this.a), mapB.apply(this.b));
    }

    
    public Pair<Integer, Integer> boxed() {
        return new Pair<Integer, Integer>(a, b);
    }
    

    public <R> R match(IntIntObjBiFunc<R> func) {
        return func.apply(a, b);
    }

    public void match(IntIntBiCons func) {
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
        if (!(other instanceof IntIntPair)) return super.equals(other);
        // Short-circuit if both elements are tuples
        IntIntPair pair = (IntIntPair) other;
        return Utils.equals(this.a, pair.a) && Utils.equals(this.b, pair.b);
    }

    @Override
    public int hashCode() {
        // Override hash code so we get slightly faster hashing (without having to allocate an array)
        return 961 + 31 * Utils.hashCode(a) + Utils.hashCode(b);
    }
}
