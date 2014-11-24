import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by James on 11/24/2014.
 */
public class ClientHandler implements Runnable {

    private Socket _client;

    public ClientHandler(Socket client) {
        _client = client;
    }

    @Override
    public void run() {
        try {
            readResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readResponse() throws IOException {
        String userInput;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(_client.getInputStream()));
        while ((userInput = stdIn.readLine()) != null) {
            System.out.println("CLIENT: "+userInput);
        }
    }

}
