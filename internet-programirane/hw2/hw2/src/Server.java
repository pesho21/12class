import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws IOException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open()
                .bind(new InetSocketAddress("localhost", 6969));

        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel clientSocket, Void attachment) {
                serverSocketChannel.accept(null, this);
                threadPool.submit(() -> handleClient(clientSocket));
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace();
            }
        });

        while (true) {
        }
    }

    private static void handleClient(AsynchronousSocketChannel clientSocket) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            String fileName = "uploaded_" + System.currentTimeMillis() + ".txt";
            FileOutputStream fos = new FileOutputStream(Paths.get(fileName).toFile());

            while (clientSocket.read(buffer).get() != -1) {
                buffer.flip();
                fos.write(buffer.array(), 0, buffer.limit());
                buffer.clear();
            }
            fos.close();
            clientSocket.close();
            System.out.println("File " + fileName + " received and saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
