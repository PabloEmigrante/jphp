package org.develnext.jphp.core.compiler.jvm.stetament;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;
import php.runtime.common.Messages;
import org.develnext.jphp.core.compiler.jvm.JvmCompiler;
import php.runtime.exceptions.ParseException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.reflection.support.Entity;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.Token;

abstract public class StmtCompiler<T extends Entity> {

    protected final JvmCompiler compiler;
    protected T entity;

    public StmtCompiler(JvmCompiler compiler){
        this.compiler = compiler;
    }

    abstract public T compile();

    public JvmCompiler getCompiler() {
        return compiler;
    }

    protected LabelNode writeLabel(MethodNode mv, int lineNumber){
        LabelNode node = new LabelNode(new Label());
        mv.instructions.add(node);
        if (lineNumber != -1)
            mv.instructions.add(new LineNumberNode(lineNumber, node));

        return node;
    }

    protected LabelNode writeLabel(MethodNode mv){
        return writeLabel(mv, -1);
    }


    /**
     * @throws ParseException
     * @param token
     */
    protected void unexpectedToken(Token token){
        Object unexpected = token.getWord();
        if (token.getType() == TokenType.T_J_CUSTOM)
            unexpected = token.getWord();

        compiler.getEnvironment().error(
                token.toTraceInfo(compiler.getContext()),
                ErrorType.E_PARSE,
                Messages.ERR_PARSE_UNEXPECTED_X,
                unexpected
        );
    }

    protected void unexpectedToken(Token token, Object expected){
        Object unexpected = token.getWord();
        if (token.getType() == TokenType.T_J_CUSTOM)
            unexpected = token.getWord();

        compiler.getEnvironment().error(
                token.toTraceInfo(compiler.getContext()),
                ErrorType.E_PARSE,
                Messages.ERR_PARSE_UNEXPECTED_X_EXPECTED_Y,
                unexpected, expected
        );
    }
}
