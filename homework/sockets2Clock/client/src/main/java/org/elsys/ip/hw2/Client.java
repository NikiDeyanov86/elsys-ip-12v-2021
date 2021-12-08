package org.elsys.ip.hw2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.*;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, String port) throws IOException {
        try {
            clientSocket = new Socket(ip, Integer.parseInt(port));
        } catch (UnknownHostException swag) {
            System.err.println("invalid host");
            System.exit(3);
        } catch (IOException e) {
            System.err.println("connection not possible");
            System.exit(4);
        } catch (Exception ex) {
            System.err.println("invalid arguments");
            System.exit(1);
        }
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


        new Thread(() -> {
            try {
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        stopConnection();
                    }
                    if (line != null)
                        System.out.println(line);
                }
            } catch (Throwable t) {
                t.printStackTrace();
                System.exit(1);
            }
        }).start();
    }

    public void sendMessage(String msg) {
        Clock clock = Clock.systemDefaultZone();
        if (!msg.equals("")) out.println(msg + " " + clock.getZone());
    }


    public void stopConnection() {
            System.out.println("server disconnect");
            System.exit(0);
    }


    public static void main(String[] args) throws IOException {

        if (args.length == 1) {
            String[] hostPort = args[0].split(":");

                Client client = new Client();
                client.startConnection(hostPort[0], hostPort[1]);

                Scanner scanner = new Scanner(System.in);

                while (true) {
                    String line = scanner.nextLine();
                    client.sendMessage(line);

                }

        } else {
            System.err.println("invalid arguments");
            System.exit(1);
        }



    }
}
