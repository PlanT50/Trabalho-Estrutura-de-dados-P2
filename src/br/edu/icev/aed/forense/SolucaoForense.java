package br.edu.icev.aed.forense;

import br.edu.icev.aed.forense.AnaliseForenseAvancada;
import br.edu.icev.aed.forense.Alerta;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SolucaoForense implements AnaliseForenseAvancada {
    public SolucaoForense() {}

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

    private List<LogEvent> lerLogs(String caminhoarq) throws IOException {
        List<LogEvent> logs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoarq))) {
            String linha = br.readLine();
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                String[] c = linha.split(",");

                if (c.length < 7) continue;

                try {
                    long timestamp = Long.parseLong(c[0].trim());
                    String user = c[1].trim();
                    String session = c[2].trim();
                    String action = c[3].trim();
                    String resource = c[4].trim();
                    int severity = Integer.parseInt(c[5].trim());
                    long bytes = c[6].trim().isEmpty() ? 0 : Long.parseLong(c[6].trim());

                    logs.add(new LogEvent(timestamp, user, session, action, resource, severity, bytes));
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        return logs;
    }


    //D1
    @Override
    public Set<String> encontrarSessoesInvalidas(String caminhoarq) throws IOException {
        List<LogEvent> logs = lerLogs(caminhoarq);

        Map<String, Stack<String>> pilhaUsuario = new HashMap<>();
        Set<String> invalidas = new HashSet<>();

        for (LogEvent ev : logs) {

            pilhaUsuario.putIfAbsent(ev.userId, new Stack<>());
            Stack<String> pilha = pilhaUsuario.get(ev.userId);

            if (ev.action.equals("LOGIN")) {

                if (!pilha.isEmpty()) {
                    invalidas.add(ev.sessionId);
                }

                pilha.push(ev.sessionId);
            } else if (ev.action.equals("LOGOUT")) {

                if (pilha.isEmpty()) {
                    invalidas.add(ev.sessionId);
                } else {
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
        }
            return invalidas;
        }

    //D2
        @Override
        public List<String> reconstruirLinhaTempo (String caminhoarq, String sessionID) throws IOException {
        List<LogEvent> logs = lerLogs(caminhoarq);

            List<String> resultado = new ArrayList<>();

            for (LogEvent ev : logs) {
                if (ev.sessionId.equals(sessionID)) {
                    resultado.add(ev.action);
                }
            }

            return resultado;
        }



    //D3
    @Override
    public List<Alerta> priorizarAlertas(String caminhoarq, int n) throws IOException {

        List<LogEvent> logs = lerLogs(caminhoarq);

        PriorityQueue<Alerta> fila = new PriorityQueue<>(
                (a1, a2) -> Integer.compare(a2.getSeverityLevel(), a1.getSeverityLevel())
        );

        for (LogEvent acao : logs) {
            fila.add(new Alerta(
                    acao.timestamp,
                    acao.userId,
                    acao.sessionId,
                    acao.action,
                    acao.resource,
                    acao.severity,
                    acao.bytes
            ));
        }

        List<Alerta> resultado = new ArrayList<>();

        for (int i = 0; i < n && !fila.isEmpty(); i++) {
            resultado.add(fila.poll());
        }

        return resultado;
    }


    //D4
    @Override
        public Map<Long, Long> encontrarPicosTransferencia (String caminhoarq) throws IOException {
        List<LogEvent> logs = lerLogs(caminhoarq);

        Map<Long, Long> resultado = new HashMap<>();
        Stack<LogEvent> pilha = new Stack<>();

        for (int i = logs.size() - 1; i >= 0; i--) {
            LogEvent atual = logs.get(i);

            while (!pilha.isEmpty() && pilha.peek().bytes <= atual.bytes) {
                pilha.pop();
            }

            if (!pilha.isEmpty()) {
                resultado.put(atual.timestamp, pilha.peek().timestamp);
            }

            pilha.push(atual);
        }

        return resultado;
    }



    @Override
        public Optional<List<String>> rastrearContaminacao (String var1, String var2, String var3) throws IOException {
            return Optional.empty();
        }

    }
