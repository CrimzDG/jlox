package lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Scanner(String source) {
	this.source = source;
    }

    List<Token> scanTokens() {
	while (!isAtEnd()) {
	    //At start of next lexeme
	    start = current;
	    scanToken();
	}

	tokens.add(new Token(TokenType.EOF, "", null, line));
	return tokens;
    }

    private void scanToken() {
	char c = advance();
	switch (c) {
	case '(': addToken(TokenType.LEFT_PAREN); break;
	case ')': addToken(TokenType.RIGHT_PAREN); break;
	case '{': addToken(TokenType.LEFT_BRACE); break;
	case '}': addToken(TokenType.RIGHT_BRACE); break;
	case ',': addToken(TokenType.COMMA); break;
	case '.': addToken(TokenType.DOT); break;
	case '-': addToken(TokenType.MINUS); break;
	case '+': addToken(TokenType.PLUS); break;
	case ';': addToken(TokenType.SEMICOLON); break;
	case '*': addToken(TokenType.STAR); break;
	    
	case '!':
	    addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
	    break;
	case '=':
	    addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
	    break;
	case '<':
	    addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
	    break;
	case '>':
	    addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
	    break;
	case '/':
	    if (match('/')) {
		//Eat until we reach EOF or newline, we dont care about comments
		while (peek() != '\n' && !isAtEnd()) advance();
	    }
	    
	    else {
		addToken(TokenType.SLASH);
	    }
	    break;

	case ' ':
	case '\r':
	case '\t':
	    break;
	    
	case '\n':
	    line++;
	    break;

	case '"': string(); break;

	default:
	    if (isDigit(c)) {
		number();
	    }
	    
	    else {
		Lox.error(line, "Unexpected character.");
	    }
	    break;
	}
    }

    private void number() {
	while (isDigit(peek())) advance();

	//We find a valid decimal point
	if (peek() == '.' && isDigit(peekNext())) {
	    //Eat the .
	    advance();

	    while (isDigit(peek())) advance();
	}

	addToken(TokenType.NUMBER)
    }

    private void string() {
	while (peek() != '"' && !isAtEnd()) {
	    if (peek() == '\n') line++;
	    advance();
	}

	if(isAtEnd()) {
	    Lox.error(line, "Unterminated string.");
	    return;
	}

	advance(); //Eats last "

	String value = source.substring(start + 1, current - 1); //Get rid of "
	addToken(TokenType.STRING, value);
    }

    private boolean match(char expected) {
	if (isAtEnd()) return false;
	if (source.charAt(current) != expected) return false;

	current ++;
	return true;
    }

    private char peek() {
	if (isAtEnd()) return '\0';
	return source.charAt(current);
    }

    private boolean isDigit(char c) {
	return c >= '0' && c <= '9';
    }

    private boolean isAtEnd() {
	return current >= source.length();
    }

    private char advance() {
	return source.charAt(current++);
    }

    private void addToken(TokenType type) {
	addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
	String text = source.substring(start, current);
	tokens.add(new Token(type, text, literal, line));
    }
}
