import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Socket server
 */
public class SocketServer {

    private ServerSocket serverSocket;
    private int port;

    public SocketServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        System.out.println("Starting socket server at port: " + port);
        serverSocket = new ServerSocket(port);

        // Listen for clients. Block until connection

        System.out.println("Waiting for clients");
        Socket client = serverSocket.accept();

        // Client connected!
        sendWelcomeMessage(client);
    }

    private void sendWelcomeMessage(Socket client) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        writer.write("Hello. You are connected to a simple socket server.");
        writer.flush();
        writer.close();
    }

    /**
     * Main driver for server
     */
    public static void main(String[] args) {
        int portNumber = 9000;

        try {
            SocketServer socketServer = new SocketServer(portNumber);
            socketServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
