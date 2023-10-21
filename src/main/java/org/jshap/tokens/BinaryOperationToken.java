package org.jshap.tokens;

public record BinaryOperationToken (
    OperationType operation
) implements Token {
    @Override
    public TokenType type() {
        return TokenType.BINARY_OPERATION;
    }
}
