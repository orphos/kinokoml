package ac.res.kinokoml;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.object.Shape;
import com.oracle.truffle.api.source.Source;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import ac.res.kinokoml.nodes.Expression;
import ac.res.kinokoml.nodes.Function;
import ac.res.kinokoml.parser.NodeBuildingVisitor;
import ac.res.kinokoml.runtime.ExtendedKinokoMLRecord;
import ac.res.kinokoml.runtime.Identifier;
import ac.res.kinokoml.runtime.KinokoContext;
import ac.res.kinokoml.runtime.KinokoRecord;

@TruffleLanguage.Registration(id = KinokoMLLanguage.ID, name = "KinokoML", defaultMimeType = KinokoMLLanguage.MIME_TYPE, characterMimeTypes = KinokoMLLanguage.MIME_TYPE,
 fileTypeDetectors = KinokoMLDetector.class)
public final class KinokoMLLanguage extends TruffleLanguage<KinokoContext> {
    public static final String ID = "kinokoml";
    public static final String MIME_TYPE = "application/x-kinokoml";

    private Shape initialRecordShape;

    public KinokoMLLanguage() {
      initialRecordShape = Shape.newBuilder()/*.layout(ExtendedKinokoMLRecord.class)*/.build();
    }

    @Override
    protected KinokoContext createContext(Env env) {
        return new KinokoContext();
    }

    public KinokoRecord newRecordObject() {
        return new KinokoRecord(initialRecordShape);
    }

    protected CallTarget parse(ParsingRequest request) throws Exception {
        Source src = request.getSource();
        KinokoMLLexer lexer = new KinokoMLLexer(CharStreams.fromString(src.getCharacters().toString()));
        KinokoMLParser parser = new KinokoMLParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.compilationUnit();
        Function rootNode = new Function(null, new Identifier[0], (Expression)new NodeBuildingVisitor().visit(tree), this, Truffle.getRuntime().createMaterializedFrame(new Object[0]), true);
        return rootNode.getCallTarget();
    }
}