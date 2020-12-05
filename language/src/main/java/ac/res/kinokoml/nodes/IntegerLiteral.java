package ac.res.kinokoml.nodes;

import java.math.BigInteger;
import java.util.HashSet;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;

import ac.res.kinokoml.runtime.Identifier;

public class IntegerLiteral extends Expression {
    @CompilationFinal
    private BigInteger value;
    @CompilationFinal
    private long longValue;
    @CompilationFinal
    private boolean isLong = false;

    public IntegerLiteral(BigInteger value) {
        this.value = value;
        try {
            longValue = value.longValueExact();
            isLong = true;
        } catch (ArithmeticException e) {
        }
    }

    @Override
    public Object execute(VirtualFrame frame) {
        if (isLong)
            return longValue;
        return value;
    }

    @Override
    public HashSet<Identifier> getReferencedVarIds() {
        return new HashSet<>();
    }
}