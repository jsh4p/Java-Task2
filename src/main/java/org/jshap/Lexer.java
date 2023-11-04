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
     * Метод поиска заданной переменной в списке объявленных
     * @param string переменная, которая ищется в списке
     * @param vars список переменных
     * @return ссылка на найденную переменную
     */
    public static VariableToken findVar(final String string, final LinkedList<VariableToken> vars) {
        String varName = string;

        if ('+' == string.charAt(0) || '-' == string.charAt(0)) {
            if (string.length() == 1) {
                return null;
            }

            varName = string.substring(1);
        }

        for (int i = 0; i < vars.size(); ++i) {
            if (vars.at(i).name().equals(varName)) {
                return vars.at(i);
            }
        }

        return null;
    }

    /**
     * Метод поиска функции в качестве реализованной
     * @param string функция, которая ищется
     * @return ссылка на тип функции
     */
    public static FunctionType findFun(final String string) {
        if (!string.contains("(") || string.length() < 2) {
            return null;
        }

        String funName = string.substring(0, string.indexOf('('));

        if ('+' == funName.charAt(0) || '-' == funName.charAt(0)) {
            funName = funName.substring(1);
        }

        switch (funName) {
            case "sin" -> {
                return FunctionType.SIN;
            }
            case "cos" -> {
                return FunctionType.COS;
            }
            case "tan" -> {
                return FunctionType.TAN;
            }
            case "atan" -> {
                return FunctionType.ATAN;
            }
            case "log" -> {
                return FunctionType.LOG;
            }
            case "log10" -> {
                return FunctionType.LOG10;
            }
            case "abs" -> {
                return FunctionType.ABS;
            }
            case "exp" -> {
                return FunctionType.EXP;
            }
            default -> {
                return null;
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

        VariableToken var;
        if ((var = findVar(token, vars)) != null) {
            int k = token.charAt(0) == '-'?-1:1;

            return new VariableToken(var.name(), k * var.value());
        }

        FunctionType fun;
        if ((fun = findFun(token)) != null) {
            String param = getFunParam(token, tokenizer);
            if ('-' == token.charAt(0)) {
                param = "=" + param;
            } // как-то обозначим, что перед функцией стоит минус.

            return new FunctionToken(fun, param);
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
