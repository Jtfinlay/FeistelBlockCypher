import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Socket server
 */
public class SocketServer {

    private int _port;
    ServerSocket serverSocket;

    public SocketServer(int port) {
        _port = port;
    }

    public void start() throws IOException {
        System.out.println("Starting server at port: " + _port);
        serverSocket = new ServerSocket(_port);

        // Listen for clients. Block until connection

        while (true) {
            System.out.println("Waiting for clients");
            Socket client = serverSocket.accept();
            System.out.println("Client has connected.");

            Thread thread = new Thread(new ClientHandler(client));
            thread.start();
        }
    }

    /**
     * Main driver for server
     */
    public static void main(String[] args) {
        int portNumber = 16000;

        try {
            SocketServer socketServer = new SocketServer(portNumber);
            socketServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
