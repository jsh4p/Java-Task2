package org.jshap.tokens;

/**
 * Запись, в которой хранится функция
 * @param function тип функции
 * @param param параметр функции
 */
public record FunctionToken(
    FunctionType function,
    String param
) implements Token {
    @Override
    public TokenType type() {
        return TokenType.FUNCTION;
    }
}
