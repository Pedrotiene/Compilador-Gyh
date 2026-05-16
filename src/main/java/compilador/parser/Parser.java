package compilador.parser;

import compilador.lexer.Token;
import compilador.lexer.TokenType;

import java.util.List;

public class Parser {

    private List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        programa();
        consume(TokenType.EOF, "Esperado fim do arquivo");
    }

    // ===============================
    // Helpers
    // ===============================

    private Token peek() {
        return tokens.get(pos);
    }

    private Token advance() {
        return tokens.get(pos++);
    }

    private boolean match(TokenType type) {
        if (peek().getType() == type) {
            advance();
            return true;
        }
        return false;
    }

    private void consume(TokenType type, String msg) {
        if (peek().getType() == type) {
            advance();
        } else {
            throw error(msg);
        }
    }

    private RuntimeException error(String msg) {
        return new RuntimeException(
                "Erro sintático na linha " + peek().getLine() + ": " + msg
        );
    }

    // ===============================
    // Programa
    // ===============================

    private void programa() {
        consume(TokenType.DELIM, "Esperado ':'");
        consume(TokenType.PC_DEC, "Esperado DEC");

        listaDeclaracoes();

        consume(TokenType.DELIM, "Esperado ':'");
        consume(TokenType.PC_PROG, "Esperado PROG");

        listaComandos();
    }

    // ===============================
    // Declarações
    // ===============================

    private void listaDeclaracoes() {

        do {
            declaracao();
        } while (peek().getType() == TokenType.VAR);
    }

    private void declaracao() {
        consume(TokenType.VAR, "Esperado variável");
        consume(TokenType.DELIM, "Esperado ':'");
        tipoVar();
    }

    private void tipoVar() {
        if (!match(TokenType.PC_INT) && !match(TokenType.PC_REAL)) {
            throw error("Esperado INT ou REAL");
        }
    }

    // ===============================
    // Comandos
    // ===============================

    private void listaComandos() {

        do {
            comando();
        } while (isInicioComando());
    }

    private boolean isInicioComando() {
        TokenType t = peek().getType();

        return t == TokenType.VAR ||
                t == TokenType.PC_LER ||
                t == TokenType.PC_IMPRIMIR ||
                t == TokenType.PC_SE ||
                t == TokenType.PC_ENQTO ||
                t == TokenType.PC_INI;
    }

    private void comando() {
        TokenType t = peek().getType();

        switch (t) {
            case VAR -> comandoAtribuicao();
            case PC_LER -> comandoEntrada();
            case PC_IMPRIMIR -> comandoSaida();
            case PC_SE -> comandoCondicao();
            case PC_ENQTO -> comandoRepeticao();
            case PC_INI -> subAlgoritmo();
            default -> throw error("Comando inválido");
        }
    }

    // ===============================
    // Tipos de comando
    // ===============================

    private void comandoAtribuicao() {
        consume(TokenType.VAR, "Esperado variável");
        consume(TokenType.ATRIB, "Esperado ':='");
        expressaoAritmetica();
    }

    private void comandoEntrada() {
        consume(TokenType.PC_LER, "Esperado LER");
        consume(TokenType.VAR, "Esperado variável");
    }

    private void comandoSaida() {
        consume(TokenType.PC_IMPRIMIR, "Esperado IMPRIMIR");

        if (match(TokenType.VAR) || match(TokenType.CADEIA)) {
            return;
        }

        throw error("Esperado variável ou cadeia");
    }

    private void comandoCondicao() {
        consume(TokenType.PC_SE, "Esperado SE");

        expressaoRelacional();

        consume(TokenType.PC_ENTAO, "Esperado ENTAO");

        comando();

        if (match(TokenType.PC_SENAO)) {
            comando();
        }
    }

    private void comandoRepeticao() {
        consume(TokenType.PC_ENQTO, "Esperado ENQTO");

        expressaoRelacional();

        comando();
    }

    private void subAlgoritmo() {
        consume(TokenType.PC_INI, "Esperado INI");

        listaComandos();

        consume(TokenType.PC_FIM, "Esperado FIM");
    }

    // ===============================
    // Expressões Aritméticas
    // ===============================

    private void expressaoAritmetica() {
        termoAritmetico();

        while (peek().getType() == TokenType.OP_SOMA ||
                peek().getType() == TokenType.OP_SUB) {
            advance();
            termoAritmetico();
        }
    }

    private void termoAritmetico() {
        fatorAritmetico();

        while (peek().getType() == TokenType.OP_MULT ||
                peek().getType() == TokenType.OP_DIV) {
            advance();
            fatorAritmetico();
        }
    }

    private void fatorAritmetico() {
        TokenType t = peek().getType();

        switch (t) {
            case NUM_INT, NUM_REAL, VAR -> advance();

            case ABRE_PAR -> {
                advance();
                expressaoAritmetica();
                consume(TokenType.FECHA_PAR, "Esperado ')'");
            }

            default -> throw error("Fator inválido");
        }
    }

    // ===============================
    // Expressões Relacionais
    // ===============================

    private void expressaoRelacional() {
        termoRelacional();

        while (peek().getType() == TokenType.OP_E ||
                peek().getType() == TokenType.OP_OU) {
            advance();
            termoRelacional();
        }
    }

    private void termoRelacional() {
        if (match(TokenType.ABRE_PAR)) {
            expressaoRelacional();
            consume(TokenType.FECHA_PAR, "Esperado ')'");
            return;
        }

        expressaoAritmetica();

        if (isOperadorRelacional(peek().getType())) {
            advance();
        } else {
            throw error("Esperado operador relacional");
        }

        expressaoAritmetica();
    }

    private boolean isOperadorRelacional(TokenType t) {
        return t == TokenType.OP_MENOR ||
                t == TokenType.OP_MENOR_IGUAL ||
                t == TokenType.OP_MAIOR ||
                t == TokenType.OP_MAIOR_IGUAL ||
                t == TokenType.OP_IGUAL ||
                t == TokenType.OP_DIF;
    }
}