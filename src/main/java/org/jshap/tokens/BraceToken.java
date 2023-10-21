package org.jshap.tokens;

public record BraceToken (
    BraceType brace
) implements Token {
    public TokenType type() {
        return TokenType.BRACE;
    }
}
