package compilador.commands;

public class CommandAtribuicao extends Command {
    private String variable;
    private String expression;

    public CommandAtribuicao(String variable, String expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public String generateCode() {
        // Traduz para o formato padrão de atribuição em C: x = expressao;
        return variable + " = " + expression + ";";
    }

    @Override
    public String toString() {
        return "CommandAtribuicao [variable=" + variable + ", expression=" + expression + "]";
    }
}