package bg.sofia.uni.fmi.mjt.chat;

import java.io.*;
import java.net.Socket;

import static bg.sofia.uni.fmi.mjt.chat.messages.Messages.DISCONNECT_OK;
import static bg.sofia.uni.fmi.mjt.chat.messages.Messages.SERVER_CLOSED;
import static bg.sofia.uni.fmi.mjt.chat.messages.Messages.STOP_ALL_CLIENTS;

public class ClientRunnable implements Runnable {

    private static final String KILLED = STOP_ALL_CLIENTS.getMessage();

    private Socket socket;
    private boolean running = true;

    ClientRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (running) {
                if (socket.isClosed()) {
                    System.out.println("client socket is closed, stop waiting for server messages");
                    return;
                }

                String received = reader.readLine();

                // if the response is the super-random string for disconnection
                // kill the client thread
                if (isKilled(received)) {
                    running = false;
                    System.err.println(SERVER_CLOSED);
                    System.exit(0);
                }
                else {
                    System.out.println(received);
                }
            }
        } catch (IOException e) {
            // disconnect command sent to server and it has closed the socket
            if ("Socket closed".equals(e.getMessage())) {
                System.out.println(DISCONNECT_OK);
            }
            // some internal server error
            else if ("Connection reset".equals(e.getMessage())) {
                System.err.println(SERVER_CLOSED);
            }
            // everything else
            else {
                System.err.println(e.getMessage());
            }

            System.exit(0);
        }
    }

    private boolean isKilled(String received) {
        return received.split(" ")[1].equals(KILLED);
    }

}