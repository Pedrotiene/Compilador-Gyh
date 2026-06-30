package compilador.commands;

import java.util.List;

public class CommandRepeticao extends Command {
    private String condition;
    private List<Command> commandList;

    public CommandRepeticao(String condition, List<Command> commandList) {
        this.condition = condition;
        this.commandList = commandList;
    }

    @Override
    public String generateCode() {
        StringBuilder str = new StringBuilder();

        str.append("while (").append(condition).append(") {\n");

        for (Command cmd : commandList) {
            str.append("        ").append(cmd.generateCode()).append("\n");
        }
        str.append("    }");

        return str.toString();
    }

    @Override
    public String toString() {
        return "CommandRepeticao [condition=" + condition + ", commandList=" + commandList + "]";
    }
}