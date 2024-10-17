import java.io.*;
import java.net.*;

class ReadThread extends Thread {
    private BufferedReader in;
    private Socket socket;
    private Client client;

    public ReadThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = in.readLine();
                System.out.println(response);
            } catch (IOException e) {
                System.out.println("Connection closed.");
                break;
            }
        }
    }
}