package compilador.commands;

public class CommandEscrita extends Command {
    private String content;
    private int type;

    public CommandEscrita(String content) {
        this.content = content;
        this.type = -1;
    }

    public CommandEscrita(String content, int type) {
        this.content = content;
        this.type = type;
    }

    @Override
    public String generateCode() {

        if (content.startsWith("\"")) {
            return "printf(" + content + ");";
        }

        else if (type == Simbolos.REAL) {
            return "printf(\"%lf\", " + content + ");";
        }

        else {
            return "printf(\"%d\", " + content + ");";
        }
    }

    @Override
    public String toString() {
        return "CommandEscrita [content=" + content + "]";
    }
}