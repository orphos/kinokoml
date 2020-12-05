package ac.res.kinokoml.nodes;

import java.util.HashSet;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;

import ac.res.kinokoml.runtime.Identifier;

public abstract class Expression extends KinokoMLNode {
    public abstract Object execute(VirtualFrame frame);

    public abstract HashSet<Identifier> getReferencedVarIds();

    @CompilationFinal
    protected boolean isTail = false;

    public void markTail() {
        if (isTail)
            return;
        CompilerDirectives.transferToInterpreterAndInvalidate();
        isTail = true;
    }
}