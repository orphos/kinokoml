package ac.res.kinokoml.nodes;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

import ac.res.kinokoml.runtime.Identifier;

@NodeField(name = "identifier", type = Identifier.class)
public abstract class WriteSlot extends KinokoMLNode {
    public static WriteSlot create(Identifier identifier) { return WriteSlotNodeGen.create(identifier); }

    protected FrameSlot getSlot(VirtualFrame frame) {
        return slot = getSlot(frame, getIdentifier(), slot);
    }

    public abstract Identifier getIdentifier();

    @CompilationFinal
    private FrameSlot slot = null;

    public abstract void executeWrite(VirtualFrame frame, Object value);

    @Specialization
    public void writeLong(VirtualFrame frame, long value) {
        frame.setLong(getSlot(frame), value);
    }
    @Specialization(replaces = { "writeLong" })
    public void write(VirtualFrame frame, Object value) {
        frame.setObject(getSlot(frame), value);
    }
}