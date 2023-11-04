package org.jshap;

import org.jshap.containers.*;
import org.jshap.tokens.*;

/**
 * Класс, занимающийся вычислением выражения
 * @author jshap
 */
public class Solution {
    /**
     * Метод для подсчета арифметического выражения
     * @param expression арифметическое выражения для подсчета
     * @param vars список объявленных переменных
     * @return результат выражения
     * @throws RuntimeException неизвестный токен, неправильная расстановка скобок
     * @throws NullPointerException стек значений пуст
     */
    public static Double calcExpression(final String expression, final LinkedList<VariableToken> vars) {
        LinkedList<Token> postfixForm = PostfixConvertor.toPostfixForm(expression, vars);
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
                values.push(((VariableToken) postfixForm.at(i)).value());
            } else if (postfixForm.at(i) instanceof FunctionToken) {
                String param = ((FunctionToken) postfixForm.at(i)).param();
                double value = 1;
                if (param.charAt(0) == '=') {
                    param = param.substring(1);
                    value = -1;
                }
                double paramVal = calcExpression(param, vars);

                switch(((FunctionToken) postfixForm.at(i)).function()) {
                    case FunctionType.SIN -> value *= Math.sin(paramVal);
                    case FunctionType.COS -> value *= Math.cos(paramVal);
                    case FunctionType.TAN -> value *= Math.tan(paramVal);
                    case FunctionType.ATAN -> value *= Math.atan(paramVal);
                    case FunctionType.LOG -> value *= Math.log(paramVal);
                    case FunctionType.LOG10 -> value *= Math.log10(paramVal);
                    case FunctionType.ABS -> value *= Math.abs(paramVal);
                    case FunctionType.EXP -> value *= Math.exp(paramVal);
                }

                values.push(value);
            }
        }

        return values.top();
    }
}
