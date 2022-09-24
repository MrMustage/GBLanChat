package Socket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Message {
    private int type;
    private String payload;
    LocalDateTime DateTime;

    private int toID;
    final private DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public Message(Message message){
        this.type=message.getType();
        this.payload=message.getPayload();
        this.DateTime=message.getDateTimeOBJ();
        this.toID = message.getToID();
    }
    public Message(int type,String payload){
        this.type=type;
        this.payload=payload;
        this.DateTime=LocalDateTime.now();
    }
    public Message(int type,String payload,LocalDateTime DateTime, int toID){
        this.type=type;
        this.payload=payload;
        this.DateTime=DateTime;
        this.toID = toID;
    }


    public void setType(int type){this.type = type;}
    public int getType(){return this.type;}
    public void setPayload(String payload){this.payload = payload;}
    public String getPayload(){return payload;}
    public void setDateTime(String DateTime){this.DateTime= LocalDateTime.parse(DateTime,DateFormat);}
    public String getDateTime(){return this.DateTime.format(DateFormat);}
    public void setDateTimeOBJ(LocalDateTime DateTime){this.DateTime=DateTime;}
    public LocalDateTime getDateTimeOBJ(){return this.DateTime;}
    public void setLocalTimeOBJ(LocalDateTime DateTime){this.DateTime=DateTime;}

    public int getToID (){
        return this.toID;
    }
    public void setToID(int i){
        this.toID = i;
    }
    @Override
    public String toString(){return this.type+","+this.payload+","+this.DateTime.format(DateFormat)+","+toID + ",";}
    public Message(String message){
        String[] info = message.split(",");
        this.type=Integer.valueOf(info[0]);
        this.payload=info[1];
        this.DateTime=LocalDateTime.parse(info[2],DateFormat);
        this.toID = Integer.parseInt(info[3]);
    }
}
