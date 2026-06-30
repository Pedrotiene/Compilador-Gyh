package compilador.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LeitorArquivos {

    public static List<String> listarArquivos(String caminhoPasta) {

        List<String> arquivos = new ArrayList<>();

        File pasta = new File(caminhoPasta);

        if (!pasta.exists() || !pasta.isDirectory()) {
            throw new IllegalArgumentException("Pasta inválida: " + caminhoPasta);
        }

        File[] lista = pasta.listFiles();

        if (lista == null) {
            return arquivos;
        }

        for (File arquivo : lista) {

            if (arquivo.isFile() &&
                    arquivo.getName().toLowerCase().endsWith(".gyh")) {

                arquivos.add(arquivo.getAbsolutePath());
            }
        }

        return arquivos;
    }
}