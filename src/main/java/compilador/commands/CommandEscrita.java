package compilador.commands;

public class CommandEscrita extends Command {
    private String content;
    private int type; // Usado caso queira saber se é INT ou REAL para variáveis

    // Construtor simples para quando for uma Cadeia de texto direta ou variável genérica
    public CommandEscrita(String content) {
        this.content = content;
        this.type = -1;
    }

    // Construtor completo passando o tipo (útil se o conteúdo for uma variável cadastrada)
    public CommandEscrita(String content, int type) {
        this.content = content;
        this.type = type;
    }

    @Override
    public String generateCode() {
        // Se o conteúdo começar com aspas, é uma String direta literal
        if (content.startsWith("\"")) {
            return "printf(" + content + ");";
        }
        // Se for a variável do tipo REAL, usa %f ou %lf (double)
        else if (type == Simbolos.REAL) {
            return "printf(\"%lf\", " + content + ");";
        }
        // Caso contrário (ou se for INT), usa %d
        else {
            return "printf(\"%d\", " + content + ");";
        }
    }

    @Override
    public String toString() {
        return "CommandEscrita [content=" + content + "]";
    }
}