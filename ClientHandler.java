import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

/**
 * Created by James on 11/24/2014.
 */
public class ClientHandler implements Runnable {

    private Socket _client;
    private SocketServer _server;
    private Encryption _encrypter;
    private String _dir;

    public ClientHandler(SocketServer server, Socket client, String dir) throws IOException {
        _server = server;
        _client = client;
        _encrypter = new Encryption();
        _dir = new File(dir).getCanonicalPath();
    }

    @Override
    public void run() {
        try {
            while (true) {
                readResponse();
            }
        } catch (SocketException e) {
            System.out.println("Connect to client lost.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readResponse() throws IOException {
        DataInputStream in = new DataInputStream(_client.getInputStream());

        int length = in.readInt();
        if (length <= 0) return;

        byte[] message = new byte[length];
        in.readFully(message, 0, message.length);

        for (User u : _server.getUserList()) {
            byte[] decrypted = _encrypter.decrypt(message, u.key);
            if (new String(decrypted).startsWith(Statics.ACTION_AUTHENTICATE)) {
                /** AUTHENTICATE USER **/
                authenticateUser(u);
                return;
            } else if (new String(decrypted).startsWith(Statics.ACTION_SENDFILE)) {
                /** TRANSMIT FILE **/
                String fileName = new String(decrypted).substring(4);
                if (u.authenticated) {
                    transmitFile(u, fileName);
                } else {
                    System.out.println(u.name + " is not authenticated");
                    transmitDenied();
                }
                return;
            } else if (new String(decrypted).startsWith(Statics.ACTION_FINISHED)) {
                /** ALL DONE! **/
                u.authenticated = false;
                _client.close();
                return;
            }
        }
        System.out.println("Could not find key for client.");
        transmitDenied();
    }

    private void authenticateUser(User user) throws IOException {
        user.authenticated = true;
        System.out.println("Authenticated: " + user.name);

        byte[] msg = _encrypter.encrypt(Statics.RESPONSE_AUTHENTICATED, user.key);
        transmit(msg);
    }

    private void transmitFile(User user, String fileName) throws IOException {
        /** Transmit Acknowledgement **/
        transmit(_encrypter.encrypt(Statics.RESPONSE_ACK, user.key));

        /** Find file **/
        File f = new File(_dir, fileName);
        if (!f.exists() || f.isDirectory())
        {
            transmit(_encrypter.encrypt(Statics.RESPONSE_FILEDNE, user.key));
            return;
        }

        if (!f.getCanonicalPath().startsWith(_dir))
        {
            System.out.println("File not in uploading directory: " + f.getCanonicalPath());
            transmit(_encrypter.encrypt(Statics.RESPONSE_FILEDNE, user.key));
            return;
        }

        /** File size **/
        if (f.length() > Integer.MAX_VALUE - 25)
        {
            System.out.println(fileName + " is too large to send.");
            transmit(_encrypter.encrypt(Statics.RESPONSE_FILEDNE, user.key));
            return;
        }

        /** Send file **/
        FileInputStream fin = new FileInputStream(f);
        byte[] fileContent = new byte[(int)f.length()];
        fin.read(fileContent);
        fin.close();
        transmit(_encrypter.encrypt(fileContent, user.key));

        /** Transmit Done **/
        transmit(_encrypter.encrypt(Statics.ACTION_FINISHED, user.key));
    }

    private void transmitDenied() throws IOException {
        transmit(Statics.RESPONSE_DENIED.getBytes());
        _client.close();
    }

    private void transmit(byte[] message) throws IOException {
        DataOutputStream out = new DataOutputStream(_client.getOutputStream());
        out.writeInt(message.length);
        out.write(message);
    }

}
