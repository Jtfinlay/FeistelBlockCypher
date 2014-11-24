import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {

    private String _hostname;
    private int _port;
    Socket socketClient;

    public SocketClient(String hostname, int port) {
        _hostname = hostname;
        _port = port;
    }

    public void connect() throws UnknownHostException, IOException {
        System.out.println("Ateemption to connect to "+_hostname+".");
        socketClient = new Socket(_hostname, _port);
        System.out.println("Connection established");
    }

    public void readResponse() throws IOException {
        String userInput;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

        System.out.println("Response from server:");
        while ((userInput = stdIn.readLine()) != null) {
            System.out.println(userInput);
        }
    }

    public static void main(String args[]) {
        SocketClient client = new SocketClient("localhost", 9000);
        try {
            client.connect();
            client.readResponse();
        } catch (UnknownHostException e) {
            System.err.println("Host unknown.");
        } catch (IOException e) {
            System.err.println("Cannot establish connection.");
        }
    }

}
