package ac.res.kinokoml.nodes.internal;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;

import ac.res.kinokoml.runtime.KinokoInteger;

public abstract class Add extends Infix {
    public static Add create() {
        return AddNodeGen.create();
    }
    @Specialization(rewriteOn = ArithmeticException.class)
    public long addLong(long left, long right) throws ArithmeticException{
        return Math.addExact(left, right);
    }
    @Specialization
    @TruffleBoundary
    public KinokoInteger addBigInteger(KinokoInteger left, KinokoInteger right) {
        return new KinokoInteger(left.toBigInteger().add(right.toBigInteger()));
    }
}