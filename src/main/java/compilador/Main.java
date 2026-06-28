package compilador;


import compilador.antlr.GyhLangLexer;
import compilador.antlr.GyhLangParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Substitua pelo caminho do seu arquivo de teste
        String arquivoTeste = "src/input/programa_teste.gyh";

        try {
            // 1. Carrega o código fonte
            CharStream cs = CharStreams.fromFileName(arquivoTeste);

            // 2. Análise Léxica com ANTLR
            GyhLangLexer lexer = new GyhLangLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            // 3. Análise Sintática com ANTLR
            GyhLangParser parser = new GyhLangParser(tokens);

            System.out.println("Iniciando compilacao do arquivo: " + arquivoTeste);

            // 4. Inicia a análise a partir da regra principal definida no seu .g4
            parser.programa();

            System.out.println("Analise concluida com sucesso!");

            // 5. Gera o código em C
            System.out.println("Iniciando geracao de codigo...");
            parser.generateCommand();

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro durante a compilacao: " + e.getMessage());
        }
    }
}