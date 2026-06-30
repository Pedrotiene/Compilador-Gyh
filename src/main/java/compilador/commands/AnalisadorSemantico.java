package compilador.commands;

import compilador.antlr.GyhLangBaseVisitor;
import compilador.antlr.GyhLangParser;
import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;

public class AnalisadorSemantico extends GyhLangBaseVisitor<Integer> {

    private TabelaDeSimbolos tabela = new TabelaDeSimbolos();

    private boolean possuiErro = false;

    public boolean possuiErro() {
        return possuiErro;
    }

    private void erro(String mensagem) {
        System.err.println("Erro Semântico: " + mensagem);
        possuiErro = true;
    }


    private List<Command> listaComandosGlobal = new ArrayList<>();


    private List<Command> listaComandosAtual = listaComandosGlobal;


    public List<Command> getListaComandos() {
        return listaComandosGlobal;
    }


    public TabelaDeSimbolos getTabelaDeSimbolos() {
        return tabela;
    }

    // =====================================================================
    // 1. ÁREA DE DECLARAÇÕES (DEC)
    // =====================================================================
    @Override
    public Integer visitDeclaracao(GyhLangParser.DeclaracaoContext ctx) {
        String nomeVar = ctx.Var().getText();
        String textoTipo = ctx.tipoVar().getText(); // "INT" ou "REAL"

        // Mapeia o texto do ANTLR usando a sua nova classe 'Simbolos'
        int tipoVar = textoTipo.equals("INT") ? Simbolos.INT : Simbolos.REAL;

        if (tabela.existe(nomeVar)) {
            erro("A variável '" + nomeVar + "' já foi declarada anteriormente.");
        } else {
            // Instancia a classe usando o nome atualizado: Simbolos
            tabela.adicionar(new Simbolos(nomeVar, tipoVar, null));
        }
        return null;
    }

    // =====================================================================
    // 2. COMANDO ATRIBUIÇÃO (:=)
    // =====================================================================
    @Override
    public Integer visitComandoAtribuicao(GyhLangParser.ComandoAtribuicaoContext ctx) {
        String nomeVar = ctx.Var().getText();

        if (!tabela.existe(nomeVar)) {
            erro("A variável '" + nomeVar + "' está sendo usada sem ser declarada.");
            return null;
        }

        int tipoDaVariavel = tabela.getSymbol(nomeVar).getType();
        int tipoDaExpressao = visit(ctx.expressaoAritmetica());


        if (tipoDaVariavel == Simbolos.INT && tipoDaExpressao == Simbolos.REAL) {
            erro("Tipo incompatível! Não é possível atribuir REAL para a variável inteira '" + nomeVar + "'.");
        }

        String expressaoTexto = ctx.expressaoAritmetica().getText();
        listaComandosAtual.add(new CommandAtribuicao(nomeVar, expressaoTexto));

        return null;
    }

    // =====================================================================
    // 3. COMANDO LEITURA (LER)
    // =====================================================================
    @Override
    public Integer visitComandoEntrada(GyhLangParser.ComandoEntradaContext ctx) {
        String nomeVar = ctx.Var().getText();

        if (!tabela.existe(nomeVar)) {
            erro("Variável '" + nomeVar + "' no comando LER não foi declarada.");
            return null;
        }

        int tipoVar = tabela.getSymbol(nomeVar).getType();
        listaComandosAtual.add(new CommandLeitura(nomeVar, tipoVar));
        return null;
    }

    // =====================================================================
    // 4. COMANDO ESCRITA (IMPRIMIR)
    // =====================================================================
    @Override
    public Integer visitComandoSaida(GyhLangParser.ComandoSaidaContext ctx) {
        if (ctx.Var() != null) {
            String nomeVar = ctx.Var().getText();
            if (!tabela.existe(nomeVar)) {
                erro("Variável '" + nomeVar + "' no IMPRIMIR não foi declarada.");
                return null;
            }
            int tipoVar = tabela.getSymbol(nomeVar).getType();
            listaComandosAtual.add(new CommandEscrita(nomeVar, tipoVar));
        }
        else if (ctx.Cadeia() != null) {
            String textoLiteral = ctx.Cadeia().getText();
            listaComandosAtual.add(new CommandEscrita(textoLiteral));
        }
        return null;
    }

    // =====================================================================
    // 5. COMANDO CONDICIONAL (SE-ENTAO-SENAO)
    // =====================================================================
    @Override
    public Integer visitComandoCondicao(GyhLangParser.ComandoCondicaoContext ctx) {


        visit(ctx.expressaoRelacional());

        String condicaoTexto = ctx.expressaoRelacional().getText();

        condicaoTexto = condicaoTexto.replace("OU", "||");
        condicaoTexto = condicaoTexto.replace("E", "&&");

        List<Command> listaAnterior = this.listaComandosAtual;

        List<Command> listaEntao = new ArrayList<>();
        this.listaComandosAtual = listaEntao;
        visit(ctx.comando(0));

        List<Command> listaSenao = new ArrayList<>();
        if (ctx.PCSenao() != null) {
            this.listaComandosAtual = listaSenao;
            visit(ctx.comando(1));
        }

        this.listaComandosAtual = listaAnterior;

        listaComandosAtual.add(new CommandCondicao(condicaoTexto, listaEntao, listaSenao));

        return null;
    }

    // =====================================================================
    // 6. COMANDO REPETIÇÃO (ENQTO)
    // =====================================================================
    @Override
    public Integer visitComandoRepeticao(GyhLangParser.ComandoRepeticaoContext ctx) {

        visit(ctx.expressaoRelacional());

        String condicaoTexto = ctx.expressaoRelacional().getText();

        condicaoTexto = condicaoTexto.replace("OU", "||");
        condicaoTexto = condicaoTexto.replace("E", "&&");

        List<Command> listaAnterior = this.listaComandosAtual;
        List<Command> listaBlocoWhile = new ArrayList<>();

        this.listaComandosAtual = listaBlocoWhile;
        visit(ctx.comando());

        this.listaComandosAtual = listaAnterior;

        listaComandosAtual.add(new CommandRepeticao(condicaoTexto, listaBlocoWhile));

        return null;
    }

    // =====================================================================
    // 7. VALIDAÇÃO DE TIPOS MATEMÁTICOS (ÁRVORE DE EXPRESSÕES)
    // =====================================================================
    @Override
    public Integer visitExpressaoAritmetica(GyhLangParser.ExpressaoAritmeticaContext ctx) {

        int tipo = Simbolos.INT;

        for (GyhLangParser.TermoAritmeticoContext termo : ctx.termoAritmetico()) {
            int t = visit(termo);

            if (t == Simbolos.REAL) {
                tipo = Simbolos.REAL;
            }
        }

        return tipo;
    }

    @Override
    public Integer visitTermoAritmetico(GyhLangParser.TermoAritmeticoContext ctx) {

        int tipo = Simbolos.INT;

        for (GyhLangParser.FatorAritmeticoContext fator : ctx.fatorAritmetico()) {
            int t = visit(fator);

            if (t == Simbolos.REAL) {
                tipo = Simbolos.REAL;
            }
        }

        return tipo;
    }

    @Override
    public Integer visitFatorAritmetico(GyhLangParser.FatorAritmeticoContext ctx) {

        // Variável
        if (ctx.Var() != null) {

            String nomeVar = ctx.Var().getText();

            if (!tabela.existe(nomeVar)) {
                System.err.println(
                        "Erro Semântico: Variável '" + nomeVar +
                                "' usada na expressão matemática não foi declarada."
                );
                possuiErro = true;
                return -1;
            }

            return tabela.getSymbol(nomeVar).getType();
        }

        // Número inteiro
        if (ctx.NumInt() != null) {

            try {

                BigInteger valor = new BigInteger(ctx.NumInt().getText());

                if (valor.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0 ||
                        valor.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0) {

                    System.err.println(
                            "Erro Semântico: Overflow em constante inteira: "
                                    + valor
                    );

                    possuiErro = true;
                    return -1;
                }

            } catch (NumberFormatException e) {

                System.err.println(
                        "Erro Semântico: Constante inteira inválida."
                );

                possuiErro = true;
                return -1;
            }

            return Simbolos.INT;
        }


        if (ctx.NumReal() != null) {

            return Simbolos.REAL;
        }


        if (ctx.expressaoAritmetica() != null) {

            return visit(ctx.expressaoAritmetica());
        }

        return -1;
    }

    @Override
    public Integer visitExpressaoRelacional(GyhLangParser.ExpressaoRelacionalContext ctx) {
        return visit(ctx.expressaoBooleana());
    }

    @Override
    public Integer visitExpressaoBooleana(GyhLangParser.ExpressaoBooleanaContext ctx) {

        for (GyhLangParser.FatorBooleanoContext fator : ctx.fatorBooleano()) {
            visit(fator);
        }

        return null;
    }

    @Override
    public Integer visitFatorBooleano(GyhLangParser.FatorBooleanoContext ctx) {

        if (ctx.comparacao() != null) {
            visit(ctx.comparacao());
        }

        if (ctx.expressaoBooleana() != null) {
            visit(ctx.expressaoBooleana());
        }

        return null;
    }

    @Override
    public Integer visitComparacao(GyhLangParser.ComparacaoContext ctx) {

        visit(ctx.expressaoAritmetica(0));
        visit(ctx.expressaoAritmetica(1));

        return null;
    }
}