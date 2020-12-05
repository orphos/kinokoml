package ac.res.kinokoml.nodes;

import java.util.HashSet;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import ac.res.kinokoml.runtime.Identifier;

@NodeInfo(shortName = "boolean")
public class BooleanExpression extends Expression {
    private final boolean value;

    public BooleanExpression(boolean value) {
        this.value = value;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return value;
    }

    @Override
    public HashSet<Identifier> getReferencedVarIds() {
        return new HashSet<>();
    }
}