package org.elsys.ip.hw2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> clients = new ArrayList<>();

    public void start(int port) throws IOException {
        try {
            serverSocket = new ServerSocket(port);
        } catch (BindException e) {
            System.err.println("port is already in use");
            System.exit(2);
        } catch (Exception ex) {
            System.err.println("invalid arguments");
            System.exit(1);
        }

        while (true) {
            new ClientHandler(serverSocket.accept(), this).start();
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private final Server server;

        public ClientHandler(Socket socket, Server server) {
            this.clientSocket = socket;
            this.server = server;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                server.addClient(this);
                //System.out.println("Connection of a new Client...");

                while (true) {
                    String line = in.readLine();
                   // System.out.println(line); // printira line ot potrebitel
                    if (line == null) {
                        break;
                    }

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


    }

    private void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    private void addClient(ClientHandler client) {
        clients.add(client);
    }


    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            int port = 0;
            try {
                port = Integer.parseInt(args[0]);
                if (port < 0) {
                    System.err.println("invalid arguments");
                    System.exit(1);
                }
            } catch (NumberFormatException e) {
                System.err.println("invalid arguments");
                System.exit(1);
            }

            Server server = new Server();
            server.start(port);
        } else {
            System.err.println("invalid arguments");
            System.exit(1);
        }

    }
}