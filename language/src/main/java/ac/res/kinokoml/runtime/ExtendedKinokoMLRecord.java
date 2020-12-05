package ac.res.kinokoml.runtime;

import com.oracle.truffle.api.object.Shape;

public class ExtendedKinokoMLRecord extends KinokoRecord {
    @DynamicField private Object _obj0;
    @DynamicField private Object _obj1;
    @DynamicField private Object _obj2;
    @DynamicField private long _long0;
    @DynamicField private long _long1;
    @DynamicField private long _long2;
    public ExtendedKinokoMLRecord(Shape shape) {
        super(shape);
    }
    
}