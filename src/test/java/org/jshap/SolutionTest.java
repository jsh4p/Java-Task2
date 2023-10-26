package org.jshap;

import org.jshap.containers.LinkedList;
import org.jshap.tokens.Lexer;
import org.jshap.tokens.Token;
import org.jshap.tokens.VariableToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {
    /**
     * Тест метода проверки правильности арифметического выражения
     */
    @Test
    void isProperlyArrangedTest() {
        LinkedList<VariableToken> vars = new LinkedList<>();

        String equation1 = "( 2 * ) 3 ";
        LinkedList<Token> tokens1 = Lexer.getTokens(equation1,vars);

        assertFalse(Solution.isProperlyArranged(equation1, tokens1));

        String equation2 = ") (";
        LinkedList<Token> tokens2 = Lexer.getTokens(equation2,vars);

        assertFalse(Solution.isProperlyArranged(equation2, tokens2));

        String equation3 = "( ( ( ) )";
        LinkedList<Token> tokens3 = Lexer.getTokens(equation3,vars);

        assertFalse(Solution.isProperlyArranged(equation3, tokens3));

        String equation4 = "- 4 2";
        LinkedList<Token> tokens4 = Lexer.getTokens(equation4,vars);

        assertFalse(Solution.isProperlyArranged(equation4, tokens4));

        String equation5 = "2 4";
        LinkedList<Token> tokens5 = Lexer.getTokens(equation5,vars);

        assertFalse(Solution.isProperlyArranged(equation5, tokens5));

        String equation6 = "( 1 - 2 ) 2";
        LinkedList<Token> tokens6 = Lexer.getTokens(equation6,vars);

        assertFalse(Solution.isProperlyArranged(equation6, tokens6));

        String equation7 = "+ / -";
        LinkedList<Token> tokens7 = Lexer.getTokens(equation7,vars);

        assertFalse(Solution.isProperlyArranged(equation7, tokens7));

        String equation8 = "1 + 2 * log(x)";
        LinkedList<Token> tokens8 = Lexer.getTokens(equation8,vars);

        assertTrue(Solution.isProperlyArranged(equation8, tokens8));
    }

    /**
     * Тест метода конвертации выражения в постфиксную форму
     */
    @Test
    void toPostfixFormTest() {
        String equation = "-5.3 * 14 + 1";
        LinkedList<VariableToken> vars = new LinkedList<>();
        LinkedList<Token> postfixForm = Solution.toPostfixForm(equation, vars);

        assertEquals("[ NumberToken[value=-5.3] -> NumberToken[value=14.0] ->" +
                " BinaryOperationToken[operation=MULTIPLY] -> NumberToken[value=1.0] ->" +
                " BinaryOperationToken[operation=PLUS] ]",
                Solution.toPostfixForm(equation, vars).toString());
    }

    /**
     * Тест проброса ошибки о неправильности выражения при конвертации в постфиксную форму
     */
    @Test
    void toPostfixFormExceptionTest() {
        String equation = "( 13.  / ) 9";
        LinkedList<VariableToken> vars = new LinkedList<>();

        assertThrows(RuntimeException.class, () ->{
            Solution.toPostfixForm(equation, vars);
        });
    }

    /**
     * Тест метода подсчёта арифметического выражения
     */
    @Test
    void calcEquationTest() {
        Double value = Math.exp(4) + 3. / 14;
        String equation = "exp(2 * y) + 3. / 14";
        LinkedList<VariableToken> vars = new LinkedList<>();
        vars.pushBack(new VariableToken("y", 2., false));


        assertEquals(value, Solution.calcEquation(equation, vars));
    }
}