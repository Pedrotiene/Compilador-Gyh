package compilador;

import compilador.lexer.Lexer;
import compilador.lexer.Token;
import compilador.util.FileUtil;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        String source = FileUtil.load("src/input/Testes Analisadores/_Testes_Léxicos/programa0.gyh");

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
        }
    }
}
