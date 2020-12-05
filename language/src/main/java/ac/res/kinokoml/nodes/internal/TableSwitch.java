package ac.res.kinokoml.nodes.internal;

import java.util.HashSet;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;

import ac.res.kinokoml.nodes.Expression;
import ac.res.kinokoml.runtime.Identifier;

public class TableSwitch extends Switch {
    @CompilationFinal
    private int startIndex;

    public TableSwitch(Expression expr, Expression[] targets, int startIndex) {
        super(expr, targets);
        this.startIndex = startIndex;
    }

    @Override
    protected int computeTargetIndex(Object value) {
        int targetIndex = (int) value - startIndex;
        return 0 <= targetIndex && targetIndex < targets.length ? targetIndex : targets.length - 1;
    }
}