package ac.res.kinokoml.nodes;

import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;

import ac.res.kinokoml.nodes.internal.CloneRecord;
import ac.res.kinokoml.runtime.Identifier;
import ac.res.kinokoml.runtime.KinokoRecord;

public class With extends NewRecord {
    @Child private Expression source;
    @Child private CloneRecord cloner = CloneRecord.create();

    public With(Expression source, String[] labels, Expression[] values) {
        super(labels, values);
        this.source = source;
    }

    @Override
    public void prologue(VirtualFrame frame) {
        setState(frame, cloner.executeClone(frame, (KinokoRecord)source.execute(frame)));
    }
}