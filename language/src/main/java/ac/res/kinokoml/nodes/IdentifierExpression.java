package ac.res.kinokoml.nodes;

import java.util.HashSet;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import ac.res.kinokoml.runtime.Identifier;

@NodeInfo(shortName = "id")
public class IdentifierExpression extends Expression {
    @CompilationFinal
    private Identifier id;
    @CompilationFinal
    private boolean local;
    @Child
    private ReadSlot readLocal;

    public HashSet<Identifier> getReferencedVarIds() {
        HashSet<Identifier> ret = new HashSet<>();
        if (!local)
            ret.add(id);
        return ret;
    }

    public IdentifierExpression(Identifier id, boolean local) {
        this.id = id;
        this.local = local;
        this.readLocal = ReadSlot.create(id);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return readLocal.execute(frame);
    }

}