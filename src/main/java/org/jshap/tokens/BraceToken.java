package org.jshap.tokens;

/**
 * Запись, в которой хранится скобка
 * @param brace тип скобки
 */
public record BraceToken (
    BraceType brace
) implements Token {
    public TokenType type() {
        return TokenType.BRACE;
    }
}
