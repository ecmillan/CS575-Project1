/**
 * A draft of a recursive-descent parser. Work-in-progress.
 */
package com.company;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;


import lexer.Token;
import lexer.Lexer.Token.TokenType;

/**
 * @author Ahmed
 *
 */
public class Parser {
    private ArrayList<Token> tokens;
    private Token lookahead;

    private Stack<String> NameStack;

    private int indents; // maybe add indents too?

    public void parse(List<Token> tokens) throws ParserException {
        this.tokens = new ArrayList<Token>(tokens);
        this.NameStack = new Stack<String>();
        // List<Token> clone = new ArrayList<Token>(this.tokens);
        lookahead = this.tokens.get(0);
        // this.indents = 0;

        document();

        if (!check(Token.TokenType.EPSILON)) {
            throw new ParserException("Unexpected %s symbol found", lookahead);
        } else {
            System.out.println("Document parsed successfully!");
        }
    }

    private void document() throws ParserException {
        // document ::= element EOF
        System.out.println("document --> element EOF");
        element();
        match(Token.TokenType.EOF);
    }

    private void element() throws ParserException {
        // element ::= < elementPrefix
        // indent();
        System.out.println("element --> < elementPrefix");
        match(Token.TokenType.OPEN);
        elementPrefix();
    }

    private void elementPrefix() throws ParserException {
        // elementPrefix ::= NAME attribute elementSuffix
        System.out.println("elementPrefix --> NAME attribute elementSuffix");
        Token current = this.tokens.get(0);
        //System.out.println("current elementPrefix  - " + current.getLexeme());
        this.NameStack.push(current.getLexeme());
        System.out.println("Opening tag - " + current.getLexeme());
        match(Token.TokenType.NAME);
        attribute();
        elementSuffix();
    }

    private void attribute() throws ParserException {
        // attribute ::= NAME = STRING attribute
        if (check(Token.TokenType.NAME)) {
            System.out.println("attribute --> NAME = STRING attribute");
            match(Token.TokenType.NAME);
            match(Token.TokenType.ASSIGN);
            match(Token.TokenType.STRING);
            attribute(); // recursively calls itself as long as there are more attributes to parse
        } else {
            // attribute ::= EPSILON
            System.out.println("attribute --> EPSILON");
        }
    }

    private void elementSuffix() throws ParserException {
        // elementSuffix ::= > elementOrData endTag
        if (check(Token.TokenType.CLOSE)) {
            System.out.println("elementSuffix --> > elementOrData endTag");
            match(Token.TokenType.CLOSE);
            elementOrData();
            endTag();
        } else if (check(Token.TokenType.SLGT)) {
            // elementSuffix ::= />
            System.out.println("elementSuffix --> />");
            // Since this is a Closing empty tag, auto pop the stack
            this.NameStack.pop();
            System.out.println("Closed empty tag");
            match(Token.TokenType.SLGT);
        } else {
            throw new ParserException("Snytax error: An unexpected symbol has been encountered!");
        }
    }

    private void elementOrData() throws ParserException {
        // elementOrData ::= < elementPrefix elementOrData
        if (check(Token.TokenType.OPEN)) {
            System.out.println("elementOrData --> < elementPrefix elementOrData");
            match(Token.TokenType.OPEN);
            elementPrefix();
            elementOrData();
        } else if (check(Token.TokenType.DATA)) {
            // elementOrData ::= DATA elementOrData
            System.out.println("elementOrData --> DATA elementOrData");
            match(Token.TokenType.DATA);
            elementOrData();
        } else {
            // elementOrData ::= EPSILON
            System.out.println("elementOrData --> EPSILON");
        }
    }

    private void endTag() throws ParserException {
        System.out.println("endTag --> </ NAME >");
        // 3 tokens here, so we need the middle one for name
        Token current = this.tokens.get(1);
        //System.out.println("current end = |" + current.getLexeme() + "|");
        // try to pop the string and see if they match
        String openName = this.NameStack.pop();

        if (!openName.equals(current.getLexeme())){
            System.out.println("Error - Invalid closing tag name. Expected " + openName + " but found " + current.getLexeme());
            // todo - throw error up to end program execution
            // since it is still broken and doesn't close program, just keep going for now
        }
        else {
            System.out.println("success closing " + openName);
        }
        match(Token.TokenType.LTSL);
        match(Token.TokenType.NAME);
        match(Token.TokenType.CLOSE);
    }

	/* From this point onward, we only have helper functions. */

    private void nextToken() {
        this.tokens.remove(0);

        if (this.tokens.isEmpty()) {
            lookahead = new TToken(Token.TokenType.EPSILON, "");
        } else {
            lookahead = this.tokens.get(0);
        }
    }

    private boolean check(Token.TokenType type) {
        return lookahead.getType() == type;
    }

    private void match(Token.TokenType type) throws ParserException {
        matchNoAdvance(type);

        nextToken(); // advance to the next token
    }

    private void matchNoAdvance(Token.TokenType type) throws ParserException {
        if (!check(type)) {
            throw new ParserException("Snytax error: An unexpected symbol has been encountered!");
        }
    }

    private void indent() {
        for (int i = 0; i < indents; i++) {
            System.out.print("\t");
        }
    }
}
