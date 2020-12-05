package ac.res.kinokoml.runtime;

import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.Shape;

public class KinokoRecord extends DynamicObject {

    public KinokoRecord(Shape shape) {
        super(shape);
    }

}