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


public class LongObjPair<Obj1__> extends Tuple implements LongObjMap.Entry<Obj1__> {

    public long a;
    public Obj1__ b;

    public LongObjPair(long a, Obj1__ b) {
        this.a = a;
        this.b = b;
    }

    public long getLeft() {
        return a;
    }

    public Obj1__ getRight() {
        return b;
    }

    public void setLeft(long a) {
        this.a = a;
    }

    public void setRight(Obj1__ b) {
        this.b = b;
    }

    public <A1, B1> Pair<A1, B1> map(LongObjFunc<A1> mapA, Func<Obj1__, B1> mapB) {
        return new Pair<A1, B1>(mapA.apply(this.a), mapB.apply(this.b));
    }

    
    public Pair<Long, Obj1__> boxed() {
        return new Pair<Long, Obj1__>(a, b);
    }
    

    public <R> R match(LongObjObjBiFunc<Obj1__, R> func) {
        return func.apply(a, b);
    }

    public void match(LongObjBiCons<Obj1__> func) {
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
    public Obj1__ getValue() {
        return b;
    }

    @Override
    public Obj1__ setValue(Obj1__ b) {
        Obj1__ old = this.b;
        this.b = b;
        return old;
    }

    @Override
    public Object[] toArray() {
        return new Object[] {a, b};
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof LongObjPair)) return super.equals(other);
        // Short-circuit if both elements are tuples
        LongObjPair<Obj1__> pair = (LongObjPair<Obj1__>) other;
        return Utils.equals(this.a, pair.a) && Utils.equals(this.b, pair.b);
    }

    @Override
    public int hashCode() {
        // Override hash code so we get slightly faster hashing (without having to allocate an array)
        return 961 + 31 * Utils.hashCode(a) + Utils.hashCode(b);
    }
}
