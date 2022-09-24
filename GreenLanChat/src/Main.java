import java.io.IOException;
import java.util.Scanner;
import java.util.WeakHashMap;

import DataBase.BDataBase;
import Socket.*;

public class Main {


    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        System.out.println(BDataBase.getInstance().select(BDataBase.parameters.id, BDataBase.parameters.username,"atan"));


    }
}
