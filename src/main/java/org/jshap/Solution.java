package org.jshap;

import org.jshap.containers.*;
import org.jshap.tokens.*;

/**
 * Класс, состоящий из методов, необходимых для решения задачи
 * @author jshap
 */
public class Solution {
    /**
     * Метод перевода арифметического выражения в постфиксную форму
     * @param equation арифметическое выражение
     * @param vars список объявленных переменных
     * @return список токенов
     * @throws RuntimeException неизвестный токен
     */
    public static LinkedList<Token> convertToPostfixForm(final String equation, final LinkedList<VariableToken> vars) {
        LinkedList<Token> result = new LinkedList<>();
        LinkedList<Token> tokens = Lexer.getTokens(equation, vars);
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
                        default -> throw new RuntimeException("Unexpected token " + tokens.at(i));
                    }
                }
                default -> throw new RuntimeException("Unexpected token " + tokens.at(i));
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

    /**
     * Метод для подсчета арифметического выражения
     * @param equation арифметическое выражения для подсчета
     * @param vars список объявленных переменных
     * @return результат выражения
     * @throws RuntimeException неизвестный токен, неправильная расстановка скобок
     * @throws NullPointerException стек значений пуст
     */
    public static Double calcEquation(final String equation, LinkedList<VariableToken> vars) {
        if (!isProperlyArranged(equation)) {
            throw new RuntimeException("Incorrect arrangement of braces");
        }

        LinkedList<Token> postfixForm = convertToPostfixForm(equation, vars);
        SimpleStack<Double> values = new SimpleStack<>();

        for (int i = 0; i < postfixForm.size(); ++i) {
            if (postfixForm.at(i) instanceof NumberToken) {
               values.push(((NumberToken) postfixForm.at(i)).value());
            } else if (postfixForm.at(i) instanceof BinaryOperationToken) {
                if (values.size() < 2) {
                    throw new NullPointerException("Empty value stack");
                }

                Double right = values.pop();
                Double left = values.pop();

                switch (((BinaryOperationToken) postfixForm.at(i)).operation()) {
                    case BinaryOperationType.PLUS -> values.push(left + right);
                    case BinaryOperationType.MINUS -> values.push(left - right);
                    case BinaryOperationType.MULTIPLY -> values.push(left * right);
                    case BinaryOperationType.DIVIDE -> values.push(left / right);
                    case BinaryOperationType.POWER -> values.push(Math.pow(left, right));
                    default -> throw new RuntimeException("Unexpected token " + postfixForm.at(i));
                }
            } else if (postfixForm.at(i) instanceof VariableToken) {
                Double value = ((VariableToken) postfixForm.at(i)).value();

                if (((VariableToken) postfixForm.at(i)).isInverted()) {
                    value *= -1;
                }

                values.push(value);
            } else if (postfixForm.at(i) instanceof FunctionToken) {
                Double param = calcEquation(((FunctionToken) postfixForm.at(i)).param(), vars);
                Double value;

                switch(((FunctionToken) postfixForm.at(i)).function()) {
                    case FunctionType.SIN -> value = Math.sin(param);
                    case FunctionType.COS -> value = Math.cos(param);
                    case FunctionType.TAN -> value = Math.tan(param);
                    case FunctionType.ATAN -> value = Math.atan(param);
                    case FunctionType.LOG -> value = Math.log(param);
                    case FunctionType.LOG10 -> value = Math.log10(param);
                    case FunctionType.ABS -> value = Math.abs(param);
                    case FunctionType.EXP -> value = Math.exp(param);
                    default -> throw new RuntimeException("Unexpected token " + postfixForm.at(i));
                }

                if (((FunctionToken) postfixForm.at(i)).isInverted()) {
                    value *= -1;
                }

                values.push(value);
            } else {
                throw new RuntimeException("Unexpected token " + postfixForm.at(i));
            }
        }

        return values.top();
    }

    /**
     * Метод проверки правильности расстановки скобок
     * @param equation арифметическое выражение
     * @return булевое значение
     */
    public static boolean isProperlyArranged(final String equation) {
        SimpleStack<Character> stack = new SimpleStack<Character>();

        for (Character ch : equation.toCharArray()) {
            switch(ch) {
                case'(' -> stack.push(')');
                case'[' -> stack.push(']');
                case')',']' -> {
                    if (stack.top() != ch || stack.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
