package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Chris on 10/17/2016.
 */
public class Tokenizer
{


    public List<Token> getTokenStream(String source) throws Exception
    {
        try
        {
            // Represents the list of tokens to return
            List<Token> tokens = new ArrayList<Token>();

            // Lexer logic begins here
            StringBuilder tokenPatternBuffer = new StringBuilder();

            for (Token.TokenType type : Token.TokenType.values())
            {
                tokenPatternBuffer.append(String.format("|(?<%s>%s)", type.name(), type.pattern));
            }

            Pattern pattern = Pattern.compile(tokenPatternBuffer.substring(1));
            Matcher matcher = pattern.matcher(source);

            // Begin matching tokens
            while (matcher.find()) {
                if (matcher.group().matches(Token.COMMENT_PATTERN) || matcher.group().matches(Token.WHITESPACE_PATTERN)) {
                    continue;
                } else if (matcher.group(Token.TokenType.NAME.name()) != null) {
                    tokens.add(new Token(Token.TokenType.NAME, matcher.group(Token.TokenType.NAME.name())));
                    continue;
                } else if (matcher.group(Token.TokenType.STRING.name()) != null) {
                    tokens.add(new Token(Token.TokenType.STRING, matcher.group(Token.TokenType.STRING.name())));
                    continue;
                } else if (matcher.group(Token.TokenType.DATA.name()) != null) {
                    if (matcher.group().contains(" ") || matcher.group().contains("\t") || matcher.group().contains("\n")
                            || matcher.group().contains("\r")) {
                        String[] chunks = matcher.group().split("\\s+");
                        for (int i = 0; i < chunks.length; i++) {
                            if (chunks[i].isEmpty()) {
                                continue;
                            } else {
                                tokens.add(new Token(Token.TokenType.DATA, chunks[i]));
                            }
                        }
                        continue;
                    } else {
                        tokens.add(new Token(Token.TokenType.DATA, matcher.group(Token.TokenType.DATA.name())));
                        continue;
                    }
                } else if (matcher.group(Token.TokenType.OPEN_TAG.name()) != null) {
                    if (matcher.group().equals("</")) {
                        tokens.add(new Token(Token.TokenType.OPEN_CLOSING_TAG, matcher.group(Token.TokenType.OPEN_TAG.name())));
                        continue;
                    } else {
                        tokens.add(new Token(Token.TokenType.OPEN_TAG, matcher.group(Token.TokenType.OPEN_TAG.name())));
                        continue;
                    }
                } else if (matcher.group(Token.TokenType.CLOSE_TAG.name()) != null) {
                    if (matcher.group().equals("/>")) {
                        tokens.add(new Token(Token.TokenType.CLOSE_EMPTY_TAG, matcher.group(Token.TokenType.CLOSE_TAG.name())));
                        continue;
                    } else {
                        tokens.add(new Token(Token.TokenType.CLOSE_TAG, matcher.group(Token.TokenType.CLOSE_TAG.name())));
                        continue;
                    }
                } else if (matcher.group(Token.TokenType.ASSIGN.name()) != null) {
                    tokens.add(new Token(Token.TokenType.ASSIGN, matcher.group(Token.TokenType.ASSIGN.name())));
                    continue;
                } else if (matcher.group(Token.TokenType.EOF.name()) != null) {
                    tokens.add(new Token(Token.TokenType.EOF, matcher.group(Token.TokenType.EOF.name())));
                    break;
                } else {

                    throw new Exception("Fatal error while scanning...");
                }
            }
            return tokens;
        }
        catch (Exception ex){
            System.out.println(ex);
            throw ex;
        }
    }
}
