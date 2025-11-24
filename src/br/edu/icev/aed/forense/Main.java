package br.edu.icev.aed.forense;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {

        SolucaoForense teste = new SolucaoForense ();

        List<String> lihadotempo = teste.reconstruirLinhaTempo("teste2.csv", "s1");

        System.out.println("linha do tempo: s1 " );
        for (String acao : lihadotempo) {
            System.out.println(acao);
        }
    }
}
