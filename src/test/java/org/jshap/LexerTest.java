package org.jshap;

import org.jshap.tokens.FunctionType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.StringTokenizer;
import org.jshap.tokens.VariableToken;
import org.jshap.containers.LinkedList;

class LexerTest {
    /**
     * Тест метода нахождения переменной
     */
    @Test
    void findVarTest() {
        LinkedList<VariableToken> vars = new LinkedList<>();
        vars.pushBack(new VariableToken("x", 3.));
        vars.pushBack(new VariableToken("y", -1.));

        assertEquals(vars.at(1), Lexer.findVar("y", vars));
        assertNull(Lexer.findVar("z", vars));
    }

    /**
     * Тест метода нахождения функции в списке реализованных
     */
    @Test
    void isFunTest() {
        String funName1 = "sin()";
        String funName2 = "син()";

        assertEquals(FunctionType.SIN,Lexer.findFun(funName1));
        assertNull(Lexer.findFun(funName2));
    }

    /**
     * Тест метода получения параметра функции
     */
    @Test
    void getFunParamTest() {
        String fun1 = "sin(2 * x)";
        StringTokenizer tokenizer1 = new StringTokenizer(fun1, " ", true);

        assertEquals("2 * x", Lexer.getFunParam(fun1, tokenizer1));

        String fun2 = "cos()";
        StringTokenizer tokenizer2 = new StringTokenizer(fun2, " ", true);

        assertEquals("", Lexer.getFunParam(fun2, tokenizer2));
    }

    /**
     * Тест проброса ошибки при неправильном параметре функции
     */
    @Test
    void getFunParamExceptionTest() {
        String fun1 = "-abs(abs(-1)))";
        StringTokenizer tokenizer1 = new StringTokenizer(fun1, " ", true);

        assertThrows(RuntimeException.class, () -> {
            Lexer.getFunParam(fun1, tokenizer1);
        });
    }

    /**
     * Тест метода проверки, является ли строка числом
     */
    @Test
    void isNumberTest() {
        String num1 = "6.2";

        assertTrue(Lexer.isNumber(num1));

        String num2 = "sa.12";

        assertFalse(Lexer.isNumber(num2));
    }

    /**
     * Тест метода проверки, является ли строка числом, со знаком перед числом
     */
    @Test
    void isNumberTestWithSign() {
        String num1 = "-5.8";

        assertTrue(Lexer.isNumber(num1));

        String num2 = "+2.3";

        assertTrue(Lexer.isNumber(num2));

        String num3 = "-3.9abc";

        assertFalse(Lexer.isNumber(num3));
    }

    /**
     * Тест метода получения из арифметического выражения списка токенов
     */
    @Test
    void getTokensTest() {
        String expression = "exp(x) + 3";
        LinkedList<VariableToken> vars = new LinkedList<>();

        assertEquals("[ FunctionToken[function=EXP, param=x] ->" +
                        " BinaryOperationToken[operation=PLUS] -> NumberToken[value=3.0] ]",
                Lexer.getTokens(expression, vars).toString());
    }

    /**
     * Тест проброса ошибки при неизвестном токене
     */
    @Test
    void getTokensExceptionTest() {
        String expression = "blablabla";
        LinkedList<VariableToken> vars = new LinkedList<>();

        assertThrows(RuntimeException.class, () -> {
            Lexer.getTokens(expression, vars);
        });
    }
}