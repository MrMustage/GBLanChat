package Socket;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class BServer {

    private static final String killString = "kill";
    private static final int port = 5454;
    private static String lastMSG=null;
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("10.0.0.23", port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(256);

        System.out.println("server started at port: " + port );
        while (true) {

            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                    if (key.isAcceptable()) {
                        register(selector, serverSocket);
                    }

                if (key.isReadable()) {
                    ReadKeyHandling(buffer, key);
                    //answerWithEcho(buffer,key);
                }
                if (!key.isValid()) key.channel().close();
                iter.remove();

            }

        }
    }
    public static void ReadKeyHandling(ByteBuffer buffer,SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);
        String pure = new String(buffer.array()).trim();
        //client.close();
        if(pure.equals(lastMSG)){return;}
        if(pure.equals("-1"))client.close();
        buffer.flip();
        client.write(buffer);
        Message msg = new Message(new String(buffer.array()));
        //msg.setLocalTimeOBJ(LocalDateTime.now());
        System.out.println(msg.getPayload()+" - "+msg.getDateTime());
        buffer.clear();
        lastMSG=pure;
    }
    private static void answerWithEcho(ByteBuffer buffer, SelectionKey key) throws IOException {

        SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);
        if (new String(buffer.array()).trim().equals(killString)) {
            client.close();
            System.out.println("Not accepting client messages anymore");
        }
        else {
            buffer.flip();
            client.write(buffer);
            Message msg = new Message(new String(buffer.array()));
            msg.setLocalTimeOBJ(LocalDateTime.now());
            System.out.println(msg.getPayload()+" - "+msg.getDateTime());
            buffer.clear();
        }
    }
    private static void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }
    public static Process start() throws IOException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = BServer.class.getCanonicalName();
        ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className);
        return builder.start();
    }


}
