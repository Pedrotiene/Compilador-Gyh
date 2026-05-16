package compilador;

import compilador.lexer.Lexer;
import compilador.lexer.Token;
import compilador.parser.Parser;
import compilador.util.FileUtil;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        String source = FileUtil.load("src/input/Testes Analisadores/_Testes_Sintáticos/programa4.gyh");

        // ===== ANÁLISE LÉXICA =====
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        for (Token t : tokens) {
            System.out.println(t);
        }

        if (!lexer.getErrors().isEmpty()) {
            System.out.println("\n==== ERROS LÉXICOS ====");
            for (String e : lexer.getErrors()) {
                System.out.println(e);
            }
            return; // Para aqui se tiver erro léxico
        }

        // ===== ANÁLISE SINTÁTICA =====
        Parser parser = new Parser(tokens);

        try {
            parser.parse();
            System.out.println("\nPrograma sintaticamente correto!");
        } catch (Exception e) {
            System.out.println("\n==== ERRO SINTÁTICO ====");
            System.out.println(e.getMessage());
        }
    }
}