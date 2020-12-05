package ac.res.kinokoml.parser;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import ac.res.kinokoml.nodes.*;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import ac.res.kinokoml.KinokoMLParser.AddPrecedenceExpressionContext;
import ac.res.kinokoml.KinokoMLParser.AliasTypeDeclBodyContext;
import ac.res.kinokoml.KinokoMLParser.ApplicationExpressionContext;
import ac.res.kinokoml.KinokoMLParser.BooleanExpressionContext;
import ac.res.kinokoml.KinokoMLParser.CompilationUnitContext;
import ac.res.kinokoml.KinokoMLParser.ConsExpressionContext;
import ac.res.kinokoml.KinokoMLParser.DotExpressionContext;
import ac.res.kinokoml.KinokoMLParser.EqPrecedenceExpressionContext;
import ac.res.kinokoml.KinokoMLParser.ExpressionContext;
import ac.res.kinokoml.KinokoMLParser.FloatExpressionContext;
import ac.res.kinokoml.KinokoMLParser.FnExpressionContext;
import ac.res.kinokoml.KinokoMLParser.IdentifierExpressionContext;
import ac.res.kinokoml.KinokoMLParser.IfExpressionContext;
import ac.res.kinokoml.KinokoMLParser.IntegerExpressionContext;
import ac.res.kinokoml.KinokoMLParser.LetExpressionContext;
import ac.res.kinokoml.KinokoMLParser.LogicalExpressionContext;
import ac.res.kinokoml.KinokoMLParser.MulPrecedenceExpressionContext;
import ac.res.kinokoml.KinokoMLParser.ParenExpressionContext;
import ac.res.kinokoml.KinokoMLParser.RecordExpressionContext;
import ac.res.kinokoml.KinokoMLParser.SingleExpressionContext;
import ac.res.kinokoml.KinokoMLParser.StringExpressionContext;
import ac.res.kinokoml.KinokoMLParser.TupleExpressionContext;
import ac.res.kinokoml.KinokoMLParser.TypeDeclContext;
import ac.res.kinokoml.KinokoMLParser.TypeDeclExpressionContext;
import ac.res.kinokoml.KinokoMLParser.TypeExpressionContext;
import ac.res.kinokoml.KinokoMLParser.UnitExpressionContext;
import ac.res.kinokoml.KinokoMLParser.VaritnaTypeDeclBodyContext;
import ac.res.kinokoml.KinokoMLParserVisitor;
import ac.res.kinokoml.nodes.internal.Infix;
import ac.res.kinokoml.runtime.Identifier;

public class NodeBuildingVisitor extends AbstractParseTreeVisitor<KinokoMLNode>
        implements KinokoMLParserVisitor<KinokoMLNode> {

    private final LinkedList<HashMap<String, Identifier>> scopes;

    public NodeBuildingVisitor() {
        scopes = new LinkedList<>();
        scopes.push(new HashMap<>());
    }

    private HashMap<String, Identifier> enterScope() {
        HashMap<String, Identifier> current = scopes.getFirst();
        scopes.push(new HashMap<>());
        return current;
    }

    private void leaveScope() {
        scopes.pop();
    }

    private Identifier searchLocal(String name) {
        return scopes.getFirst().get(name);
    }

    private Identifier search(String name) {
        for (HashMap<String, Identifier> scope : scopes) {
            Identifier ret = scope.get(name);
            if (ret != null)
                return ret;
        }
        return null;
    }

    private void put(Identifier id) {
        scopes.getFirst().put(id.getName(), id);
    }

    public RuntimeException newPanic() {
        return new RuntimeException();
    }

    @Override
    public KinokoMLNode visitApplicationExpression(ApplicationExpressionContext ctx) {
        return new ApplyExpression((Expression) visit(ctx.left), (Expression) visit(ctx.right));
    }

    @Override
    public If visitIfExpression(IfExpressionContext ctx) {
        return new If((Expression) visit(ctx.cond), (Expression) visit(ctx.body), (Expression) visit(ctx.fallback));
    }

    @Override
    public KinokoMLNode visitDotExpression(DotExpressionContext ctx) {
        return ReadRecordField.create((Expression)visit(ctx.operand), ctx.label.getText());
    }

    @Override
    public KinokoMLNode visitFnExpression(FnExpressionContext ctx) {
        enterScope();
        Identifier argId = new Identifier(ctx.Identifier().getText());
        put(argId);
        Expression body = (Expression) visit(ctx.body);

        FnExpression ret = new FnExpression(argId, body);
        leaveScope();
        return ret;
    }

    @Override
    public Expression visitEqPrecedenceExpression(EqPrecedenceExpressionContext ctx) {
        return createInfixExpression(ctx.left, ctx.op.getText(), ctx.right);
    }

    @Override
    public Expression visitSingleExpression(SingleExpressionContext ctx) {
        return (Expression) (visit(ctx.simpleExpression()));
    }

    @Override
    public KinokoMLNode visitTupleExpression(TupleExpressionContext ctx) {
        // TODO Auto-generated method stub
        throw newPanic();
    }

    @Override
    public Expression visitMulPrecedenceExpression(MulPrecedenceExpressionContext ctx) {
        return createInfixExpression(ctx.left, ctx.op.getText(), ctx.right);
    }

    @Override
    public Expression visitLogicalExpression(LogicalExpressionContext ctx) {
        return createInfixExpression(ctx.left, ctx.op.getText(), ctx.right);
    }

    @Override
    public Expression visitAddPrecedenceExpression(AddPrecedenceExpressionContext ctx) {
        return createInfixExpression(ctx.left, ctx.op.getText(), ctx.right);
    }

    @Override
    public Expression visitConsExpression(ConsExpressionContext ctx) {
        return createInfixExpression(ctx.left, ctx.op.getText(), ctx.right);
    }

    @Override
    public KinokoMLNode visitRecordExpression(RecordExpressionContext ctx) {
        String[] labels = ctx.labels.stream().map(Token::getText).toArray(String[]::new);
        Expression[] values = ctx.values.stream().map(exp -> (Expression)visit(exp)).toArray(Expression[]::new);
        if (ctx.source == null)
            return new NewRecord(labels, values);
        return new With((Expression)visit(ctx.source), labels, values);
    }

    @Override
    public KinokoMLNode visitLetExpression(LetExpressionContext ctx) {
        Identifier id = new Identifier(ctx.binder.getText());
        boolean rec = ctx.REC() != null;
        boolean shadowing = searchLocal(id.getName()) != null;
        Expression bindant = null;
        if (!rec)
            bindant = (Expression) visit(ctx.bindant);
        if (shadowing)
            enterScope();
        put(id);
        if (rec)
            bindant = (Expression) visit(ctx.bindant);
        LetExpression ret = new LetExpression(ctx.REC() != null, id, bindant, (Expression) visit(ctx.body));
        if (shadowing)
            leaveScope();
        return ret;
    }

    @Override
    public KinokoMLNode visitTypeDeclExpression(TypeDeclExpressionContext ctx) {
        // TODO Auto-generated method stub
        throw newPanic();
    }

    @Override
    public KinokoMLNode visitIdentifierExpression(IdentifierExpressionContext ctx) {
        String name = ctx.Identifier().getText();
        Identifier id = searchLocal(name);
        boolean local = id != null;
        if (!local)
            id = search(name);
        return new IdentifierExpression(id, local);
    }

    @Override
    public IntegerLiteral visitIntegerExpression(IntegerExpressionContext ctx) {
        String text = ctx.Integer().getText();
        BigInteger value;
        if (text.startsWith("0x"))
            value = new BigInteger(text.substring(2), 16);
        else if (text.startsWith("0b"))
            value = new BigInteger(text.substring(2), 2);
        else if (text.startsWith("0"))
            value = new BigInteger(text, 8);
        else
            value = new BigInteger(text);
        return new IntegerLiteral(value);
    }

    @Override
    public KinokoMLNode visitFloatExpression(FloatExpressionContext ctx) {
        // TODO Auto-generated method stub
        throw newPanic();
    }

    @Override
    public StringExpression visitStringExpression(StringExpressionContext ctx) {
        String text = ctx.StringLiteral().getText();
        return new StringExpression(text.substring(1, text.length() - 1));
    }

    @Override
    public BooleanExpression visitBooleanExpression(BooleanExpressionContext ctx) {
        return new BooleanExpression(ctx.BooleanLiteral().getText().equals("true") ? true : false);
    }

    @Override
    public Expression visitParenExpression(ParenExpressionContext ctx) {
        return (Expression) visit(ctx.expression());
    }

    @Override
    public KinokoMLNode visitUnitExpression(UnitExpressionContext ctx) {
        // TODO Auto-generated method stub
        throw newPanic();
    }

    @Override
    public KinokoMLNode visitTypeExpression(TypeExpressionContext ctx) {
        // TODO Auto-generated method stub
        throw newPanic();
    }

    @Override
    public KinokoMLNode visitAliasTypeDeclBody(AliasTypeDeclBodyContext ctx) {
        // TODO Auto-generated method stub
        throw newPanic();
    }

    @Override
    public KinokoMLNode visitVaritnaTypeDeclBody(VaritnaTypeDeclBodyContext ctx) {
        // TODO Auto-generated method stub
        throw newPanic();
    }

    @Override
    public KinokoMLNode visitTypeDecl(TypeDeclContext ctx) {
        // TODO Auto-generated method stub
        throw newPanic();
    }

    @Override
    public Expression visitCompilationUnit(CompilationUnitContext ctx) {
        return (Expression) visit(ctx.expression());
    }

    public InfixExpression createInfixExpression(ExpressionContext left, String op, ExpressionContext right) {
        return new InfixExpression((Expression) visit(left), Infix.create(op), (Expression) visit(right));
    }

}