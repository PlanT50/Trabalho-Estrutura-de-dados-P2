package br.edu.icev.aed.forense;

import br.edu.icev.aed.forense.AnaliseForenseAvancada;
import br.edu.icev.aed.forense.Alerta;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SolucaoForense implements AnaliseForenseAvancada {
    @Override
    public Set<String> encontrarSessoesInvalidas(String s) throws IOException {
        return Set.of();
    }

    @Override
    public List<String> reconstruirLinhaTempo(String s, String s1) throws IOException {
        return List.of();
    }

    @Override
    public List<Alerta> priorizarAlertas(String s, int i) throws IOException {
        return List.of();
    }

    @Override
    public Map<Long, Long> encontrarPicosTransferencia(String s) throws IOException {
        return Map.of();
    }

    @Override
    public Optional<List<String>> rastrearContaminacao(String s, String s1, String s2) throws IOException {
        return Optional.empty();
    }
}
