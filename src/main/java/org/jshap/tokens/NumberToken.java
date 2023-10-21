package org.jshap.tokens;

public record NumberToken (
    Double value
) implements Token {
    @Override
    public TokenType type() {
        return TokenType.NUMBER;
    }
}