package org.jshap.tokens;

/**
 * Запись, в которой хранится функция
 * @param function тип функции
 * @param param параметр функции
 * @param isInverted стоит ли минус перед функцией
 */
public record FunctionToken(
    FunctionType function,
    String param,
    boolean isInverted
) implements Token {
    @Override
    public TokenType type() {
        return TokenType.FUNCTION;
    }
}
