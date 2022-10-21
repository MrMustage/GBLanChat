package DataBase;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

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

    private void append(String st) throws IOException {
        FileWriter fw = new FileWriter(path,true);
        BufferedWriter bw = new BufferedWriter(fw);

        if(isEmpty()) bw.write( st);
        else bw.write( "\r\n" + st);

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

        int targetIDValue = enumToInt(target);
        int byParameterValue = enumToInt(byParameter);

        while ((line = bufferedReader.readLine())!= null){
            parametersFromLine = line.split(":");
            if (parametersFromLine[byParameterValue].equals(value)){
                return parametersFromLine[targetIDValue];
            }
        }
        return "not found - Error 404";
    }

    public void delete (parameters deleteBy, String targetValue) throws IOException {
        FileReader fr = new FileReader(this.path);
        LineNumberReader lnr = new LineNumberReader(fr);
        List<String> lines = Files.readAllLines(this.path.toPath());
        String[] parametersFromLine;

        String st;
        while ((st = lnr.readLine()) != null){
            parametersFromLine = st.split(":");
            if(parametersFromLine[enumToInt(deleteBy)].equals(targetValue)){
                lines.remove(lnr.getLineNumber()-1);
            }
        }
        Files.write(this.path.toPath(), lines);

//        while ((line = bufferedReader.readLine())!= null){
//            parametersFromLine = line.split(":");
//            if (parametersFromLine[enumToInt(deleteBy)].equals(targetValue)){
//                bufferedWriter.write("null:null:");
//
//                bufferedWriter.close();
//                bufferedReader.close();
//                fileReader.close();
//                fw.close();
//                return;
//            }
//        }
        return;
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

        String lastLine = "";
        String curruntLine="";
        while ((curruntLine=bufferedReader.readLine()) != null){
            lastLine=curruntLine;
        }
        String parameters[] = lastLine.split(":");
        return Integer.parseInt(parameters[1]);
    }

    private int enumToInt (parameters p){
        switch (p){
            case username ->  {return 0;}
            case id -> {return 1;}
        }
        return -1;
    }



}
