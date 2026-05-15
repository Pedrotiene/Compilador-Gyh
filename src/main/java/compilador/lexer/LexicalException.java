package compilador.lexer;

public class LexicalException extends RuntimeException {
    public LexicalException(String message, int line) {
        super("Erro léxico na linha " + line + ": " + message);
    }
}
