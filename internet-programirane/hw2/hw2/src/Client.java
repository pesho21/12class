import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.util.concurrent.Future;

public class Client {
    public static void main(String[] args) throws Exception {
        AsynchronousSocketChannel clientSocket = AsynchronousSocketChannel.open();
        Future<Void> future = clientSocket.connect(new java.net.InetSocketAddress("localhost", 6969));
        future.get();

        String filePath = "~/Downloads/message.txt";
        sendFile(clientSocket, expandPath(filePath));
    }

    private static String expandPath(String path) {
        if (path.startsWith("~")) {
            return System.getProperty("user.home") + path.substring(1);
        }
        return path;
    }

    private static void sendFile(AsynchronousSocketChannel clientSocket, String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(Paths.get(filePath).toFile());
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int bytesRead;
        while ((bytesRead = fis.read(buffer.array())) != -1) {
            buffer.limit(bytesRead);
            clientSocket.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    attachment.clear();
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                }
            });
            buffer.clear();
        }
        fis.close();
        clientSocket.close();
        System.out.println("File successfully sent to the server.");
    }
}
