package br.edu.icev.aed.forense;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {

        SolucaoForense sf = new SolucaoForense();

        List<Alerta> top = sf.priorizarAlertas("teste3.csv", 10);

        for (Alerta a : top) {
            System.out.println(a);
        }

    }
    }

