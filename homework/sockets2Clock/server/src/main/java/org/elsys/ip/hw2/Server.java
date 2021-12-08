package org.elsys.ip.hw2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TimeZone;


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
                    String args[] = line.split(" ");
//
                    if (args.length == 2) {
                        if (args[0].equalsIgnoreCase("time")) time_func(out, args[1]);
                        else if (args[0].equalsIgnoreCase("quit") || args[0].equalsIgnoreCase("exit")) this.dispose();
                        else  out.println("invalid input");
                    } else if (args.length == 3) {
                        System.out.println("go");
                        if (args[0].equalsIgnoreCase("time")) clock((args[0] + " " + args[1]), out);
                        else out.println("invalid input");
                    } else {
                        out.println("invalid input");
                    }

                    //System.out.println(line); // printira line ot potrebitel

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

    private static void time_func(PrintWriter out, String zone) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(zone));

        if (TimeZone.getTimeZone(ZoneId.of(zone)).useDaylightTime()) {
            ZonedDateTime ret = zonedDateTime.minusHours(1);
            out.println(ret.format(dtf));
        } else {
            out.println(zonedDateTime.format(dtf));
        }
    }

    private static void clock(String line, PrintWriter out) {

        String[] sep = line.split(" ");
        if (sep[0].equalsIgnoreCase("time")) {
            if (sep.length > 2) {
                out.println("invalid input");
            } else if (sep[0].equalsIgnoreCase("time")) {

                if (sep[1].charAt(0) == '+') {
                    OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

                    try {
                        sep[1] = sep[1].substring(1);
                        String[] hh_mm = sep[1].split(":");
                        if (hh_mm[0].length() != 2 || hh_mm[1].length() != 2) {
                            out.println("invalid input");
                            return;
                        }
                        if (!checktimeZone(("+" + sep[1]))) {
                            out.println("invalid time zone");
                            return;
                        }

                        out.println(now.plusHours(Integer.parseInt(hh_mm[0])).plusMinutes(Integer.parseInt(hh_mm[1])).format(dtf));
                    } catch (Exception e) {
                        out.println("invalid input");
                    }

                } else if (sep[1].charAt(0) == '-') {
                    OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
                    try {
                        sep[1] = sep[1].substring(1);
                        String[] hh_mm = sep[1].split(":");
                        if (hh_mm[0].length() != 2 || hh_mm[1].length() != 2) {
                            out.println("invalid input");
                            return;
                        }
                        if (!checktimeZone(("-" + sep[1])) ) {
                            out.println("invalid time zone");
                            return;
                        }
                        out.println(now.minusHours(Integer.parseInt(hh_mm[0])).minusMinutes(Integer.parseInt(hh_mm[1])).format(dtf));
                    } catch (Exception e) {
                        out.println("invalid input");
                    }
                } else {
                    out.println("invalid input");
                }

            }
        } else {
            out.println("invalid input");
        }
    }

    private static boolean checktimeZone(String arg) { // проверка дали даденото + или - е валидно в Time зоните по условие
        LocalDateTime now = LocalDateTime.now();

        for (String zone : ZoneId.getAvailableZoneIds()) {
            ZoneId id = ZoneId.of(zone);
            ZonedDateTime zoneTime = now.atZone(id);
            ZoneOffset Offset = zoneTime.getOffset();
            String legal = Offset.getId().replaceAll("Z", "+00:00");
            if (legal.equalsIgnoreCase(arg)) return true;
        }
        return false;
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