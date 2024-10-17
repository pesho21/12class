import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private final String host;
    private final int port;
    private String username;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void execute() {
        try {
            Socket socket = new Socket(host, port);

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();
        } catch (IOException ex) {
            System.out.println("Error connecting to the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    void setUsername(String username) {
        this.username = username;
    }

    String getUsername() {
        return this.username;
    }

    public static void main(String[] args) {
        if (args.length < 2) return;
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Client client = new Client(host, port);
        client.execute();
    }
}
