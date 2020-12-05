package ac.res.kinokoml.runtime;

import com.oracle.truffle.api.dsl.TypeSystem;

import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

@TypeSystem({ long.class })
public class KinokoRuntimeTypes {
    @ImplicitCast
    @TruffleBoundary
    public static KinokoInteger asInteger(long value) {
        return new KinokoInteger(value);
    }
}