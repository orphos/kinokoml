package ac.res.kinokoml.nodes;

import java.util.HashSet;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.object.DynamicObjectLibrary;

import ac.res.kinokoml.runtime.Identifier;
import ac.res.kinokoml.runtime.KinokoRecord;

@NodeChild("target")
@NodeField(type = String.class, name = "label")
public abstract class ReadRecordField extends Expression {
    public static ReadRecordField create(Expression target, String label) { return ReadRecordFieldNodeGen.create(target, label); }

    public abstract String getLabel();

    @Specialization(limit = "4")
    public Object doRead(KinokoRecord target, @CachedLibrary("target") DynamicObjectLibrary objectLibrary) {
        return objectLibrary.getOrDefault(target, getLabel(), 0);
    }

    public HashSet<Identifier> getReferencedVarIds() { return null; }

}