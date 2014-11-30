import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by James on 11/24/2014.
 */
public class ClientHandler implements Runnable {

    private Socket _client;
    private SocketServer _server;
    private Encryption _encrypter;

    private static final String ACTION_AUTHENTICATE = "USER";
    private static final String ACTION_SENDFILE = "FILE";


    public ClientHandler(SocketServer server, Socket client) {
        _server = server;
        _client = client;
        _encrypter = new Encryption();
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
        DataInputStream in = new DataInputStream(_client.getInputStream());

        int length = in.readInt();
        if (length > 0)
        {
            byte[] message = new byte[length];
            in.readFully(message, 0, message.length);

            for (User u : _server.getUserList())
            {
                String decrypted = _encrypter.decrypt(message, u.key);
                if (decrypted.startsWith(ACTION_AUTHENTICATE))
                {
                    authenticateUser(u);
                }  else if (decrypted.startsWith(ACTION_SENDFILE))
                {
                    String fileName = decrypted.substring(4);
                    if (u.authenticated)
                    {
                        transmitFile(fileName);
                    } else {
                        System.out.println(u.name + " tried to access " + fileName +
                                " but is not authenticated");
                    }
                }

            }
        }
    }

    private void authenticateUser(User user)
    {
         user.authenticated = true;
    }

    private void transmitFile(String fileName)
    {

    }

}
