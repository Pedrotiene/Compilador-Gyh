package compilador.commands;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GeraCodigo {

    public static void exportar(AnalisadorSemantico semantico, String arquivoSaida) {
        StringBuilder codigoC = new StringBuilder();

        // 1. Cabeçalho padrão de um arquivo C
        codigoC.append("#include <stdio.h>\n");
        codigoC.append("#include <stdlib.h>\n\n");
        codigoC.append("int main() {\n");

        // 2. Seção de Declaração de Variáveis
        codigoC.append("    // --- Declaracao de Variaveis ---\n");

        for (Simbolos sym : semantico.getTabelaDeSimbolos().getAll()) {
            codigoC.append("    ").append(sym.generateCode()).append("\n");
        }
        codigoC.append("\n");

        // 3. Seção do Corpo do Programa (Comandos)
        codigoC.append("    // --- Corpo do Programa ---\n");
        List<Command> comandos = semantico.getListaComandos();
        for (Command cmd : comandos) {
            codigoC.append("    ").append(cmd.generateCode()).append("\n");
        }

        // 4. Rodapé padrão do C
        codigoC.append("\n    return 0;\n");
        codigoC.append("}\n");

        // 5. Grava a String gerada em um arquivo físico .c
        try (FileWriter fw = new FileWriter(arquivoSaida)) {
            fw.write(codigoC.toString());
            System.out.println("\n🎉 SUCESSO! Arquivo C gerado com sucesso em: " + arquivoSaida);
        } catch (IOException e) {
            System.err.println("Erro ao tentar gravar o arquivo de saída.");
            e.printStackTrace();
        }
    }
}