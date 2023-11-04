package org.jshap.tokens;

/**
 * Запись, в которой хранится объявленная переменная
 * @param name имя переменной
 * @param value значение переменной
 */
public record VariableToken (
    String name,
    double value
) implements Token {
    @Override
    public TokenType type() {
        return TokenType.VARIABLE;
    }
}
