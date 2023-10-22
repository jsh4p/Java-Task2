package org.jshap.tokens;

/**
 * Запись, в которой хранится бинарная операция
 * @param operation тип бинарной операции
 */
public record BinaryOperationToken (
    BinaryOperationType operation
) implements Token {
    @Override
    public TokenType type() {
        return TokenType.BINARY_OPERATION;
    }
}
