package compilador;

import compilador.antlr.GyhLangLexer;
import compilador.antlr.GyhLangParser;
import compilador.commands.AnalisadorSemantico;
import compilador.commands.GeraCodigo;
import compilador.commands.LeitorArquivos;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        /*
         * ============================================================
         * OPÇÃO 1 - Compilar um único arquivo
         * ============================================================
         */

//        String arquivoEntrada = "src/input/Testes Analisadores/_Testes_Semanticos/programa5.gyh";
//
//        String pastaOutput = "src/output/";
//        String nomeArquivoSaida = "programa5.c";
//
//        String arquivoSaida = pastaOutput + nomeArquivoSaida;
//
//        compilarArquivo(arquivoEntrada, arquivoSaida);

        /*
         * ============================================================
         * OPÇÃO 2 - Compilar todos os arquivos de uma pasta
         * ============================================================
         */

        String pastaEntrada = "src/input/Testes Analisadores/_Testes_Semanticos";
        String pastaSaida = "src/output/";

        List<String> arquivos = LeitorArquivos.listarArquivos(pastaEntrada);

        for (String arquivoEntrada : arquivos) {

            String nomeArquivo = arquivoEntrada.substring(
                    arquivoEntrada.lastIndexOf("\\") + 1);

            String arquivoSaida = pastaSaida +
                    nomeArquivo.replace(".gyh", ".c");

            compilarArquivo(arquivoEntrada, arquivoSaida);
        }
    }

    /*
     * ============================================================================
     * Comente até aqui se quiser usar arquivo por arquivo, e descomente a OPÇÃO 1
     * ============================================================================
     */

    private static void compilarArquivo(String arquivoEntrada,
                                        String arquivoSaida) {

        try {

            System.out.println("\n====================================");
            System.out.println("Compilando: " + arquivoEntrada);
            System.out.println("====================================");

            CharStream input = CharStreams.fromFileName(arquivoEntrada);

            // 1. Análise Léxica
            GyhLangLexer lexer = new GyhLangLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            // 2. Análise Sintática
            GyhLangParser parser = new GyhLangParser(tokens);
            ParseTree tree = parser.programa();

            // 3. Análise Semântica
            System.out.println("Iniciando Análise Semântica...");
            AnalisadorSemantico semantico = new AnalisadorSemantico();
            semantico.visit(tree);

            if (semantico.possuiErro()) {
                System.out.println("Compilação interrompida devido a erros semânticos.");
                return;
            }

            System.out.println("Análise Semântica concluída.");

            // 4. Geração do código C
            GeraCodigo.exportar(semantico, arquivoSaida);

            System.out.println("Arquivo gerado: " + arquivoSaida);

        } catch (IOException e) {

            System.err.println("Erro ao ler o arquivo: " + arquivoEntrada);

        } catch (Exception e) {

            System.err.println("Erro durante a compilação de: " + arquivoEntrada);
            e.printStackTrace();

        }
    }
}