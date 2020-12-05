package ac.res.kinokoml.nodes.internal;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

import ac.res.kinokoml.runtime.KinokoInteger;

public abstract class Eq extends Infix {
    public static Eq create() {
        return EqNodeGen.create();
    }
    @Specialization
    public boolean eq(long left, long right) throws ArithmeticException{
        return left == right;
    }
    @Fallback
    @TruffleBoundary
    public boolean eq(Object left, Object right) {
        return left.equals(right);
    }
}