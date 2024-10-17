import java.io.*;
import java.net.*;

class ClientHandler implements Runnable {
    private final Socket socket;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String username;
            while (true) {
                out.println("Enter your username: ");
                username = in.readLine();
                if (username == null) {
                    return;
                }

                if (Server.addUsername(username)) {
                    out.println("Username registered successfully.");
                    Server.broadcastMessage(username + " has joined the chat.");
                    break;
                } else {
                    out.println("Username is already taken. Try again.");
                }
            }

            Server.addClientHandler(this);

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("exit")) {
                    Server.removeUser(username, this);
                    socket.close();
                    break;
                }
                Server.broadcastMessage(username + ": " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}