package compilador.commands;

import java.util.HashMap;

public class TabelaDeSimbolos {
    private HashMap<String, Simbolos> tabela = new HashMap<>();

    public void adicionar(Simbolos symbol) {
        tabela.put(symbol.getName(), symbol);
    }

    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }

    public Simbolos getSymbol(String nome) {
        return tabela.get(nome);
    }

    public java.util.Collection<Simbolos> getAll() {
        return tabela.values();
    }
}