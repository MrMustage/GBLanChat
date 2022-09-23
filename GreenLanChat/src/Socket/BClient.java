package Socket;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class BClient {

    private static SocketChannel client;
    private static ByteBuffer buffer;
    private static BClient instance;

    public static BClient start() {
        if (instance == null)
            instance = new BClient();

        return instance;
    }

    public static void stop() throws IOException {
        client.close();
        buffer = null;
    }

    private BClient() {
        try {
            client = SocketChannel.open(new InetSocketAddress("10.0.0.18", 5454));
            buffer = ByteBuffer.allocate(256);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendMessage(String msg) {
        buffer = ByteBuffer.wrap(msg.getBytes());
        String response = null;
        try {
            client.write(buffer);
            buffer.clear();
            client.read(buffer);
            response = new String(buffer.array()).trim();
            System.out.println("response=" + response);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }

    public String readBuffer () throws IOException {
        String a = String.valueOf(client.read(buffer));
        return a;
    }
}
