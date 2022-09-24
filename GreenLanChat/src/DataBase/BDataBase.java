package DataBase;

import javax.print.DocFlavor;
import java.io.*;

public class BDataBase{

    public enum parameters {
        username,
        id,
    }
    private static BDataBase instance;
    private File path;

    private int id;


    public static BDataBase getInstance() throws IOException {
        if(instance == null){
            instance = new BDataBase();
        }
        return instance;
    }

    private BDataBase () throws IOException {
        this.path = new File("src/DataBase/DB.bdb");
        this.id = lastID();
    }

    private void append(String username) throws IOException {
        FileWriter fw = new FileWriter(path,true);
        BufferedWriter bw = new BufferedWriter(fw);

        if(isEmpty()) bw.write( username);
        else bw.write( "\r\n" + username);
        String line;


        bw.close();
        fw.close();
    }

    public void insert (String username) throws IOException {
        append(username + ":" + id + ":");
        id++;
    }
    public String select(parameters target /*the parameter we want*/,parameters byParameter /*the parameter to check the value*/,Object value /*the value we want*/) throws IOException {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        String[] parametersFromLine; //umm🤓... this is actually a 2D array of chars 🤔

        int targetIDValue = 0;
        switch (target){
            case username -> targetIDValue = 0;
            case id -> targetIDValue = 1;
        }

        int byParameterValue = 0;
        switch (byParameter){
            case username -> byParameterValue = 0;
            case id -> byParameterValue = 1;
        }


        while ((line = bufferedReader.readLine())!= null){
            parametersFromLine = line.split(":");
            if (parametersFromLine[byParameterValue].equals(value)){
                return parametersFromLine[targetIDValue];
            }
        }
        return "not found - Error 404";
    }

    public boolean isEmpty () throws IOException {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        if(bufferedReader.readLine()==null){
            fileReader.close();
            bufferedReader.close();
            return true;
        }
        fileReader.close();
        bufferedReader.close();
        return false;
    }

    private int lastID () throws IOException {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        while ((bufferedReader.readLine() != null)){
            id++;
        }
        return id;
    }



}
