import java.io.IOException;
import java.util.Scanner;

import Socket.*;

public class Main {


    public static void main(String[] args) throws IOException {

        BClient bc  = BClient.start();
        Scanner sc = new Scanner(System.in);

        Runnable readBuffers = new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        System.err.println(bc.readBuffer());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        while (true){
            bc.sendMessage(new Message(1,sc.next()).toString());
        }
    }
}
