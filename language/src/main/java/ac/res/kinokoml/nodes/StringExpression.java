package ac.res.kinokoml.nodes;

import java.util.HashSet;

import com.oracle.truffle.api.frame.VirtualFrame;

import ac.res.kinokoml.runtime.Identifier;

public class StringExpression extends Expression {
    private final String value;

    public StringExpression(String value) {
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