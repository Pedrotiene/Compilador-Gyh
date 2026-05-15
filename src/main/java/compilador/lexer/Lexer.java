package compilador.lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private String source;
    private int pos = 0;
    private int line = 1;
    private List<String> errors = new ArrayList<>();

    public Lexer(String source) {
        this.source = source;
    }

    private TokenType checkKeyword(String lexeme) {
        return switch (lexeme) {
            case "DEC" -> TokenType.PC_DEC;
            case "PROG" -> TokenType.PC_PROG;
            case "INT" -> TokenType.PC_INT;
            case "LER" -> TokenType.PC_LER;
            case "REAL" -> TokenType.PC_REAL;
            case "IMPRIMIR" -> TokenType.PC_IMPRIMIR;
            case "SE" -> TokenType.PC_SE;
            case "SENAO" -> TokenType.PC_SENAO;
            case "ENTAO" -> TokenType.PC_ENTAO;
            case "ENQTO" -> TokenType.PC_ENQTO;
            case "INI" -> TokenType.PC_INI;
            case "FIM" -> TokenType.PC_FIM;
            case "E" -> TokenType.OP_E;
            case "OU" -> TokenType.OP_OU;
            default -> null;
        };
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (!isAtEnd()) {
            char c = advance();

            switch (c) {
                case '"':
                    Token t = string();
                    if (t != null) tokens.add(t);
                    break;

                case '+':
                    tokens.add(new Token(TokenType.OP_SOMA, "+", line));
                    break;

                case '-':
                    tokens.add(new Token(TokenType.OP_SUB, "-", line));
                    break;

                case '*':
                    tokens.add(new Token(TokenType.OP_MULT, "*", line));
                    break;

                case '/':
                    tokens.add(new Token(TokenType.OP_DIV, "/", line));
                    break;

                case '(':
                    tokens.add(new Token(TokenType.ABRE_PAR, "(", line));
                    break;

                case ')':
                    tokens.add(new Token(TokenType.FECHA_PAR, ")", line));
                    break;

                case ':':
                    if (!isAtEnd() && peek() == '=') {
                        advance();
                        tokens.add(new Token(TokenType.ATRIB, ":=", line));
                    } else {
                        tokens.add(new Token(TokenType.DELIM, ":", line));
                    }
                    break;

                case '<':
                    if (!isAtEnd() && peek() == '=') {
                        advance();
                        tokens.add(new Token(TokenType.OP_MENOR_IGUAL, "<=", line));
                    } else {
                        tokens.add(new Token(TokenType.OP_MENOR, "<", line));
                    }
                    break;

                case '>':
                    if (!isAtEnd() && peek() == '=') {
                        advance();
                        tokens.add(new Token(TokenType.OP_MAIOR_IGUAL, ">=", line));
                    } else {
                        tokens.add(new Token(TokenType.OP_MAIOR, ">", line));
                    }
                    break;

                case '=':
                    if (!isAtEnd() && peek() == '=') {
                        advance();
                        tokens.add(new Token(TokenType.OP_IGUAL, "==", line));
                    } else {
                        errors.add("Erro léxico na linha " + line + ": Esperado '=='");
                    }
                    break;

                case '!':
                    if (!isAtEnd() && peek() == '=') {
                        advance();
                        tokens.add(new Token(TokenType.OP_DIF, "!=", line));
                    } else {
                        errors.add("Erro léxico na linha " + line + ": Esperado '!='");
                    }
                    break;

                case '#':
                    while (!isAtEnd() && peek() != '\n') {
                        advance();
                    }
                    break;

                case '\n':
                    line++;
                    break;

                case ' ':
                case '\t':
                case '\r':
                    break;

                default:
                    if (Character.isDigit(c)) {
                        tokens.add(number(c));
                    } else if (Character.isLetter(c)) {
                        t = identifier(c);
                        if (t != null) tokens.add(t);
                    } else {
                        errors.add("Erro léxico na linha " + line + ": Símbolo inválido: " + c);
                    }
            }
        }

        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

    private boolean isAtEnd() {
        return pos >= source.length();
    }

    private char advance() {
        return source.charAt(pos++);
    }

    private Token number(char first) {
        StringBuilder sb = new StringBuilder();
        sb.append(first);

        boolean isReal = false;

        while (!isAtEnd()) {
            char c = peek();

            if (isDigit(c)) {
                sb.append(advance());
            } else if (c == '.' && !isReal) {
                isReal = true;
                sb.append(advance());
            } else {
                break;
            }
        }

        return new Token(
                isReal ? TokenType.NUM_REAL : TokenType.NUM_INT,
                sb.toString(),
                line
        );
    }

    private Token identifier(char first) {
        StringBuilder sb = new StringBuilder();
        sb.append(first);

        while (!isAtEnd() && isLetterOrDigit(peek())) {
            sb.append(advance());
        }

        String lexeme = sb.toString();

        TokenType keyword = checkKeyword(lexeme);
        if (keyword != null) {
            return new Token(keyword, lexeme, line);
        }

        if (Character.isLowerCase(lexeme.charAt(0))) {
            return new Token(TokenType.VAR, lexeme, line);
        }

        if (Character.isUpperCase(lexeme.charAt(0))) {
            errors.add("Erro léxico na linha " + line + ": Palavra-chave inválida: " + lexeme);
        } else {
            errors.add("Erro léxico na linha " + line + ": Identificador inválido: " + lexeme);
        }
        return null;
    }

    private Token string() {
        StringBuilder sb = new StringBuilder();

        while (!isAtEnd() && peek() != '"') {
            if (peek() == '\n') line++;
            sb.append(advance());
        }

        if (isAtEnd()) {
            errors.add("Erro léxico na linha " + line + ": String não fechada");
            return null;
        }

        advance();

        return new Token(TokenType.CADEIA, sb.toString(), line);
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(pos);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isLetter(char c) {
        return Character.isLetter(c);
    }

    private boolean isLetterOrDigit(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    public List<String> getErrors() {
        return errors;
    }
}
