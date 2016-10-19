package com.company;

/**
 * Created by Chris on 10/17/2016.
 */
public class Token
{
    public TokenType type;
    public String value;

    public Token(TokenType type, String val)
    {
        this.type = type;
        this.value = val;
    }

    public static final String COMMENT_PATTERN = "!--.*?--";
    public static final String WHITESPACE_PATTERN = "[ \t\n\r]+";
    public static final String INITIAL_PATTERN = "[a-zA-Z]|_|:";
    public static final String OTHER_PATTERN = "(" + INITIAL_PATTERN + "|[0-9]|-|\\.)+";
    public static final String ORDINARY_PATTERN = "[^<>\"'&]";
    public static final String SPECIAL_PATTERN = "&lt;|&gt;|&quot;|&apos;|&amp;";
    public static final String REFERENCE_PATTERN = "&#[0-9]+;|&#x([0-9]|[a-fA-F])+;";
    public static final String CHAR_PATTERN = ORDINARY_PATTERN + "|" + SPECIAL_PATTERN + "|" + REFERENCE_PATTERN;

    /* The actual tokens. */
    public static final String NAME_PATTERN = "(?<!>)(" + OTHER_PATTERN + ")+";
    public static final String STRING_PATTERN = "(\"(" + CHAR_PATTERN + "|')*\")|'(" + CHAR_PATTERN + "|\")*'";
    public static final String DATA_PATTERN = "(?<=>)(" + CHAR_PATTERN + ")+(?<!=)";
    public static final String OPEN_PATTERN = "</?(?!!)"; // matches both < and </, but ignores <!
    public static final String CLOSE_PATTERN = "/?(?<!-)>"; // matches both > and />, but ignores ->
    public static final String ASSIGN_PATTERN = "(?<!=)=";
    public static final String EOF_PATTERN = "&EOF"; // a special token signifying the end of the input stream


    public enum TokenType
    {
        NAME(NAME_PATTERN),
        STRING(STRING_PATTERN),
        DATA(DATA_PATTERN),
        OPEN_TAG(OPEN_PATTERN),
        CLOSE_TAG(CLOSE_PATTERN),
        OPEN_CLOSING_TAG(null), // LTSL = </
        CLOSE_EMPTY_TAG(null), // SLGT = />
        ASSIGN(ASSIGN_PATTERN),
        COMMENT(COMMENT_PATTERN),
        EPSILON(null),
        EOF(EOF_PATTERN);

        public String pattern;

        TokenType(String pattern) {
            this.pattern = pattern;
        }
    }
}
