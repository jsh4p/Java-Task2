package org.jshap.tokens;

/**
 * Запись, в которой хранится объявленная переменная
 * @param name имя переменной
 * @param value значение переменной
 * @param isInverted стоит ли минус перед переменной
 */
public record VariableToken (
    String name,
    double value,
    boolean isInverted
) implements Token {
    @Override
    public TokenType type() {
        return TokenType.VARIABLE;
    }
}
