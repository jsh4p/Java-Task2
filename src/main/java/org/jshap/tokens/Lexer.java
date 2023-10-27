package org.jshap.tokens;

import java.util.StringTokenizer;
import org.jshap.containers.LinkedList;

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
     * @param equation арифметическое выражение
     * @param vars список объявленных переменных
     * @return список токенов
     * @throws RuntimeException неизвестный токен
     */
    public static LinkedList<Token> getTokens(final String equation, final LinkedList<VariableToken> vars) {
        LinkedList<Token> tokens = new LinkedList<>();
        StringTokenizer tokenizer = new StringTokenizer(equation, DELIMITERS, true);

        while (tokenizer.hasMoreTokens()) {
            String curToken = tokenizer.nextToken();

            if (curToken.isBlank()) {
                continue;
            }

            if (isNumber(curToken)) { // Пуш чисел
                tokens.pushBack(new NumberToken(Double.parseDouble(curToken)));
            } else if (findVar(curToken, vars) != -1) { // Пуш переменных
                int ind = findVar(curToken, vars);

                if (curToken.charAt(0) == '-') {
                    tokens.pushBack(new VariableToken(vars.at(ind).name(), vars.at(ind).value(), true));
                } else {
                    tokens.pushBack(vars.at(ind));
                }
            } else if (isFun(curToken)) { //Пуш функций
                String funName = curToken.substring(0, curToken.indexOf('('));
                boolean isInverted = false;

                if ('+' == curToken.charAt(0) || '-' == curToken.charAt(0)) {
                    funName = funName.substring(1);
                    isInverted = true;
                }

                String param = getFunParam(curToken, tokenizer);

                tokens.pushBack(makeToken(funName, param, isInverted));
            } else { // Пуш скобок и бинарных операций
                tokens.pushBack(makeToken(curToken, "", false));
            }
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
     * @param varName переменная, которая ищется в списке
     * @param vars список переменных
     * @return позиция переменной в списке, в случае необнаружения возвращает -1
     */
    public static int findVar(final String varName, final LinkedList<VariableToken> vars) {
        String var = varName;

        if ('+' == varName.charAt(0) || '-' == varName.charAt(0)) {
            if (varName.length() == 1) {
                return -1;
            }

            var = varName.substring(1);
        }

        for (int i = 0; i < vars.size(); ++i) {
            if (vars.at(i).name().equals(var)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Метод поиска функции в качестве реализованной
     * @param fun функция, которая ищется
     * @return булевое значение
     */
    public static boolean isFun(final String fun) {
        if (!fun.contains("(") || fun.length() < 2) {
            return false;
        }

        String funName = fun.substring(0, fun.indexOf('('));

        if ('+' == funName.charAt(0) || '-' == funName.charAt(0)) {
            funName = fun.substring(1);
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
        String param = "";
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

            param += curToken;
            if (tokenizer.hasMoreTokens()) {
                curToken = tokenizer.nextToken();
            } else if (braceDifference != 0) {
                throw new RuntimeException("Incorrect arrangement of braces/Incorrect tokenizer");
            }
        } while (braceDifference != 0);

        param = param.substring(1, param.length() - 1);

        return param;
    }

    /**
     * Метод для генерации Function и BinaryOperation токенов
     * @param token токен в строковом представлении
     * @param param параметр функции
     * @param isInverted стоит ли минус перед функцией
     * @return токен в качестве Record'а
     * @throws RuntimeException неизвестный токен
     */
    private static Token makeToken(final String token, final String param, final boolean isInverted) {
        switch (token) {
            case"sin" -> {
                return new FunctionToken(FunctionType.SIN, param, isInverted);
            }
            case"cos" -> {
                return new FunctionToken(FunctionType.COS, param, isInverted);
            }
            case"tan" -> {
                return new FunctionToken(FunctionType.TAN, param, isInverted);
            }
            case"atan" -> {
                return new FunctionToken(FunctionType.ATAN, param, isInverted);
            }
            case"log" -> {
                return new FunctionToken(FunctionType.LOG, param, isInverted);
            }
            case"log10" -> {
                return new FunctionToken(FunctionType.LOG10, param, isInverted);
            }
            case"abs" -> {
                return new FunctionToken(FunctionType.ABS, param, isInverted);
            }
            case"exp" -> {
                return new FunctionToken(FunctionType.EXP, param, isInverted);
            }
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
