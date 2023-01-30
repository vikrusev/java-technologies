package bg.sofia.uni.fmi.mjt.chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    private PrintWriter writer = null;
    private boolean connected = false;

    private static final int C_IDX = 0;
    private static final int H_IDX = 1;
    private static final int P_IDX = 2;
    private static final int U_IDX = 3;

    private void run() {
        try (Scanner scanner = new Scanner(System.in)) {

            while (true) {
                String input = scanner.nextLine();

                String[] tokens = input.split(" ");
                String command = tokens[C_IDX];

                if ("connect".equals(command)) {
                    String host = tokens[H_IDX];
                    int port = Integer.parseInt(tokens[P_IDX]);
                    String username = tokens[U_IDX];

                    connect(host, port, username);

                } else if (connected) {
                    if ("disconnect".equals(command)) {
                        disconnect(command);

                        return;
                    }

                    writer.println(input);
                } else {
                    System.out.println("=> client should first connect to a server");
                }
            }
        }
    }

    private void connect(String remoteHost, int remotePort, String username) {
        try {
            // if we are already connected - do not open a new socket
            // this case can be reached if we try to connect with a username that is already in use
            if (!connected) {
                Socket socket = new Socket(remoteHost, remotePort);
                this.writer = new PrintWriter(socket.getOutputStream(), true);
                this.connected = true;

                System.out.println("=> successfully opened a socket");
                writer.println("connect " + username);

                ClientRunnable clientRunnable = new ClientRunnable(socket);
                new Thread(clientRunnable).start();
            }

            else {
                writer.println("connect " + username);
            }
        } catch (IOException e) {
            System.out.printf("=> cannot connect to server on %s:%d, make sure that the server is started",
                            remoteHost, remotePort);
        }
    }

    private void disconnect(String disconnect) {
        writer.println(disconnect);
        this.writer.close();
        this.connected = false;
    }

    public static void main(String[] args) {
        new ChatClient().run();
    }

}