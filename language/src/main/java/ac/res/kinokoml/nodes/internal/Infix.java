package ac.res.kinokoml.nodes.internal;

import com.oracle.truffle.api.frame.VirtualFrame;

import ac.res.kinokoml.nodes.KinokoMLNode;

public abstract class Infix extends KinokoMLNode {
    public abstract Object execute(VirtualFrame frame, Object left, Object right);

    public static Infix create(String op) {
        switch (op) {
            case "+": return Add.create();
            case "-": return Subtract.create();
            case "==": return Eq.create();
            case "<": return LessThan.create();
            default: throw new IllegalArgumentException("Unsupported operator");
        }
    }
}