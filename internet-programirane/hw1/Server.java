import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final Set<String> usernames = new HashSet<>();
    private static final Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Chat server started...");
        try (ServerSocket serverSocket = new ServerSocket(6969)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean addUsername(String userName) {
        synchronized (usernames) {
            if (usernames.contains(userName)) {
                return false;
            } else {
                usernames.add(userName);
                return true;
            }
        }
    }

    public static void removeUser(String username, ClientHandler clientHandler) {
        synchronized (usernames) {
            usernames.remove(username);
        }
        synchronized (clientHandlers) {
            clientHandlers.remove(clientHandler);
        }
        broadcastMessage(username + " has left the chat.");
    }

    public static void broadcastMessage(String message) {
        synchronized (clientHandlers) {
            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.sendMessage(message);
            }
        }
    }

    public static void addClientHandler(ClientHandler clientHandler) {
        synchronized (clientHandlers) {
            clientHandlers.add(clientHandler);
        }
    }
}