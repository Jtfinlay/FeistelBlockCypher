import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Socket server
 */
public class SocketServer {

    private int _port;
    public List<User> _users;

    ServerSocket serverSocket;

    public SocketServer(int port) {
        _port = port;
        _users = new ArrayList<User>();

        _users.add(new User("Frank", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
        _users.add(new User("Ruth", "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"));
        _users.add(new User("Carl", "cccccccccccccccccccccccccccccccc"));
        _users.add(new User("Scott", "dddddddddddddddddddddddddddddddd"));
    }

    public void start(String dir) throws IOException {
        System.out.println("Starting server at port: " + _port);
        serverSocket = new ServerSocket(_port);

        // Listen for clients. Block until connection

        while (true) {
            System.out.println("Waiting for clients");
            Socket client = serverSocket.accept();
            System.out.println("Client has connected.");

            Thread thread = new Thread(new ClientHandler(this, client, dir));
            thread.start();
        }
    }

    public List<User> getUserList()
    {
        return _users;
    }

    /**
     * Main driver for server
     */
    public static void main(String[] args) {
        int portNumber = 16000;

        if (args.length != 1)
        {
            System.out.println("Error - Expect 1 param: Upload directory.");
            return;
        }

        /** Ensure download directory exists **/
        File f = new File(args[0]);
        if (!f.exists() || !f.isDirectory())
        {
            System.out.println("Given directory could not be found: " + args[0]);
            return;
        }

        try {
            SocketServer socketServer = new SocketServer(portNumber);
            socketServer.start(args[0]);
        } catch (IOException e) {
            System.out.println("Server error");
            e.printStackTrace();
        }
    }
}
