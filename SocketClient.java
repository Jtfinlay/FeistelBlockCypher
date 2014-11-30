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

    public boolean readAuthenticationResponse() throws IOException
    {
        byte[] message = receive();
        if (new String(message).startsWith(Statics.RESPONSE_DENIED))
            return false;

        byte[] msg = _encrypter.decrypt(message, _key);

        if (new String(msg).startsWith(Statics.RESPONSE_AUTHENTICATED))
            return true;

        return false;
    }

    public void sendUserID() throws IOException {
        transmit(_encrypter.encrypt(Statics.ACTION_AUTHENTICATE + _userID, _key));
    }

    public void sendFileName(String fileName) throws IOException {
        transmit(_encrypter.encrypt(Statics.ACTION_SENDFILE+fileName, _key));
    }

    public void sendComplete() throws IOException
    {
        transmit(_encrypter.encrypt(Statics.ACTION_FINISHED, _key));
    }

    public boolean readFileResponse() throws IOException
    {
        byte[] contents = _encrypter.decrypt(receive(), _key);

        if (new String(contents).startsWith(Statics.RESPONSE_FILEDNE))
        {
            System.out.println("File does not exist");
            return false;
        }

        File f = new File("hi.txt"); // TODO - Proper file name
        FileOutputStream fout = new FileOutputStream(f);
        fout.write(contents);
        fout.close();

        System.out.println("File transfer successful.");
        return true;
    }

    private void transmit(byte[] message) throws IOException {
        DataOutputStream out = new DataOutputStream(socketClient.getOutputStream());
        out.writeInt(message.length);
        out.write(message);
    }

    private byte[] receive() throws IOException {
        DataInputStream in = new DataInputStream(socketClient.getInputStream());
        byte[] message = new byte[in.readInt()];
        in.readFully(message, 0, message.length);
        return message;
    }

    public static void main(String args[]) {
        Console console = System.console();
        SocketClient client = new SocketClient("localhost", 16000);
        try {
            client.connect();
            client.sendUserID();

            /** AUTHENTICATE **/
            if (!client.readAuthenticationResponse())
            {
                System.out.println("Could not authenticate.");
                return;
            }
            System.out.println("Authentication successful.");

            String input;

            System.out.println("Please enter a file name, or 'q' to quit.");
            while (!(input = console.readLine()).equals("q"))
            {
                client.sendFileName(input);
                client.readFileResponse();
                System.out.println("\nPlease enter a file name, or 'q' to quit.");
            }


            client.sendComplete();



        } catch (UnknownHostException e) {
            System.err.println("Host unknown.");
        } catch (IOException e) {
            System.err.println("Cannot establish connection.");
        }
    }

}
