package org.jshap;

import java.util.Scanner;
import org.jshap.containers.LinkedList;
import org.jshap.tokens.VariableToken;

public class Main {
    public static LinkedList<VariableToken> setVar() {
        LinkedList<VariableToken> var = new LinkedList<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String name = scanner.next();
            if(".".equals(name)) {
                return var;
            }
            Double value = scanner.nextDouble();

            var.pushBack(new VariableToken(name, value));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите арифметическое выражение: ");
        String eq = scanner.nextLine();

        System.out.println("Введите переменные в формате name value: ");
        LinkedList<VariableToken> var = setVar();

        System.out.printf(Solution.calcEquation(eq, var).toString());
    }
}