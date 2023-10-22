package org.jshap.tokens;

/**
 * Интерфейс, который будут имплементить конкретные токены
 */
public interface Token {
    /**
     * Метод, определяющий тип токена
     * @return TokenType тип токена
     */
    public TokenType type();
}