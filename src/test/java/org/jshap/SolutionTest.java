package org.jshap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jshap.containers.LinkedList;
import org.jshap.tokens.VariableToken;

class SolutionTest {
    /**
     * Тест метода подсчёта арифметического выражения
     */
    @Test
    void calcExpressionTest() {
        Double value = Math.exp(4) + 3. / 14;
        String expression = "exp(2 * y) + 3. / 14";
        LinkedList<VariableToken> vars = new LinkedList<>();
        vars.pushBack(new VariableToken("y", 2.));

        assertEquals(value, Solution.calcExpression(expression, vars));
    }
}