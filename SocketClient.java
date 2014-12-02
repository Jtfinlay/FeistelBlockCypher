import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {

    private String _dir;
    private Encryption _encrypter;
    private String _hostname;
    private int _port;
    Socket socketClient;

    private String _userID;
    private String _key;

    public SocketClient(String hostname, int port, String userID, String key, String directory) {
        _userID = userID;
        _key = key;
        _hostname = hostname;
        _port = port;
        _encrypter = new Encryption();
        _dir = directory;
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

    public boolean readFileResponse(String fileName) throws IOException
    {
        byte[] contents = _encrypter.decrypt(receive(), _key);

        if (new String(contents).startsWith(Statics.RESPONSE_FILEDNE))
        {
            System.out.println("File does not exist");
            return false;
        } else if (new String(contents).startsWith(Statics.RESPONSE_ACK)) {
            return readFileResponse(fileName);
        }

        File tmp = new File(fileName);
        File f = new File(_dir, tmp.getName());
        FileOutputStream fout = new FileOutputStream(f);
        fout.write(contents);
        fout.close();

        byte[] end = _encrypter.decrypt(receive(), _key);
        if (new String(end).startsWith(Statics.ACTION_FINISHED))
            System.out.println("File transfer successful.");
        else
            System.out.println("Something funky happened: " + new String(end));


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

        if (args.length != 3)
        {
            System.out.println("Error - Expect 3 params: UserID, Key, Download directory.");
            return;
        }

        /** Ensure download directory exists **/
        File f = new File(args[2]);
        if (!f.exists() || !f.isDirectory())
        {
            System.out.println("Given directory could not be found: " + args[2]);
            return;
        }

        Console console = System.console();
        SocketClient client = new SocketClient("localhost", 16000, args[0], args[1], args[2]);
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
                client.readFileResponse(input);
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
