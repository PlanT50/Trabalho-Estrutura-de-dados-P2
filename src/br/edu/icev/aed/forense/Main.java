package br.edu.icev.aed.forense;

import java.io.IOException;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {

        SolucaoForense teste = new SolucaoForense();

        Set<String> invalidas = teste.encontrarSessoesInvalidas("teste.csv");

        System.out.println("Sessões inválidas encontradas:");
        for (String s : invalidas) {
            System.out.println(s);
        }
    }
}
