package org.elsys.ip.sockets.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServer {
    private ServerSocket serverSocket;
    private List<CalculatorClientHandler> clients = new ArrayList<>();

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            CalculatorClientHandler client = new CalculatorClientHandler(serverSocket.accept(), this);
            clients.add(client);
            client.start();
        }
    }

    private static class CalculatorClientHandler extends Thread {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private final ChatServer server;

        public CalculatorClientHandler(Socket socket, ChatServer server) {
            this.clientSocket = socket;
            this.server = server;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    server.printlnAll(line);
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
                server.removeClient(this);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        public void println(String line) {
            out.println(line);
        }
    }

    private void removeClient(CalculatorClientHandler client) {
        clients.remove(client);
    }

    private void printlnAll(String line) {
        for (CalculatorClientHandler client : clients) {
            client.println(line);
        }
    }

    public static void main(String[] args) throws IOException {
        ChatServer server = new ChatServer();
        server.start(6666);
    }
}
