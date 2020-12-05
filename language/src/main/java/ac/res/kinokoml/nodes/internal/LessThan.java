package ac.res.kinokoml.nodes.internal;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;

import ac.res.kinokoml.runtime.KinokoInteger;

public abstract class LessThan extends Infix {
    public static LessThan create() {
        return LessThanNodeGen.create();
    }
    @Specialization
    public boolean addLong(long left, long right) throws ArithmeticException{
        return left < right;
    }
    @Specialization
    @TruffleBoundary
    public boolean lessThan(KinokoInteger left, KinokoInteger right) {
        return left.toBigInteger().compareTo(right.toBigInteger()) < 0;
    }
}