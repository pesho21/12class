import java.io.*;
import java.net.*;
import java.util.Scanner;

class WriteThread extends Thread {
    private PrintWriter out;
    private final Socket socket;
    private final Client client;
    private Scanner scanner;

    public WriteThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        client.setUsername(username);
        out.println(username);

        String message;
        while (true) {
            message = scanner.nextLine();
            out.println(message);

            if (message.equalsIgnoreCase("exit")) {
                break;
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}