package bg.sofia.uni.fmi.mjt.chat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;

public class ChatServerTest {

    private static final int SERVER_PORT = 8080;

    @Mock
    private ChatClient chatClient;

    @Before
    public void initServer() {
        try (ChatServer es = new ChatServer(SERVER_PORT)) {
            es.start();
        } catch (IOException e) {
            System.err.println("Server could not be started");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Server could not be auto-closed");
            e.printStackTrace();
        }
    }

    @Test
    public void serverReceivedConnection() throws IOException {
        //doReturn().when(ssc.accept(any(SelectionKey.class)));
    }

}
