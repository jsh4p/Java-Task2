package org.jshap.tokens;

public record VariableToken (
    String name,
    Double value
) implements Token {
    @Override
    public TokenType type() {
        return TokenType.VARIABLE;
    }
}
