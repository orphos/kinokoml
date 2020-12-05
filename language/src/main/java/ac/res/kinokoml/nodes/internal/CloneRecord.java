package ac.res.kinokoml.nodes.internal;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.object.DynamicObjectLibrary;

import ac.res.kinokoml.nodes.KinokoMLNode;
import ac.res.kinokoml.runtime.KinokoRecord;

public abstract class CloneRecord extends KinokoMLNode {
    public static CloneRecord create() { return CloneRecordNodeGen.create(); }

    public abstract Object executeClone(VirtualFrame frame, KinokoRecord source);

    @Specialization(limit = "4")
    public Object doClone(VirtualFrame frame, KinokoRecord source, @CachedLibrary("source") DynamicObjectLibrary objectLibrary) {
        KinokoRecord ret = getLanguage().newRecordObject();
        for (Object key : objectLibrary.getKeyArray(source)) {
            objectLibrary.put(ret, key, objectLibrary.getOrDefault(source, key, null));
        }
        return ret;
    }

    
}