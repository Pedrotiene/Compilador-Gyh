package compilador.commands;

public class CommandLeitura extends Command {
    private String id;

    public CommandLeitura(String id) {
        this.id = id;
    }

    @Override
    public String generateCCode() {
        // Para o comando 'LER', eu mapeei direto para a função scanf da stdio.h.
        // Adicionei o caractere '&' no identificador porque o scanf exige o endereço de memória.
        return "scanf(\"%d\", &" + id + ");\n";
    }
}
