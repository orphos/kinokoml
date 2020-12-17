package ac.res.kinokoml.runtime;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public class KinokoUnit implements TruffleObject {
    private KinokoUnit() {}

    public static final KinokoUnit INSTANCE = new KinokoUnit();

    @ExportMessage
    boolean isNull() { return true; }

    @ExportMessage
    Object toDisplayString(@SuppressWarnings("unused") boolean allowSideEffects) {
        return "()";
    }
}
