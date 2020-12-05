package ac.res.kinokoml.runtime;

import java.math.BigInteger;

import ac.res.kinokoml.KinokoMLLanguage;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public class KinokoInteger implements TruffleObject {
    private final BigInteger value;
    public KinokoInteger(BigInteger value) {
        this.value = value;
    }
    public KinokoInteger(long value) {
        this.value = BigInteger.valueOf(value);
    }
    public BigInteger toBigInteger() {
        return value;
    }
    @ExportMessage
    boolean isNumber() {
        return fitsInLong();
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInByte() {
        return value.bitLength() < 8;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInShort() {
        return value.bitLength() < 16;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInFloat() {
        return value.bitLength() < 23;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInLong() {
        return value.bitLength() < 64;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInInt() {
        return value.bitLength() < 32;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInDouble() {
        return value.bitLength() < 53;
    }

    @ExportMessage
    @TruffleBoundary
    double asDouble() throws UnsupportedMessageException {
        if (fitsInDouble())
            return value.doubleValue();
            throw UnsupportedMessageException.create();
    }

    @ExportMessage
    @TruffleBoundary
    long asLong() throws UnsupportedMessageException {
        if (fitsInLong())
            return value.longValue();
            throw UnsupportedMessageException.create();
    }

    @ExportMessage
    @TruffleBoundary
    byte asByte() throws UnsupportedMessageException {
        if (fitsInByte())
            return value.byteValue();
            throw UnsupportedMessageException.create();
    }

    @ExportMessage
    @TruffleBoundary
    int asInt() throws UnsupportedMessageException {
        if (fitsInInt())
            return value.intValue();
            throw UnsupportedMessageException.create();
    }

    @ExportMessage
    @TruffleBoundary
    float asFloat() throws UnsupportedMessageException {
        if (fitsInFloat())
            return value.floatValue();
            throw UnsupportedMessageException.create();
    }

    @ExportMessage
    @TruffleBoundary
    short asShort() throws UnsupportedMessageException {
        if (fitsInShort())
            return value.shortValue();
            throw UnsupportedMessageException.create();
    }

    @ExportMessage
    boolean hasLanguage() {
        return true;
    }

    @ExportMessage
    Class<? extends TruffleLanguage<?>> getLanguage() {
        return KinokoMLLanguage.class;
    }

    // @ExportMessage
    // boolean hasMetaObject() {
    //     return true;
    // }

    // @ExportMessage
    // Object getMetaObject() {
    //     return SLType.NUMBER;
    // }

    @ExportMessage
    @TruffleBoundary
    Object toDisplayString(@SuppressWarnings("unused") boolean allowSideEffects) {
        return value.toString();
    }
    
}