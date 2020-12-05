package ac.res.kinokoml.nodes;

import static org.junit.Assert.assertNotNull;

import ac.res.kinokoml.KinokoMLLanguage;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.TruffleLanguage.ContextReference;
import com.oracle.truffle.api.TruffleLanguage.LanguageReference;
import com.oracle.truffle.api.dsl.ReportPolymorphism;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;

import ac.res.kinokoml.runtime.KinokoContext;
import ac.res.kinokoml.runtime.KinokoRuntimeTypes;

@TypeSystemReference(KinokoRuntimeTypes.class)
@ReportPolymorphism
public abstract class KinokoMLNode extends Node {
    @CompilationFinal
    private FrameSlot indexSlot = null;
    @CompilationFinal
    private FrameSlot stateSlot = null;
    private final Object stateKey = new Object();

    @CompilationFinal
    private ContextReference<KinokoContext> contextRef = null;

    @CompilationFinal
    private LanguageReference<KinokoMLLanguage> langRef = null;

    protected FrameSlot getSlot(Object identifier, FrameSlot cached) {
        if (cached != null)
            return cached;
        CompilerDirectives.transferToInterpreterAndInvalidate();
        final FrameDescriptor fd = getRootNode().getFrameDescriptor();
        return fd.findOrAddFrameSlot(identifier);
    }
    protected FrameSlot getSlot(VirtualFrame frame, Object identifier, FrameSlot cached) {
        if (cached != null)
            return cached;
        CompilerDirectives.transferToInterpreterAndInvalidate();
        final FrameDescriptor fd = frame.getFrameDescriptor();
        return fd.findOrAddFrameSlot(identifier);
    }

    protected FrameSlot getIndexSlot() {
        indexSlot = getSlot(this, indexSlot);
        return indexSlot;
    }

    protected RuntimeException newPanic() {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return new RuntimeException("panic");
    }

    protected int getIndex(VirtualFrame frame) {
        try {
            return frame.getInt(getIndexSlot());
        } catch (FrameSlotTypeException e) {
            throw newPanic();
        }
    }

    protected void setIndex(VirtualFrame frame, int i) {
        frame.setInt(getIndexSlot(), i);
    }

    protected FrameSlot getStateSlot() {
        stateSlot = getSlot(stateKey, stateSlot);
        return stateSlot;
    }

    protected <State> State getState(VirtualFrame frame) {
        try {
            @SuppressWarnings("unchecked")
            State state = (State) frame.getObject(getStateSlot());
            return state;
        } catch (FrameSlotTypeException e) {
            throw newPanic();
        }
    }

    protected <State> void setState(VirtualFrame frame, State state) {
        frame.setObject(getStateSlot(), state);
    }

    protected KinokoContext getContext() {
        if (contextRef != null)
            return contextRef.get();
        CompilerDirectives.transferToInterpreterAndInvalidate();
        contextRef = lookupContextReference(KinokoMLLanguage.class);
        return getContext();
    }
    protected KinokoMLLanguage getLanguage() {
        if (langRef != null)
            return langRef.get();
        CompilerDirectives.transferToInterpreterAndInvalidate();
        langRef = lookupLanguageReference(KinokoMLLanguage.class);
        return getLanguage();
    }

    
}