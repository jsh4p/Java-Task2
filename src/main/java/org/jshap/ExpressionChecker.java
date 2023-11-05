package org.jshap;

import org.jshap.containers.*;
import org.jshap.tokens.*;

/**
 * Класс для проверки арифметического выражения
 * @author jshap
 */
public class ExpressionChecker {
    /**
     * Метод проверки правильности введённого выражения
     * @param expression арифметическое выражение
     * @return булевое значение
     * @throws RuntimeException нереализованный токен
     */
    public static boolean isProperlyArranged(final String expression, final LinkedList<Token> tokens) {
        SimpleStack<Character> stack = new SimpleStack<>();

        for (Character ch : expression.toCharArray()) {
            switch(ch) {
                case'(' -> stack.push(')');
                case')' -> {
                    if (stack.isEmpty() || stack.top() != ch) { // проверка скобок
                        return false;
                    } else {
                        stack.pop();
                    }
                }
            }
        }

        if (!stack.isEmpty()) {
            return false;
        }

        for (int i = 0; i < tokens.size(); ++i) {
            switch (tokens.at(i).type()) {
                case TokenType.NUMBER,TokenType.VARIABLE,TokenType.FUNCTION -> {
                    // проверка случая, когда число стоит перед открытой, после закрытой скобки
                    if (i > 0 && tokens.at(i - 1).type() == TokenType.BRACE &&
                            ((BraceToken) tokens.at(i - 1)).brace() == BraceType.CLOSE_BRACKET) {
                        return false;
                    }

                    if (i < tokens.size() - 1 && tokens.at(i + 1).type() == TokenType.BRACE &&
                            ((BraceToken) tokens.at(i + 1)).brace() == BraceType.OPEN_BRACKET) {
                        return false;
                    }

                    // проверка на отсутствие операции между числами
                    if (tokens.size() > 1 && i > 0) {
                        switch (tokens.at(i - 1).type()) {
                            case TokenType.NUMBER,TokenType.VARIABLE,TokenType.FUNCTION -> {
                                return false;
                            }
                        }

                        if (i == tokens.size() - 1) {
                            continue;
                        }

                        switch (tokens.at(i + 1).type()) {
                            case TokenType.NUMBER,TokenType.VARIABLE,TokenType.FUNCTION -> {
                                return false;
                            }
                        }
                    }
                }
                case TokenType.BINARY_OPERATION -> {
                    if (i == 0 || i == tokens.size() - 1) {
                        // находится ли операции скраю?
                        return false;
                    }

                    if (tokens.at(i - 1).type() == TokenType.BINARY_OPERATION ||
                            tokens.at(i + 1).type() == TokenType.BINARY_OPERATION ||
                            tokens.at(i + 1).type() == TokenType.BRACE &&
                                    ((BraceToken) tokens.at(i + 1)).brace() == BraceType.CLOSE_BRACKET) {
                        // две операции, но нет чисел между ними, и операция перед закрывающейся скобкой
                        return false;
                    }
                }
                case TokenType.BRACE -> { }
                default -> throw new RuntimeException("Unrealized token " + tokens.at(i));
            }
        }

        return true;
    }
}
