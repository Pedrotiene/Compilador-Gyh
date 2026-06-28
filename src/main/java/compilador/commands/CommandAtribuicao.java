package compilador.commands;

public class CommandAtribuicao extends Command {
    private String id;
    private String expressao;

    public CommandAtribuicao(String id, String expressao) {
        this.id = id;
        this.expressao = expressao;
    }

    @Override
    public String generateCCode() {
        // A tradução da atribuição é bem direta. Nós pegamos o identificador
        // que validamos na análise léxica/sintática, usamos o operador '=' do C,
        // e concatenamos com a expressão.
        return id + " = " + expressao + ";\n";
    }
}
