package compilador.commands;

import java.util.List;

public class CommandCondicao extends Command {
    private String condition;
    private List<Command> trueList;  // Comandos do bloco ENTAO
    private List<Command> falseList; // Comandos do bloco SENAO (pode ser nulo ou vazio)

    public CommandCondicao(String condition, List<Command> trueList, List<Command> falseList) {
        this.condition = condition;
        this.trueList = trueList;
        this.falseList = falseList;
    }

    @Override
    public String generateCode() {
        StringBuilder str = new StringBuilder();
        str.append("if (").append(condition).append(") {\n");

        // Adiciona os comandos do bloco verdadeiro (ENTAO)
        for (Command cmd : trueList) {
            str.append("        ").append(cmd.generateCode()).append("\n");
        }
        str.append("    }");

        // Se houver comandos para o bloco falso (SENAO), gera a seção else
        if (falseList != null && !falseList.isEmpty()) {
            str.append(" else {\n");
            for (Command cmd : falseList) {
                str.append("        ").append(cmd.generateCode()).append("\n");
            }
            str.append("    }");
        }

        return str.toString();
    }

    @Override
    public String toString() {
        return "CommandCondicao [condition=" + condition + ", trueList=" + trueList + ", falseList=" + falseList + "]";
    }
}