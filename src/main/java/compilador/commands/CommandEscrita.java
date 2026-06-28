package compilador.commands;

public class CommandEscrita extends Command {
    private String id;

    public CommandEscrita(String id) {
        this.id = id;
    }

    @Override
    public String generateCCode() {
        // Já para o 'IMPRIMIR', mapeei para o printf do C, sempre adicionando
        // o '\n' para a formatação no terminal ficar organizada na hora da execução final.
        return "printf(\"%d\\n\", " + id + ");\n";
    }
}
