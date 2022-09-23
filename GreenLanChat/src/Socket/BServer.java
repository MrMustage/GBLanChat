package Socket;

import java.io.File;
import java.io.IOException;
import java.lang.module.ResolutionException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BServer {

    private static final String killString = "kill";
    private static int id = 0;
    private static final int port = 5454;
    private static String lastMSG = null;
    private static List<SelectionKey> userList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(256);

        System.out.println("server started at port: " + port);
        while (true) {

            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                if (key.isAcceptable()) {
                    register(selector, serverSocket);
                    key.attach(id);
                    id++;
                    userList.add(key);

                }

                if (key.isReadable()) {
                    ReadKeyHandling(buffer, key);
                    //answerWithEcho(buffer,key);
                }
                if (!key.isValid()) key.channel().close();
                iter.remove();
//                forwardMSG(buffer,scanKeysList(2),new Message(1,"work"));
            }
//    public static SelectionKey scanKeysList(int userID) {
//        for (SelectionKey sk : userList) {
//            System.out.println();
//            System.out.println("channel: " + sk.channel().toString() + "userID: " + sk.attachment());
//
//            if (sk.attachment().equals(userID)) {
//                System.out.println(sk.channel().isOpen());
//                System.out.println(sk.isWritable());
//                System.out.println("found");
//                return sk;
//            }
//        }
//        return null;
//    }

        }
    }


    public static void forwardMSG(ByteBuffer buffer, SelectionKey key, Message msg) throws IOException {
        if(key == null){
            return;
        }
        System.out.println("forwardMSG");

        SocketChannel client = (SocketChannel) key.channel();

        buffer.flip();
        client.write(ByteBuffer.wrap((msg.getPayload() + " - " + msg.getDateTime().getBytes()).getBytes()));
        //msg.setLocalTimeOBJ(LocalDateTime.now());
        System.out.println(msg.getPayload() + " - " + msg.getDateTime());
        buffer.clear();
    }
    public static void ReadKeyHandling(ByteBuffer buffer, SelectionKey key) throws IOException {
        System.out.println("ReadKeyHandling");
        SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);
        String pure = new String(buffer.array()).trim();
        if (pure.equals(lastMSG)) {
            client.write(ByteBuffer.wrap("Kicked from server: spam".getBytes(StandardCharsets.UTF_8)));
            System.out.println("Kicked from server: spam");
            client.socket().close();
            return;
        }
        if (pure.equals("-1")) client.close();
        buffer.flip();
        client.write(buffer);
        Message msg = new Message(new String(buffer.array()));

        //msg.setLocalTimeOBJ(LocalDateTime.now());
        System.out.println(msg.getPayload() + " - " + msg.getDateTime());
        buffer.clear();
        lastMSG = pure;
    }

    private static void answerWithEcho(ByteBuffer buffer, SelectionKey key) throws IOException {

        SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);
        if (new String(buffer.array()).trim().equals(killString)) {
            client.close();
            System.out.println("Not accepting client messages anymore");
        } else {
            buffer.flip();
            client.write(buffer);
            Message msg = new Message(new String(buffer.array()));
            msg.setLocalTimeOBJ(LocalDateTime.now());
            System.out.println(msg.getPayload() + " - " + msg.getDateTime());
            buffer.clear();
        }
    }

    private static void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {
        System.out.println("register");
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);

        client.register(selector, SelectionKey.OP_READ);
    }

    //public static SelectionKey findByID(int i){return };
    public static Process start() throws IOException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = BServer.class.getCanonicalName();
        ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className);
        return builder.start();
    }

    public static void sendMSG(Message msg) {

    }

    public static SelectionKey scanKeysListByID(int userID) {
        for (SelectionKey sk : userList) {
            System.out.println();
            System.out.println("channel: " + sk.channel().toString() + "userID: " + sk.attachment());

            if (sk.attachment().equals(userID)) {
                System.out.println(sk.channel().isOpen());
                System.out.println(sk.isWritable());
                System.out.println("found");
                return sk;
            }
        }
        return null;
    }

}
