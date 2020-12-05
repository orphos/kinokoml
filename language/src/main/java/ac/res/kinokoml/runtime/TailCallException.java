package ac.res.kinokoml.runtime;

import com.oracle.truffle.api.nodes.ControlFlowException;

import ac.res.kinokoml.nodes.Function;

public class TailCallException extends ControlFlowException {
    private static final long serialVersionUID = 1L;
    public final Function function;
    public final Object argument;
    public TailCallException(Function function, Object argument) {
        this.function = function;
        this.argument = argument;
    }
}