package org.jshap;

import org.jshap.containers.*;
import org.jshap.tokens.*;

// получше декомпозировать, наверное

/**
 * Класс, состоящий из методов, необходимых для решения задачи
 * @author jshap
 */
public class Solution {
    /**
     * Метод для подсчета арифметического выражения
     * @param equation арифметическое выражения для подсчета
     * @param vars список объявленных переменных
     * @return результат выражения
     * @throws RuntimeException неизвестный токен, неправильная расстановка скобок
     * @throws NullPointerException стек значений пуст
     */
    public static Double calcEquation(final String equation, LinkedList<VariableToken> vars) {
        LinkedList<Token> postfixForm = toPostfixForm(equation, vars);
        SimpleStack<Double> values = new SimpleStack<>();

        for (int i = 0; i < postfixForm.size(); ++i) {
            if (postfixForm.at(i) instanceof NumberToken) {
               values.push(((NumberToken) postfixForm.at(i)).value());
            } else if (postfixForm.at(i) instanceof BinaryOperationToken) {
                Double right = values.pop();
                Double left = values.pop();

                switch (((BinaryOperationToken) postfixForm.at(i)).operation()) {
                    case BinaryOperationType.PLUS -> values.push(left + right);
                    case BinaryOperationType.MINUS -> values.push(left - right);
                    case BinaryOperationType.MULTIPLY -> values.push(left * right);
                    case BinaryOperationType.DIVIDE -> values.push(left / right);
                    case BinaryOperationType.POWER -> values.push(Math.pow(left, right));
                }
            } else if (postfixForm.at(i) instanceof VariableToken) {
                Double value = ((VariableToken) postfixForm.at(i)).value();

                if (((VariableToken) postfixForm.at(i)).isInverted()) {
                    value *= -1;
                }

                values.push(value);
            } else if (postfixForm.at(i) instanceof FunctionToken) {
                Double param = calcEquation(((FunctionToken) postfixForm.at(i)).param(), vars);
                Double value = 1.;

                switch(((FunctionToken) postfixForm.at(i)).function()) {
                    case FunctionType.SIN -> value = Math.sin(param);
                    case FunctionType.COS -> value = Math.cos(param);
                    case FunctionType.TAN -> value = Math.tan(param);
                    case FunctionType.ATAN -> value = Math.atan(param);
                    case FunctionType.LOG -> value = Math.log(param);
                    case FunctionType.LOG10 -> value = Math.log10(param);
                    case FunctionType.ABS -> value = Math.abs(param);
                    case FunctionType.EXP -> value = Math.exp(param);
                }

                if (((FunctionToken) postfixForm.at(i)).isInverted()) {
                    value *= -1;
                }

                values.push(value);
            }
        }

        return values.top();
    }

    /**
     * Метод перевода арифметического выражения в постфиксную форму
     * @param equation арифметическое выражение
     * @param vars список объявленных переменных
     * @return список токенов
     * @throws RuntimeException неизвестный токен
     */
    public static LinkedList<Token> toPostfixForm(final String equation, final LinkedList<VariableToken> vars) {
        LinkedList<Token> result = new LinkedList<>();
        LinkedList<Token> tokens = Lexer.getTokens(equation, vars);

        if (!isProperlyArranged(equation, tokens)) {
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

    /**
     * Метод проверки правильности введённого выражения
     * @param equation арифметическое выражение
     * @return булевое значение
     */
    public static boolean isProperlyArranged(final String equation, final LinkedList<Token> tokens) {
        SimpleStack<Character> stack = new SimpleStack<>();

        for (Character ch : equation.toCharArray()) {
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
            }
        }

        return true;
    }
}
