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
                String[] c = linha.split(",");

                long timestamp = Long.parseLong(c[0]);
                String user = c[1];
                String session = c[2];
                String action = c[3];
                String resource = c[4];
                int severity = Integer.parseInt(c[5]);
                long bytes = c[6].isEmpty() ? 0 : Long.parseLong(c[6]);

                logs.add(new LogEvent(timestamp, user, session, action, resource, severity, bytes));
            }
        }
        return logs;
    }


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


        @Override
        public List<String> reconstruirLinhaTempo (String caminhoarq, String sessionID) throws IOException {
        List<LogEvent> logs = lerLogs(caminhoarq);

        Queue<String> fila = new LinkedList<>();

        for (LogEvent acao : logs) {
            if (acao.sessionId.equals(sessionID)) {
                fila.add(acao.action);
            }
        }
        return new ArrayList<>(fila);

        }


    @Override
    public List<Alerta> priorizarAlertas(String caminhoarq, int n) throws IOException {

        List<LogEvent> logs = lerLogs(caminhoarq);

        PriorityQueue<Alerta> fila = new PriorityQueue<>(new Comparator<Alerta>() {
            @Override
            public int compare(Alerta a1, Alerta a2) {
                return Integer.compare(a2.getSeverity(), a1.getSeverity());
            }
        });

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



    @Override
        public Map<Long, Long> encontrarPicosTransferencia (String var1) throws IOException {
            return Map.of();
        }


        @Override
        public Optional<List<String>> rastrearContaminacao (String var1, String var2, String var3) throws IOException {
            return Optional.empty();
        }

    }
