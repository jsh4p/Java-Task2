package org.jshap;

import org.jshap.containers.*;
import org.jshap.tokens.*;

/**
 * Класс для конвертации арифметического выражения в постфиксную форму
 * @author jshap
 */
public class PostfixConvertor {
    /**
     * Метод перевода арифметического выражения из инфиксной формы в постфиксную
     * @param expression арифметическое выражение
     * @param vars список объявленных переменных
     * @return список токенов
     * @throws RuntimeException неизвестный токен
     */
    public static LinkedList<Token> toPostfixForm(final String expression, final LinkedList<VariableToken> vars) {
        LinkedList<Token> result = new LinkedList<>();
        LinkedList<Token> tokens = Lexer.getTokens(expression, vars);

        if (!ExpressionChecker.isProperlyArranged(expression, tokens)) {
            throw new RuntimeException("Equation is not properly arranged");
        }

        SimpleStack<Token> operations = new SimpleStack<>();

        for (int i = 0; i < tokens.size(); ++i) {
            switch(tokens.at(i).type()) {
                case TokenType.BINARY_OPERATION -> {
                    while (!operations.isEmpty() && getPriority(tokens.at(i)) <= getPriority(operations.top())) {
                        result.pushBack(operations.pop());
                    }

                    operations.push(tokens.at(i));
                }
                case TokenType.NUMBER,TokenType.VARIABLE,TokenType.FUNCTION -> result.pushBack(tokens.at(i));
                case TokenType.BRACE -> {
                    switch (((BraceToken) tokens.at(i)).brace()) {
                        case BraceType.OPEN_BRACKET -> operations.push(tokens.at(i));
                        case BraceType.CLOSE_BRACKET -> {
                            while (operations.top().type() != TokenType.BRACE) {
                                result.pushBack(operations.pop());
                            }

                            operations.pop();
                        }
                    }
                }
            }
        }

        while (!operations.isEmpty()) {
            result.pushBack(operations.pop());
        }

        return result;
    }

    /**
     * Метод получения приоритета бинарных операций
     * @param token токен
     * @return приоритет
     */
    private static byte getPriority(Token token) {
        if (token instanceof BinaryOperationToken) {
            return switch (((BinaryOperationToken) token).operation()) {
                case BinaryOperationType.PLUS, BinaryOperationType.MINUS -> 1;
                case BinaryOperationType.MULTIPLY, BinaryOperationType.DIVIDE, BinaryOperationType.POWER -> 2;
            };
        } else {
            return 0; // скобки
        }
    }
}
