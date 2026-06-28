package compilador.commands;

import java.util.ArrayList;

public class CommandCondicao extends Command {
    private String condition;
    private ArrayList<Command> condTrue;
    private ArrayList<Command> condFalse;

    public CommandCondicao(String condition, ArrayList<Command> condTrue, ArrayList<Command> condFalse) {
        this.condition = condition;
        this.condTrue = condTrue;
        this.condFalse = condFalse;
    }

    @Override
    public String generateCCode() {
        StringBuilder str = new StringBuilder();

        // Aqui precisei tomar cuidado com os blocos aninhados. Abro o 'if' do C
        // com a condição relacional avaliada lá na gramática do ANTLR.
        str.append("if (").append(condition).append(") {\n");

        // Novamente usando polimorfismo, mando cada comando do bloco 'ENTAO' se traduzir.
        for (Command cmd : condTrue) {
            str.append("        ").append(cmd.generateCCode());
        }
        str.append("    }\n");

        // Se a lista do condFalse tiver algo, significa que o usuário usou o 'SENAO'.
        // Então montamos o bloco do 'else' e repetimos o processo.
        if (condFalse != null && !condFalse.isEmpty()) {
            str.append("    else {\n");
            for (Command cmd : condFalse) {
                str.append("        ").append(cmd.generateCCode());
            }
            str.append("    }\n");
        }
        return str.toString();
    }
}
