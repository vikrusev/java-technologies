package bg.sofia.uni.fmi.java.network.server.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * This is a command execution server. It is used to listen for
 * incoming commands to execute them and to return a response.
 * We have implemented only two commands:
 *  - echo:test - echo all the data after the echo: command
 *  - gethostname - returns the hostname of the remote machine
 */
public class CommandExecutionServer implements AutoCloseable {

    public static final int SERVER_PORT = 4444;

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;
    private ByteBuffer commandBuffer;
    private boolean runServer = true;

    public CommandExecutionServer(int port) throws IOException {
        final int size = 1024;

        this.selector = Selector.open();
        this.commandBuffer = ByteBuffer.allocate(size);

        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.socket().bind(new InetSocketAddress(port));
        this.serverSocketChannel.configureBlocking(false);

        this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }

    /**
     * Start the server
     * @throws IOException
     */
    public void start() throws IOException {
        while (runServer) {
            int readyChannels = selector.select();
            if (readyChannels == 0) {
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

    /**
     * Stop the server
     * @throws IOException
     */
    public void stop() throws Exception {
        this.runServer = false;
        this.close();
    }

    /**
     * Accept a new connection
     *
     * @param key The key for which an accept was received
     * @throws IOException In case of problems with the accept
     */
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssChannel.accept();
        sc.configureBlocking(false);
        sc.register(this.selector, SelectionKey.OP_READ);
    }

    /**
     * Read data from a connection
     *
     * @param key The key for which data was received
     */
    private void read(SelectionKey key) {

        SocketChannel sc = (SocketChannel) key.channel();

        this.commandBuffer.clear();
        try {
            sc.read(this.commandBuffer);
            this.commandBuffer.flip();

            String command = Charset.forName("UTF-8").decode(this.commandBuffer).toString();
            String response = executeCommand(command);

            commandBuffer.clear();
            commandBuffer.put(response.getBytes());
            commandBuffer.put(System.lineSeparator().getBytes());

            this.commandBuffer.flip();
            while (commandBuffer.hasRemaining()) {
                sc.write(commandBuffer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validate and execute the received commands from the clients
     *
     * @param recvMsg
     * @return The result of the execution of the command
     */
    private String executeCommand(String recvMsg) {
        String[] cmdParts = recvMsg.split(":");

        if (cmdParts.length > 2) {
            return "Incorrect command syntax";
        }

        String command = cmdParts[0].trim();

        if (command.equalsIgnoreCase("echo")) {
            if (cmdParts.length <= 1) {
                return "Missing Argument";
            }
            return cmdParts[1].strip();
        } else if (command.equalsIgnoreCase("gethostname")) {
            try {
                return InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return "Could not get hostname";
            }
        } else {
            return "Unknown command";
        }
    }

    @Override
    public void close() throws Exception {
        this.selector.close();
        this.commandBuffer.clear();
        this.serverSocketChannel.close();
    }

    public static void main(String[] args) {
        try (CommandExecutionServer es = new CommandExecutionServer(SERVER_PORT)) {
            es.start();
        } catch (Exception e) {
            System.out.println("An error has occured");
            e.printStackTrace();
        }
    }
}