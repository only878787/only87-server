package ncu.sw.gameServer;

/**
 * Created by NiHao on 2016/10/18.
 */

import ncu.sw.gameUtility.Cmd;
import ncu.sw.gameUtility.Coin;
import ncu.sw.gameUtility.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WorkerThread implements Runnable{
    private Socket clientSocket = null;
    private String clientStr = "";
    private ObjectOutputStream objOutToClient;
    private int id;

    public WorkerThread( Socket clientSocket, int id ) {
        this.clientSocket = clientSocket;
        this.id = id;
        try{
            objOutToClient = new ObjectOutputStream( clientSocket.getOutputStream() );
        }catch (IOException e){
            e.printStackTrace();
        }
        // first connection is here.
    }

    public void run() {
        try{
            DataInputStream inFromClient = new DataInputStream(clientSocket.getInputStream());
            Cmd t = createCmd();
            sendCmdToClient( t );
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

    public void sendCmdToClient( Cmd c ) {
        try{
            objOutToClient.writeObject( c );
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void cmd( String str ) {
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

    public Cmd createCmd() {
        Cmd c = new Cmd();
        c.getCoinArrayList().add( new Coin( 0, 0 ) );
        c.getCoinArrayList().add( new Coin( 1, 1 ) );
        c.getPlayerArrayList().add( new Player( 2, 2, "Alice" ) );
        c.getPlayerArrayList().add( new Player( 3, 3, "Bob" ) );
        return c;
    }
}
