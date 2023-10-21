package org.jshap.tokens;

public record FunctionToken(

) implements Token {
    @Override
    public TokenType type() {
        return null;
    }
}
