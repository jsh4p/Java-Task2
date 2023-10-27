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
    public static Double calcExpression(final String expression, LinkedList<VariableToken> vars) {
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
                Double value = ((VariableToken) postfixForm.at(i)).value();

                if (((VariableToken) postfixForm.at(i)).isInverted()) {
                    value *= -1;
                }

                values.push(value);
            } else if (postfixForm.at(i) instanceof FunctionToken) {
                Double param = calcExpression(((FunctionToken) postfixForm.at(i)).param(), vars);
                Double value = 1.;

                switch(((FunctionToken) postfixForm.at(i)).function()) {
                    case FunctionType.SIN -> value = Math.sin(param);
                    case FunctionType.COS -> value = Math.cos(param);
                    case FunctionType.TAN -> value = Math.tan(param);
                    case FunctionType.ATAN -> value = Math.atan(param);
                    case FunctionType.LOG -> value = Math.log(param);
                    case FunctionType.LOG10 -> value = Math.log10(param);
                    case FunctionType.ABS -> value = Math.abs(param);
                    case FunctionType.EXP -> value = Math.exp(param);
                }

                if (((FunctionToken) postfixForm.at(i)).isInverted()) {
                    value *= -1;
                }

                values.push(value);
            }
        }

        return values.top();
    }
}
