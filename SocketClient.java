import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {

    private Encryption _encrypter;
    private String _hostname;
    private int _port;
    Socket socketClient;

    private String _userID = "Frank";
    private String _key = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    public SocketClient(String hostname, int port) {
        _hostname = hostname;
        _port = port;
        _encrypter = new Encryption();
    }

    public void connect() throws UnknownHostException, IOException {
        System.out.println("Attempting to connect to "+_hostname+".");
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

    public void sendUserID() throws IOException {
        byte[] message = _encrypter.encrypt("USER"+_userID, _key);
        DataOutputStream out = new DataOutputStream(socketClient.getOutputStream());
        out.writeInt(message.length);
        out.write(message);
    }

    public static void main(String args[]) {
        SocketClient client = new SocketClient("localhost", 16000);
        try {
            client.connect();
            client.sendUserID();
            client.readResponse();
        } catch (UnknownHostException e) {
            System.err.println("Host unknown.");
        } catch (IOException e) {
            System.err.println("Cannot establish connection.");
        }
    }

}
