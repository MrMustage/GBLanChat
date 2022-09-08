import java.io.IOException;
import java.net.*;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Scanner;

import Socket.*;

public class Main {


    public static void main(String[] args) throws IOException {

        BClient bc  = BClient.start();
        Scanner sc = new Scanner(System.in);

        while (true){
            bc.sendMessage(new Message(1,sc.next()).toString());
        }
    }
}
