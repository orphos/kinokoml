package ac.res.kinokoml.nodes.internal;

import java.util.HashSet;

import com.oracle.truffle.api.frame.VirtualFrame;

import ac.res.kinokoml.nodes.Expression;
import ac.res.kinokoml.runtime.Identifier;

public abstract class Switch extends Expression {
    @Child
    private Expression expr;
    @Children
    protected Expression[] targets;

    public Switch(Expression expr, Expression[] targets) {
        this.expr = expr;
        this.targets = targets;
    }

    protected abstract int computeTargetIndex(Object value);

    @Override
    public Object execute(VirtualFrame frame) {
        Expression target = targets[computeTargetIndex(expr.execute(frame))];
        return target.execute(frame);
    }

    @Override
    public void markTail() {
        super.markTail();
        for (Expression target : targets)
            target.markTail();
    }

    public HashSet<Identifier> getReferencedVarIds() {
        HashSet<Identifier> ret = new HashSet<>();
        ret.addAll(expr.getReferencedVarIds());
        for (Expression target : targets)
        ret.addAll(target.getReferencedVarIds());
        return ret;
    }
}