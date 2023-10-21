package org.jshap;

import org.jshap.containers.LinkedList;
import org.jshap.containers.SimpleStack;
import org.jshap.tokens.*;

public class Solution {
    private static LinkedList<Token> convertToPostfixForm(final String eq, final LinkedList<VariableToken> var) {
        LinkedList<Token> result = new LinkedList<>();
        LinkedList<Token> tokens = Lexer.getTokens(eq, var);
        SimpleStack<Token> operations = new SimpleStack<>();

        for (int i = 0; i < tokens.size(); ++i) {
            switch(tokens.at(i).type()) {
                case BINARY_OPERATION -> {
                    while (!operations.isEmpty() && getPriority(tokens.at(i)) <= getPriority(operations.top())) {
                        result.pushBack(operations.pop());
                    }
                    operations.push(tokens.at(i));
                }
                case NUMBER -> {
                    result.pushBack(tokens.at(i));
                }
                case BRACE -> {
                    switch (((BraceToken) tokens.at(i)).brace()) {
                        case BraceType.OPEN_BRACKET -> {
                            operations.push(tokens.at(i));
                        }
                        case BraceType.CLOSE_BRACKET -> {
                            while (operations.top().type() != TokenType.BRACE) { // throw NullPointer?
                                result.pushBack(operations.pop());
                            }

                            operations.pop();
                        }
                        default -> throw new RuntimeException("Unexpected token " + tokens.at(i));
                    }
                }
                default -> throw new RuntimeException("Unexpected token " + tokens.at(i));
            }
        }

        while (!operations.isEmpty()) {
            result.pushBack(operations.pop());
        }

        return result;
    }

    private static byte getPriority(Token token) {
        if (token instanceof BinaryOperationToken) {
            return switch (((BinaryOperationToken) token).operation()) {
                case OperationType.PLUS, OperationType.MINUS -> 1;
                case OperationType.MULTIPLY, OperationType.DIVIDE, OperationType.POWER -> 2;
            };
        } else {
            return 0;
        }
    }

    public static Double calcEquation(final String eq, LinkedList<VariableToken> var) {
        LinkedList<Token> postfixForm = convertToPostfixForm(eq, var);
        SimpleStack<Double> values = new SimpleStack<>();

        for (int i = 0; i < postfixForm.size(); ++i) {
            if (postfixForm.at(i) instanceof NumberToken) {
               values.push(((NumberToken) postfixForm.at(i)).value());
            } else if (postfixForm.at(i) instanceof BinaryOperationToken) {
                Double right = values.pop();
                Double left = values.pop();

                switch (((BinaryOperationToken) postfixForm.at(i)).operation()) {
                    case PLUS -> {
                        values.push(left + right);
                    }
                    case MINUS -> {
                        values.push(left - right);
                    }
                    case MULTIPLY -> {
                        values.push(left * right);
                    }
                    case DIVIDE -> {
                        values.push(left / right);
                    }
                    case POWER -> {
                        values.push(Math.pow(left, right));
                    }
                    default -> throw new RuntimeException("Unexpected token " + postfixForm.at(i));
                }
            }
        }

        return values.top();
    }

    public boolean checkAccuracy() {


        return true;
    }
}
