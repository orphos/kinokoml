package ac.res.kinokoml.nodes.internal;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;

import ac.res.kinokoml.runtime.KinokoInteger;

public abstract class Subtract extends Infix {
    public static Subtract create() {
        return SubtractNodeGen.create();
    }
    @Specialization(rewriteOn = ArithmeticException.class)
    public long addLong(long left, long right) throws ArithmeticException{
        return Math.subtractExact(left, right);
    }
    @Specialization
    @TruffleBoundary
    public KinokoInteger addBigInteger(KinokoInteger left, KinokoInteger right) {
        return new KinokoInteger(left.toBigInteger().subtract(right.toBigInteger()));
    }
}