package ac.res.kinokoml.nodes.internal;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.object.DynamicObjectLibrary;

import ac.res.kinokoml.nodes.KinokoMLNode;
import ac.res.kinokoml.runtime.Identifier;
import ac.res.kinokoml.runtime.KinokoRecord;

@NodeField(name = "label", type = String.class)
public abstract class WriteRecordField extends KinokoMLNode {
    public static WriteRecordField create(String label) { return WriteRecordFieldNodeGen.create(label); }

    public abstract void execute(VirtualFrame frame, KinokoRecord target, Object value);
    public abstract String getLabel();

    @Specialization(limit = "4")
    public void doWrite(VirtualFrame frame, KinokoRecord target, Object value, @CachedLibrary("target") DynamicObjectLibrary objectLibrary) {
        objectLibrary.put(target, getLabel(), value);
    }
    
}