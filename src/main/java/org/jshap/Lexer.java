package org.jshap;

import java.util.StringTokenizer;
import org.jshap.containers.LinkedList;
import org.jshap.tokens.*;

/**
 * Класс для работы со строкой и её разбиения на типизированные токены
 * @author jshap
 */
public class Lexer {
    /**
     * Конечная переменная, которая содержит в себе делимитеры строки
     */
    private static final String DELIMITERS = " ";

    /**
     * Метод для получения списка токенов, обёрнутый в соответствующий Record
     * @param expression арифметическое выражение
     * @param vars список объявленных переменных
     * @return список токенов
     * @throws RuntimeException неизвестный токен
     */
    public static LinkedList<Token> getTokens(final String expression, final LinkedList<VariableToken> vars) {
        LinkedList<Token> tokens = new LinkedList<>();
        StringTokenizer tokenizer = new StringTokenizer(expression, DELIMITERS, true);

        while (tokenizer.hasMoreTokens()) {
            String curToken = tokenizer.nextToken();

            if (curToken.isBlank()) {
                continue;
            }

            tokens.pushBack(makeToken(curToken, tokenizer, vars));
        }

        return tokens;
    }

    /**
     * Метод проверки, является ли заданная строка числом
     * @param string строка для проверки
     * @return булевое значение
     */
    public static boolean isNumber(final String string) {
        String str = string;

        if ('+' == str.charAt(0) || '-' == str.charAt(0)) {
            if (str.length() == 1) {
                return false;
            }

            str = string.substring(1);
        }

        if ('.' == str.charAt(0)) {
            return false;
        }

        int amountOfPoints = 0;

        for (int i = 0; i < str.length(); ++i) {
            if (!Character.isDigit(str.charAt(i)) && '.' != str.charAt(i)) {
                return false;
            }

            if ('.' == str.charAt(i)) {
                ++amountOfPoints;
            }
        }

        return amountOfPoints <= 1;
    }

    /**
     * Метод поиска заданной в выражении переменной в списке объявленных переменных
     * @param string переменная, которая ищется в списке
     * @param vars список переменных
     * @return позиция переменной в списке, в случае необнаружения возвращает -1
     */
    public static int findVar(final String string, final LinkedList<VariableToken> vars) {
        String varName = string;

        if ('+' == string.charAt(0) || '-' == string.charAt(0)) {
            if (string.length() == 1) {
                return -1;
            }

            varName = string.substring(1);
        }

        for (int i = 0; i < vars.size(); ++i) {
            if (vars.at(i).name().equals(varName)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Метод поиска функции в качестве реализованной
     * @param string функция, которая ищется
     * @return булевое значение
     */
    public static boolean isFun(final String string) {
        if (!string.contains("(") || string.length() < 2) {
            return false;
        }

        String funName = string.substring(0, string.indexOf('('));

        if ('+' == funName.charAt(0) || '-' == funName.charAt(0)) {
            funName = funName.substring(1);
        }

        switch(funName) {
            case"sin","cos","tan","atan","log","log10",
                    "abs","exp" -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * Метод вычленения параметра из функции
     * @param curToken текущий токен
     * @param tokenizer токенайзер
     * @return параметр функции
     */
    public static String getFunParam(String curToken, final StringTokenizer tokenizer) {
        StringBuilder param = new StringBuilder();
        int braceDifference = 0;

        curToken = curToken.substring(curToken.indexOf('('));
        do {
            for (int i = 0; i < curToken.length(); ++i) {
                if (curToken.charAt(i) == '(') {
                    ++braceDifference;
                } else if (curToken.charAt(i) == ')') {
                    --braceDifference;
                }
            }

            param.append(curToken);
            if (tokenizer.hasMoreTokens()) {
                curToken = tokenizer.nextToken();
            } else if (braceDifference != 0) {
                throw new RuntimeException("Incorrect arrangement of braces/Incorrect tokenizer");
            }
        } while (braceDifference != 0);

        param = new StringBuilder(param.substring(1, param.length() - 1));

        return param.toString();
    }

    /**
     * Метод для генерации токенов
     * @param token токен в строковом представлении
     * @return токен в качестве Record'а
     * @throws RuntimeException неизвестный токен
     */
    private static Token makeToken(final String token, final StringTokenizer tokenizer,
                final LinkedList<VariableToken> vars) {
        if (isNumber(token)) {
            return new NumberToken(Double.parseDouble(token));
        }

        if (findVar(token, vars) != -1) {
            int ind = findVar(token, vars);

            if (token.charAt(0) == '-') {
                return new VariableToken(vars.at(ind).name(), vars.at(ind).value(), true);
            } else {
                return vars.at(ind);
            }
        }

        if (isFun(token)) {
            boolean isInverted = false;
            String funName = token.substring(0, token.indexOf('('));

            if ('+' == funName.charAt(0) || '-' == funName.charAt(0)) {
                if ('-' == funName.charAt(0)) {
                    isInverted = true;
                }
                funName = funName.substring(1);
            }

            String param = getFunParam(token, tokenizer);

            switch (funName) {
                case "sin" -> {
                    return new FunctionToken(FunctionType.SIN, param, isInverted);
                }
                case "cos" -> {
                    return new FunctionToken(FunctionType.COS, param, isInverted);
                }
                case "tan" -> {
                    return new FunctionToken(FunctionType.TAN, param, isInverted);
                }
                case "atan" -> {
                    return new FunctionToken(FunctionType.ATAN, param, isInverted);
                }
                case "log" -> {
                    return new FunctionToken(FunctionType.LOG, param, isInverted);
                }
                case "log10" -> {
                    return new FunctionToken(FunctionType.LOG10, param, isInverted);
                }
                case "abs" -> {
                    return new FunctionToken(FunctionType.ABS, param, isInverted);
                }
                case "exp" -> {
                    return new FunctionToken(FunctionType.EXP, param, isInverted);
                }
                default -> throw new RuntimeException("Unexpected token " + token);
            }
        }

        switch (token) {
            case"+" -> {
                return new BinaryOperationToken(BinaryOperationType.PLUS);
            }
            case"-" -> {
                return new BinaryOperationToken(BinaryOperationType.MINUS);
            }
            case"*" -> {
                return new BinaryOperationToken(BinaryOperationType.MULTIPLY);
            }
            case"/" -> {
                return new BinaryOperationToken(BinaryOperationType.DIVIDE);
            }
            case"^" -> {
                return new BinaryOperationToken(BinaryOperationType.POWER);
            }
            case"(" -> {
                return new BraceToken(BraceType.OPEN_BRACKET);
            }
            case")" -> {
                return new BraceToken(BraceType.CLOSE_BRACKET);
            }
            default -> throw new RuntimeException("Unexpected token " + token);
        }
    }
}
