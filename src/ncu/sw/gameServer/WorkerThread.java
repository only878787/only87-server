package ncu.sw.gameServer;

/**
 * Created by NiHao on 2016/10/18.
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.*;
import java.net.Socket;

public class WorkerThread implements Runnable{
    Socket clientSocket = null;
    String clientStr = "";
    ObjectOutputStream objOutToClient;
    int id;
    public WorkerThread( Socket clientSocket, int id){
        this.clientSocket = clientSocket;
        this.id = id;
        try{
            objOutToClient = new ObjectOutputStream( clientSocket.getOutputStream() );
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void run(){
        try{
            /*BufferedReader inFromClient = new
                    BufferedReader(new InputStreamReader
                        (clientSocket.getInputStream()));*/
            DataInputStream inFromClient = new DataInputStream(clientSocket.getInputStream());

            while( true ){
                clientStr = inFromClient.readUTF();
                System.out.println( "FromClient "+id+" : " + clientStr );
                cmd( clientStr );
                if( clientStr.equals("bye") ){
                    System.out.println( clientStr );
                    break;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendToClient( String str){
        try{
            DataOutputStream outToClient = new DataOutputStream
                    (this.clientSocket.getOutputStream());
            outToClient.writeBytes( str + '\n' );
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void cmd(String str){
        String[] token = str.split(" ");
        /*if( token[0].equals("GET") ){
            //System.out.println("call get func");
            getTreasure( token[1] );
        }
        else if(token[0].equals("RELEASE")){
            //System.out.println("call get func");
            releaseTreasure( token[1] );
        }*/
    }
}
