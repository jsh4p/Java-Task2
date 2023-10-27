package org.jshap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jshap.containers.LinkedList;
import org.jshap.tokens.VariableToken;

class PostfixConvertorTest {
    /**
     * Тест метода конвертации выражения в постфиксную форму
     */
    @Test
    void toPostfixFormTest() {
        String expression = "-5.3 * 14 + 1";
        LinkedList<VariableToken> vars = new LinkedList<>();

        assertEquals("[ NumberToken[value=-5.3] -> NumberToken[value=14.0] ->" +
                        " BinaryOperationToken[operation=MULTIPLY] -> NumberToken[value=1.0] ->" +
                        " BinaryOperationToken[operation=PLUS] ]",
                PostfixConvertor.toPostfixForm(expression, vars).toString());
    }

    /**
     * Тест проброса ошибки о неправильности выражения при конвертации в постфиксную форму
     */
    @Test
    void toPostfixFormExceptionTest() {
        String expression = "( 13.  / ) 9";
        LinkedList<VariableToken> vars = new LinkedList<>();

        assertThrows(RuntimeException.class, () ->{
            PostfixConvertor.toPostfixForm(expression, vars);
        });
    }
}