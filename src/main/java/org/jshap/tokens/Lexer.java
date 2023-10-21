package org.jshap.tokens;

import java.util.StringTokenizer;
import org.jshap.containers.LinkedList;

public class Lexer {
    private static final String DELIMITERS = " +-*/^()";

    public static LinkedList<Token> getTokens(final String source, final LinkedList<VariableToken> var) {
        LinkedList<Token> tokens = new LinkedList<>();
        StringTokenizer tokenizer = new StringTokenizer(source, DELIMITERS, true);

        while (tokenizer.hasMoreTokens()) {
            String curToken = tokenizer.nextToken();
            if (curToken.isBlank()) {
                continue;
            }

            if (isNumber(curToken)) {
                tokens.pushBack(new NumberToken(Double.parseDouble(curToken)));
            } else if (findVar(curToken, var) != -1) { // ?
                tokens.pushBack(new NumberToken(var.at(findVar(curToken, var)).value()));
            } else {
                tokens.pushBack(makeToken(curToken));
            }
        }

        return tokens;
    }

    public static boolean isNumber(final String source) {
        for (int i = 0; i < source.length(); ++i) {
            if (!Character.isDigit(source.charAt(i)) && '.' != source.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    /*public static boolean isSimpleEquation() {

    }*/

    private static Token makeToken(final String token) {
        switch (token) {
            case"+"-> {
                return new BinaryOperationToken(OperationType.PLUS);
            }
            case"-"-> {
                return new BinaryOperationToken(OperationType.MINUS);
            }
            case"*"-> {
                return new BinaryOperationToken(OperationType.MULTIPLY);
            }
            case"/"-> {
                return new BinaryOperationToken(OperationType.DIVIDE);
            }
            case"^"-> {
                return new BinaryOperationToken(OperationType.POWER);
            }
            case"("-> {
                return new BraceToken(BraceType.OPEN_BRACKET);
            }
            case")"-> {
                return new BraceToken(BraceType.CLOSE_BRACKET);
            }
            default -> throw new RuntimeException("Unexpected token " + token);
        }
    }

    private static int findVar(String varName, final LinkedList<VariableToken> var) {
        for (int i = 0; i < var.size(); ++i) {
            if (var.at(i).name().equals(varName)) {
                return i;
            }
        }

        return -1;
    }
}
