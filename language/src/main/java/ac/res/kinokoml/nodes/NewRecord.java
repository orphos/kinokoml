package ac.res.kinokoml.nodes;

import java.util.Arrays;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;

import ac.res.kinokoml.nodes.internal.WriteRecordField;
import ac.res.kinokoml.runtime.Identifier;
import ac.res.kinokoml.runtime.KinokoRecord;

public class NewRecord extends SeqExpression {
    @CompilationFinal(dimensions = 1)
    private final String[] labels;
    @Children
    private final WriteRecordField[] writers;

    public String[] getLabels() {
        return labels;
    }

    public NewRecord(String[] labels, Expression[] fields) {
        super(fields);
        this.labels = labels;
        writers = Arrays.asList(labels).stream().map(WriteRecordField::create).toArray(WriteRecordField[]::new);
    }

    @Override
    protected void prologue(VirtualFrame frame) {
        setState(frame, getLanguage().newRecordObject());
    }

    @Override
    protected Object handleResult(VirtualFrame frame, int index, Object result) {
        writers[index].execute(frame, this.<KinokoRecord>getState(frame), result);
        return result;
    }

    @Override
    protected Object epilogue(VirtualFrame frame, Object result) {
        return getState(frame);
    }
}