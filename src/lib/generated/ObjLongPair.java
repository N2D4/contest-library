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


public class ObjLongPair<Obj1__> extends Tuple implements ObjLongMap.Entry<Obj1__> {

    public Obj1__ a;
    public long b;

    public ObjLongPair(Obj1__ a, long b) {
        this.a = a;
        this.b = b;
    }

    public Obj1__ getLeft() {
        return a;
    }

    public long getRight() {
        return b;
    }

    public void setLeft(Obj1__ a) {
        this.a = a;
    }

    public void setRight(long b) {
        this.b = b;
    }

    public <A1, B1> Pair<A1, B1> map(Func<Obj1__, A1> mapA, LongObjFunc<B1> mapB) {
        return new Pair<A1, B1>(mapA.apply(this.a), mapB.apply(this.b));
    }

    
    public Pair<Obj1__, Long> boxed() {
        return new Pair<Obj1__, Long>(a, b);
    }
    

    public <R> R match(ObjLongObjBiFunc<Obj1__, R> func) {
        return func.apply(a, b);
    }

    public void match(ObjLongBiCons<Obj1__> func) {
        func.accept(a, b);
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
        if (!(other instanceof ObjLongPair)) return super.equals(other);
        // Short-circuit if both elements are tuples
        ObjLongPair<Obj1__> pair = (ObjLongPair<Obj1__>) other;
        return Utils.equals(this.a, pair.a) && Utils.equals(this.b, pair.b);
    }

    @Override
    public int hashCode() {
        // Override hash code so we get slightly faster hashing (without having to allocate an array)
        return 961 + 31 * Utils.hashCode(a) + Utils.hashCode(b);
    }
}
