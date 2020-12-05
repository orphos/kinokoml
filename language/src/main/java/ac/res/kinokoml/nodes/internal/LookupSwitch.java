package ac.res.kinokoml.nodes.internal;

import java.util.Arrays;
import java.util.HashSet;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;

import ac.res.kinokoml.nodes.Expression;
import ac.res.kinokoml.runtime.Identifier;

public class LookupSwitch extends Switch {
    @CompilationFinal(dimensions = 1)
    private int[] indices;

    public LookupSwitch(Expression expr, Expression[] targets, int[] indices) {
        super(expr, targets);
        this.indices = indices;
    }

    @Override
    protected int computeTargetIndex(Object value) {
        int targetIndex = Arrays.binarySearch(indices, (int) value);
        return targetIndex >= 0 ? targetIndex : targets.length - 1;
    }
}