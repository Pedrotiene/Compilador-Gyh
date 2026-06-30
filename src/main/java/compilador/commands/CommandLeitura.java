package compilador.commands;

public class CommandLeitura extends Command {
    private String variable;
    private int type;

    public CommandLeitura(String variable, int type) {
        this.variable = variable;
        this.type = type;
    }

    @Override
    public String generateCode() {

        if (type == Simbolos.REAL) {
            return "scanf(\"%lf\", &" + variable + ");";
        } else {
            return "scanf(\"%d\", &" + variable + ");";
        }
    }
}