package ac.res.kinokoml.nodes;

import ac.res.kinokoml.runtime.Identifier;
import ac.res.kinokoml.runtime.KinokoUnit;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.HashSet;

public class UnitExpression extends Expression {
    @Override
    public Object execute(VirtualFrame frame) {
        return KinokoUnit.INSTANCE;
    }

    @Override
    public HashSet<Identifier> getReferencedVarIds() {
        return new HashSet<>();
    }
}
