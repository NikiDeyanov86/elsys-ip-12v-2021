package org.elsys.ip.sockets.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    private ServerSocket serverSocket;
    private Map<String, CalculatorClientHandler> clients = new HashMap<>();

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            new CalculatorClientHandler(serverSocket.accept(), this).start();
        }
    }

    private static class CalculatorClientHandler extends Thread {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private final ChatServer server;
        private String name;

        public CalculatorClientHandler(Socket socket, ChatServer server) {
            this.clientSocket = socket;
            this.server = server;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                name = in.readLine().replace(" ", "_");
                server.addClient(this, name);

                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }

                    server.printlnAll(name, line);
                }
            } catch (Throwable t) {
                System.out.println(t.getMessage());
            } finally {
                dispose();
            }
        }

        private void dispose() {
            try {
                if (clientSocket != null) clientSocket.close();
                if (in != null) in.close();
                if (out != null) out.close();
                server.removeClient(name);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        public void println(String line) {
            out.println(line);
        }
    }

    private void removeClient(String name) {
        clients.remove(name);
    }

    private void addClient(CalculatorClientHandler client, String name) {
        clients.put(name, client);
    }

    private void printlnAll(String name, String line) {
        if (line.startsWith("dm ")) {
            String[] lineSplit = line.split(" ");
            String dmTo = lineSplit[1];
            String dmMessage = line.substring(3 + dmTo.length() + 1);
            if (clients.containsKey(dmTo)) {
                clients.get(dmTo).println(name + " (dm) > " + dmMessage);
            } else {
                clients.get(name).println("Invalid DM receiver.");
            }
        } else {
            for (CalculatorClientHandler client : clients.values()) {
                client.println(name + " > " + line);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ChatServer server = new ChatServer();
        server.start(6666);
    }
}
