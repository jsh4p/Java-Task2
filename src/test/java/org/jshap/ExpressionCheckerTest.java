package org.jshap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jshap.containers.LinkedList;
import org.jshap.tokens.Token;
import org.jshap.tokens.VariableToken;

class ExpressionCheckerTest {
    /**
     * Тест метода проверки правильности арифметического выражения
     */
    @Test
    void isProperlyArrangedTest() {
        LinkedList<VariableToken> vars = new LinkedList<>();

        String expression1 = "( 2 * ) 3 ";
        LinkedList<Token> tokens1 = Lexer.getTokens(expression1,vars);

        assertFalse(ExpressionChecker.isProperlyArranged(expression1, tokens1));

        String expression2 = ") (";
        LinkedList<Token> tokens2 = Lexer.getTokens(expression2,vars);

        assertFalse(ExpressionChecker.isProperlyArranged(expression2, tokens2));

        String expression3 = "( ( ( ) )";
        LinkedList<Token> tokens3 = Lexer.getTokens(expression3,vars);

        assertFalse(ExpressionChecker.isProperlyArranged(expression3, tokens3));

        String expression4 = "- 4 2";
        LinkedList<Token> tokens4 = Lexer.getTokens(expression4,vars);

        assertFalse(ExpressionChecker.isProperlyArranged(expression4, tokens4));

        String expression5 = "2 4";
        LinkedList<Token> tokens5 = Lexer.getTokens(expression5,vars);

        assertFalse(ExpressionChecker.isProperlyArranged(expression5, tokens5));

        String expression6 = "( 1 - 2 ) 2";
        LinkedList<Token> tokens6 = Lexer.getTokens(expression6,vars);

        assertFalse(ExpressionChecker.isProperlyArranged(expression6, tokens6));

        String expression7 = "+ / -";
        LinkedList<Token> tokens7 = Lexer.getTokens(expression7,vars);

        assertFalse(ExpressionChecker.isProperlyArranged(expression7, tokens7));

        String expression8 = "1 + 2 * log(x)";
        LinkedList<Token> tokens8 = Lexer.getTokens(expression8,vars);

        assertTrue(ExpressionChecker.isProperlyArranged(expression8, tokens8));
    }
}