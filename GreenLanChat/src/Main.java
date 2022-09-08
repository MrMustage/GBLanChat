import java.io.IOException;
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
