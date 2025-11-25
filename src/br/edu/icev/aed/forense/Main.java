package br.edu.icev.aed.forense;

import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {

        SolucaoForense sf = new SolucaoForense();

        // o arquivo deve estar na raiz do projeto!!
        String caminho = "teste4.csv";

        Map<Long, Long> picos = sf.encontrarPicosTransferencia(caminho);

        System.out.println("==== RESULTADO DO DESAFIO 4 ====");
        for (Map.Entry<Long, Long> e : picos.entrySet()) {
            System.out.println("Evento em " + e.getKey() +
                    " → próximo evento maior em " + e.getValue());
        }

        System.out.println("\nFinalizado.");
    }
}
