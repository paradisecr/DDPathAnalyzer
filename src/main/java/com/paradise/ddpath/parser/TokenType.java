package com.paradise.ddpath.parser;

import java.util.EnumSet;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

public enum TokenType {
	EOF,
    ERROR,
    IDENTIFIER,             //标识符
    NUMBER,
    BREAK("break"),
    CASE("case"),
    CATCH("catch"),
    CHAR("char"),
    CLASS("class"),
    CONST("const"),
    CONTINUE("continue"),
    DEFAULT("default"),
    DO("do"),
    ELSE("else"),
    ENUM("enum"),
    EXTENDS("extends"),
    FINAL("final"),
    FINALLY("finally"),
    FOR("for"),
    GOTO("goto"),
    IF("if"),
    IMPLEMENTS("implements"),
    IMPORT("import"),
    INSTANCEOF("instanceof"),
    INTERFACE("interface"),
    NATIVE("native"),
    NEW("new"),
    PACKAGE("package"),
    PRIVATE("private"),
    PROTECTED("protected"),
    PUBLIC("public"),
    RETURN("return"),
    STATIC("static"),
    STRICTFP("strictfp"),
    SUPER("super"),
    SWITCH("switch"),
    SYNCHRONIZED("synchronized"),
    THIS("this"),
    THROW("throw"),
    THROWS("throws"),
    TRANSIENT("transient"),
    TRY("try"),
    VOID("void"),
    VOLATILE("volatile"),
    WHILE("while"),
    INTLITERAL,
    LONGLITERAL,
    FLOATLITERAL,
    DOUBLELITERAL,
    CHARLITERAL,
    STRINGLITERAL,
    TRUE("true"),
    FALSE("false"),
    NULL("null"),
    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    LBRACKET("["),
    RBRACKET("]"),
    SEMI(";"), //semicolon 分号
    COMMA(","),
    DOT("."),
    ELLIPSIS("..."),
    EQ("="),
    GT(">"),
    LT("<"),
    BANG("!"),
    TILDE("~"),
    QUES("?"),
    COLON(":"),
    EQEQ("=="),
    LTEQ("<="),
    GTEQ(">="),
    BANGEQ("!="),
    AMPAMP("&&"),
    BARBAR("||"),
    PLUSPLUS("++"),
    SUBSUB("--"),
    PLUS("+"),
    SUB("-"),
    STAR("*"),
    SLASH("/"),
    AMP("&"),
    BAR("|"),
    CARET("^"),
    PERCENT("%"),
    LTLT("<<"),
    GTGT(">>"),
    GTGTGT(">>>"),
    PLUSEQ("+="),
    SUBEQ("-="),
    STAREQ("*="),
    SLASHEQ("/="),
    AMPEQ("&="),
    BAREQ("|="),
    CARETEQ("^="),
    PERCENTEQ("%="),
    LTLTEQ("<<="),
    GTGTEQ(">>="),
    GTGTGTEQ(">>>="),
    MONKEYS_AT("@"),
    CUSTOM;

    TokenType() {
        this(null);
    }
    TokenType(String typeName) {
        this.typeName = typeName;
    }
    
    public final String typeName;
    
    /*
    public static Token fromChars(char[] chars,int start, int length){
    	char[] result = new  char[length];
    	System.arraycopy(chars, 0, result, start, length);
    	String name = result.toString();
    	for(Token token : enumSet){
    		if(StringUtils.isb)
    	}
    	return null;
    }
    */
    public String getTypeName(){
    	return typeName;
    }
    
    public String toString() {
        switch (this) {
        case IDENTIFIER:
            return "token.identifier";
        case CHARLITERAL:
            return "token.character";
        case STRINGLITERAL:
            return "token.string";
        case INTLITERAL:
            return "token.integer";
        case LONGLITERAL:
            return "token.long-integer";
        case FLOATLITERAL:
            return "token.float";
        case DOUBLELITERAL:
            return "token.double";
        case ERROR:
            return "token.bad-symbol";
        case EOF:
            return "token.end-of-input";
        case NUMBER:
        	return "token.number";
        case DOT: case COMMA: case SEMI: case LPAREN: case RPAREN:
        case LBRACKET: case RBRACKET: case LBRACE: case RBRACE:
            return "'" + typeName + "'";
        default:
            return typeName;
        }
    }

    public String getKind() {
        return "Token";
    }

    public String toString(ResourceBundle bundle) {
        String s = toString();
        return s.startsWith("token.") ? bundle.getString("compiler.misc." + s) : s;
    }
}

