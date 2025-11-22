package br.edu.icev.aed.forense;

import java.io.IOException;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        SolucaoForense teste = new SolucaoForense ();

        Set<String> invalidas =  teste.encontrarSessoesInvalidas("src//forensic_logs.csv");

        System.out.println("Sessões inválidas: ");
        for (String ver : invalidas) {
            System.out.println(ver);
        }
    }
}
