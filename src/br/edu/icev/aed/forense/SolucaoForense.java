package br.edu.icev.aed.forense;

import br.edu.icev.aed.forense.AnaliseForenseAvancada;
import br.edu.icev.aed.forense.Alerta;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SolucaoForense implements AnaliseForenseAvancada {
    private static class LogEvent {
        long timestamp;
        String userId;
        String sessionId;
        String action;
        String resource;
        int severity;
        long bytes;

        LogEvent(long timestamp, String userId, String sessionId,
                 String action, String resource, int severity, long bytes) {
            this.timestamp = timestamp;
            this.userId = userId;
            this.sessionId = sessionId;
            this.action = action;
            this.resource = resource;
            this.severity = severity;
            this.bytes = bytes;
        }
    }

    private List<LogEvent> lerLogs(String caminho) throws IOException {
        List<LogEvent> logs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha = br.readLine();
            while ((linha = br.readLine()) != null) {
                String[] c = linha.split(",");

                long timestamp = Long.parseLong(c[0]);
                String user = c[1];
                String session = c[2];
                String action = c[3];
                String resource = c[4];
                int severity = Integer.parseInt(c[5]);
                long bytes = Long.parseLong(c[6]);

                logs.add(new LogEvent(timestamp, user, session, action, resource, severity, bytes)));
            }
        }
        return logs;
    }


    @Override
    public Set<String> encontrarSessoesInvalidas(String Csv) throws IOException {
        List<LogEvent> logs = lerLogs(Csv);

        Map<String, Stack<String>> pilhaUsuario = new HashMap<>();
        Set<String> invalidas = new HashSet<>();

        for (LogEvent ev : logs) {

            pilhaUsuario.putIfAbsent(ev.userId, new Stack<>());
            Stack<String> pilha = pilhaUsuario.get(ev.userId);

            if (ev.action.equals("LOGIN")) {

                if (!pilha.isEmpty()) {
                    invalidas.add(ev.sessionId);
                }

                pilha.push(ev.sessionId)
    }
            else if (ev.action.equals("LOGOUT")) {

                if (pilha.isEmpty()) {
                    invalidas.add(ev.sessionId);
                }
                else {
                    String topo = pilha.peek();

                    if (!topo.equals(ev.sessionId)) {
                        invalidas.add(ev.sessionId);
                    } else {
                        pilha.pop();
                    }
                }
            }
        }
        for (Stack<String> pilha : pilhaUsuario.values()) {
            while (!pilha.isEmpty()) {
                invalidas.add(pilha.pop());
            }
            return invalidas;
        }


        @Override
        public List<String> reconstruirLinhaTempo(String var1, String var2) throws IOException {
            return List.of();
        }


        @Override
        public List<Alerta> priorizarAlertas(String var1, int var2) throws IOException {
            return List.of();
        }


        @Override
        public Map<Long, Long> encontrarPicosTransferencia(String var1) throws IOException {
            return Map.of();
        }


        @Override
        public Optional<List<String>> rastrearContaminacao(String var1, String var2, String var3) throws IOException {
            return Optional.empty();
        }

    }
