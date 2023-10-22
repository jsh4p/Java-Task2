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
            } else if (curToken.contains("[") && isFun(curToken.substring(0, curToken.indexOf('[')))) { //Пуш функций
                String funName = curToken.substring(0, curToken.indexOf('['));
                boolean isInverted = false;

                if ('+' == curToken.charAt(0) || '-' == curToken.charAt(0)) {
                    funName = curToken.substring(1, curToken.indexOf('['));
                    isInverted = true;
                }

                String param = curToken.substring(curToken.indexOf('[') + 1);
                int amountOpen = 1;
                int amountClose = 0;

                for (int i = 0; i < param.length(); ++i) {
                    if (param.charAt(i) == '[') {
                        ++amountOpen;
                    } else if (param.charAt(i) == ']') {
                        ++amountClose;
                    }
                }

                while (amountOpen != amountClose) {
                    curToken = tokenizer.nextToken();

                    for (int i = 0; i < curToken.length(); ++i) {
                        if (curToken.charAt(i) == '[') {
                            ++amountOpen;
                        } else if (curToken.charAt(i) == ']') {
                            ++amountClose;
                        }
                    }

                    param += curToken;
                }

                param = param.substring(0,param.length() - 1);

                tokens.pushBack(makeToken(funName, param, isInverted));
            } else { // Пуш скобок
                tokens.pushBack(makeToken(curToken, "", false));
            }
        }

        return tokens;
    }

    /**
     * Метод проверки, является ли заданная строка числом
     * @param stringToCheck строка для проверки
     * @return булевое значение
     */
    public static boolean isNumber(final String stringToCheck) {
        for (int i = 0; i < stringToCheck.length(); ++i) {
            if (i == 0 && ('+' == stringToCheck.charAt(0) || '-' == stringToCheck.charAt(0))) {
                if (stringToCheck.length() == 1) {
                    return false;
                }

                continue;
            }

            if (!Character.isDigit(stringToCheck.charAt(i)) && '.' != stringToCheck.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Метод для генерации Function и BinaryOperation токенов
     * @param token токен в строком представлении
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

    /**
     * Метод поиска заданной в выражении переменной в списке объявленных переменных
     * @param varName переменная, которая ищется в списке
     * @param vars список переменных
     * @return позиция переменной в списке, в случае необнаружения возвращает -1
     */
    public static int findVar(final String varName, final LinkedList<VariableToken> vars) {
        for (int i = 0; i < vars.size(); ++i) {
            if (i == 0 && ('+' == varName.charAt(0) || '-' == varName.charAt(0))) {
                if (varName.length() == 1) {
                    return -1;
                }

                if (vars.at(i).name().equals(varName.substring(1))) {
                    return i;
                }

                continue;
            }

            if (vars.at(i).name().equals(varName)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Метод поиска функции в качестве реализованной
     * @param funName функция, которая ищется
     * @return булевое значение
     */
    public static boolean isFun(final String funName) {
        String fun = "";

        if ('+' == funName.charAt(0) || '-' == funName.charAt(0)) {
            fun = funName.substring(1);
        } else {
            fun = funName;
        }

        switch(fun) {
            case"sin","cos","tan","atan","log","log10",
                    "abs","exp" -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}