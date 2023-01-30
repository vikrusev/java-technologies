package bg.sofia.uni.fmi.mjt.chat;

import bg.sofia.uni.fmi.mjt.chat.messages.Messages;
import bg.sofia.uni.fmi.mjt.chat.services.ChatServerService;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

import static java.util.Map.Entry;
import static bg.sofia.uni.fmi.mjt.chat.messages.Messages.*;

public final class ChatServer extends ChatServerService {

    private Map<SocketChannel, String> users = new HashMap<>();

    private boolean terminating = false;

    public ChatServer(int port) throws IOException {
        super(port);

        System.out.printf("--- server started on: %s%n", InetAddress.getLocalHost().getHostAddress());
    }

    public void start() throws IOException {
        while (runServer) {
            int readyChannels = selector.select();

            if (readyChannels ==  0) {
                continue;
            }

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                if (key.isAcceptable()) {
                    accept(key);
                }
                else if (key.isReadable()) {
                    read(key);
                }

                keyIterator.remove();
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssChannel.accept();
        sc.configureBlocking(false);
        sc.register(this.selector, SelectionKey.OP_READ);

        System.out.printf("%n--- a client has connected%n");
    }

    private void read(SelectionKey key) {

        SocketChannel sc = (SocketChannel) key.channel();

        this.commandBuffer.clear();
        try {
            sc.read(this.commandBuffer);
            this.commandBuffer.flip();

            String command = Charset.forName("UTF-8").decode(this.commandBuffer).toString();
            Messages response = executeCommand(command, sc);

            // no response needed for these cases
            if (response == RESPONSE_TEXT && "".equals(response.getMessage())) {
                return;
            }

            if (response == DISCONNECT_OK) {
                return;
            }

            // internal server error - stop the server
            if (response == DISCONNECT_FAIL) {
                System.err.println("--- could not close а socket");
                this.stop();
            }

            String responseText = response.getMessage();

            this.commandBuffer.clear();
            this.commandBuffer.put(responseText.getBytes());
            this.commandBuffer.put(System.lineSeparator().getBytes());

            this.commandBuffer.flip();
            while (commandBuffer.hasRemaining()) {
                sc.write(commandBuffer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Messages executeCommand(String recvMsg, SocketChannel sc) {
        recvMsg = recvMsg.trim().replaceAll(" +", " ");

        String[] cmdParts = recvMsg.split(" ");

        if (cmdParts.length == 0) {
            return NO_COMMAND;
        }

        String command = cmdParts[0].trim();

        if ("connect".equals(command)) {
            if (cmdParts.length < 2) {
                return MISSING_USERNAME;
            }

            String username = cmdParts[1].trim();
            return newClient(username, sc);
        }

        switch (command) {
            case "disconnect": return disconnectClient(sc);
            case "list-users": return listUsers();
            case "send": return sendMsg(recvMsg, sc);
            case "send-all": return sendAll(recvMsg, sc);
            default: return UNKNOWN_COMMAND;
        }

    }

    private Messages newClient(String username, SocketChannel sc) {
        if (!users.containsValue(username)) {
            users.put(sc, username);
            System.out.printf("%s has connected%n", username);

            return CONNECTED_OK;
        }

        return USERNAME_EXISTS;
    }

    private Messages disconnectClient(SocketChannel sc) {

        try {
            sc.close();
        } catch (IOException e) {
            return DISCONNECT_FAIL;
        }

        String username = users.get(sc);
        users.remove(sc);
        System.out.printf("%s has disconnected%n", username);

        return DISCONNECT_OK;
    }

    private Messages listUsers() {
        StringBuilder list = new StringBuilder();
        list.append("Users online: ").append(users.size());

        users.values().forEach(name -> list
                                        .append(System.lineSeparator())
                                        .append("-- ")
                                        .append(name));

        RESPONSE_TEXT.setMessage(list.toString());
        return RESPONSE_TEXT;
    }

    private Messages sendMsg(String line, SocketChannel sc) {
        String[] lineParts = line.split(" ");

        try {
            String userFrom = users.get(sc);
            String userTo = lineParts[1];

            SocketChannel socketTo = getUserByUsername(userTo).getKey();

            StringBuilder message = new StringBuilder();
            Arrays.stream(lineParts)
                    .skip(2)
                    .forEach(part -> message.append(" ").append(part));

            return send(message.toString(), userFrom, socketTo);

        } catch (Exception e) {
            RESPONSE_TEXT.setMessage(e.getMessage());
            return RESPONSE_TEXT;
        }
    }

    private Messages sendAll(String line, SocketChannel sc) {
        String[] lineParts = line.split(" ");

        String userFrom = users.get(sc);

        StringBuilder message = new StringBuilder();
        Arrays.stream(lineParts)
                .skip(1)
                .forEach(part -> message.append(" ").append(part));

        int usersCount = users.size();
        if (usersCount > 1 || (terminating && usersCount > 0)) {

            if (!terminating) {
                System.out.printf("%s sent a message to everybody%n", userFrom);
            }

            for (SocketChannel scc : users.keySet()) {
                if (scc != sc) {
                    send(message.toString(), userFrom, scc);
                }
            }

            RESPONSE_TEXT.setMessage("");
            return RESPONSE_TEXT;
        }

        return NO_ONLINE_USERS;
    }

    private Messages send(String message, String userFrom, SocketChannel socketTo) {
        try {
            this.commandBuffer.clear();
            this.commandBuffer.put(String.format("[%s]:", userFrom).getBytes());
            this.commandBuffer.put(message.getBytes());
            this.commandBuffer.put(System.lineSeparator().getBytes());

            this.commandBuffer.flip();
            while (commandBuffer.hasRemaining()) {
                socketTo.write(commandBuffer);
            }
        } catch (IOException e) {
            RESPONSE_TEXT.setMessage(e.getMessage());
            return RESPONSE_TEXT;
        }

        if (!terminating) {
            System.out.printf("%s sent a message%n", userFrom);
        }

        RESPONSE_TEXT.setMessage("");
        return RESPONSE_TEXT;
    }

    // if the server shutdowns - send a proper message to all currently connected users and disconnect them
    private void killClients() {
        sendAll("send-all " + STOP_ALL_CLIENTS, null);

        HashSet<SocketChannel> tempSet = new HashSet<>(users.keySet());
        for (SocketChannel sc : tempSet) {
            disconnectClient(sc);
        }
    }

    private void stop() {
        try {
            this.runServer = false;
            this.terminating = true;

            this.killClients();

            final int second = 1000;
            Thread.sleep(second);

            System.out.printf("sockets still opened: %d%n", users.size());
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        this.stop();

        this.selector.close();
        this.commandBuffer.clear();
        this.serverSocketChannel.close();
    }

    private Entry<SocketChannel, String> getUserByUsername(String username) throws Exception {
        for (Entry<SocketChannel, String> user : users.entrySet()) {
            if (user.getValue().equals(username)) {
                return user;
            }
        }

        throw new Exception(USER_NOT_FOUND.getMessage());
    }

    public static void main(String[] args) {
        final int serverPort = 8080;

        try (ChatServer es = new ChatServer(serverPort)) {
            es.start();
        } catch (IOException e) {
            System.err.println("Server could not be started");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Server could not be auto-closed");
            e.printStackTrace();
        }
    }
}