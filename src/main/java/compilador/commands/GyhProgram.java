package compilador.commands;

import java.io.FileWriter;
import java.util.ArrayList;

public class GyhProgram {
    private SymbolTable varTable;
    private ArrayList<Command> comandos;

    public void generatetarget() {
        // optamos por usar um StringBuilder aqui por questões de performance
        // ao invés de concatenar strings diretamente.
        StringBuilder str = new StringBuilder();

        // Primeiro, geramos o cabeçalho padrão do C para garantir que as funções de I/O funcionem.
        str.append("#include <stdio.h>\n");
        str.append("#include <stdlib.h>\n\n");
        str.append("int main() {\n");

        // Aqui nós varremos a Tabela de Símbolos que foi preenchida durante a análise semântica.
        // Já mapeamos os tipos da nossa linguagem para os tipos nativos do C.
        if (varTable != null) {
            for (Symbol symbol : varTable.getAll()) {
                if (symbol.getType() == 1) {
                    str.append("    int ").append(symbol.getName()).append(";\n");
                } else {
                    str.append("    float ").append(symbol.getName()).append(";\n");
                }
            }
        }
        str.append("\n");

        // Agora iteramos sobre a lista de comandos que o parser do ANTLR agrupou.
        // A mágica acontece aqui: usamos polimorfismo. Nós delegamos para cada nó
        // da nossa AST a responsabilidade de traduzir a si mesmo para C.
        if (comandos != null) {
            for (Command cmd : comandos) {
                str.append("    ").append(cmd.generateCCode());
            }
        }

        str.append("\n    return 0;\n");
        str.append("}\n");

        // Por fim, pegamos todo esse buffer que montamos em memória e persistimos
        // em um arquivo programa.c físico para podermos compilar com o GCC.
        try {
            FileWriter fw = new FileWriter("programa.c");
            fw.write(str.toString());
            fw.close();
            System.out.println("Sucesso: Arquivo programa.c gerado para compilação!");
        } catch (Exception ex) {
            System.out.println("Erro ao gerar arquivo C: " + ex.getMessage());
        }
    }

    public void setVarTable(SymbolTable varTable) { this.varTable = varTable; }
    public void setCommand(ArrayList<Command> comandos) { this.comandos = comandos; }
}