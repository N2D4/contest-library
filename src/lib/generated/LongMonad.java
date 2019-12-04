/* Generated by Generificator on Wed Dec 04 00:49:23 CET 2019 from Monad.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import lib.utils.tuples.*;

import lib.utils.function.Cons;
import lib.utils.function.Func;

import java.util.function.Function;


public class LongMonad extends Tuple {

    public long value;

    public LongMonad(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public <T> Monad<T> map(LongObjFunc<T> mappingFunction) {
        return new Monad<T>(mappingFunction.apply(this.value));
    }

    public <R> R match(LongObjFunc<R> func) {
        return func.apply(value);
    }

    public void match(LongCons func) {
        func.accept(value);
    }

    @Override
    public Object[] toArray() {
        return new Object[] {value};
    }
}
