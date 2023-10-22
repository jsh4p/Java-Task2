package org.jshap.tokens;

/**
 * Запись, в которой хранится число
 * @param value хранимое число
 */
public record NumberToken (
    Double value
) implements Token {
    @Override
    public TokenType type() {
        return TokenType.NUMBER;
    }
}