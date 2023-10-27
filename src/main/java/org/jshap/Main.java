package org.jshap;

import java.util.Scanner; // Для ввода с клавиатуры
import org.jshap.containers.LinkedList;
import org.jshap.tokens.VariableToken;
import static org.jshap.Solution.calcEquation;

public class Main {
    /**
     * Метод для ввода переменных с клавиатуры
     * @return список из объявленных переменных
     */
    public static LinkedList<VariableToken> setVars() {
        LinkedList<VariableToken> vars = new LinkedList<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String name = scanner.next();
            if(".".equals(name)) {
                scanner.close();
                return vars;
            }
            String value = scanner.next();

            vars.pushBack(new VariableToken(name, Double.parseDouble(value), false));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите арифметическое выражение: "); // e.g sin(2 * x) + 2 ^ 5 - 1
        String equation = scanner.nextLine();

        System.out.println("Введите переменные (до точки) в формате name value: "); // e.g x 3
        LinkedList<VariableToken> vars = setVars();

        System.out.printf("Ответ: " + calcEquation(equation, vars).toString()); // ~30.7
    }
}