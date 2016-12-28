package ncu.sw.TCPSM;

/**
 * Created by NiHao on 2016/10/18.
 */

import ncu.sw.UDPSM.UDPBroadCastClient;
import ncu.sw.gameServer.BulletTask;
import ncu.sw.gameServer.ServerGameController;
import ncu.sw.gameUtility.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;

public class WorkerThread implements Runnable{
    private Socket clientSocket = null;
    private int id;
    private String identity;
    private boolean isStopped = false;

    public WorkerThread(Socket clientSocket, int id ) {
        this.clientSocket = clientSocket;
        this.id = id;
    }

    public void run() {
        String clientStr;
        try{
            DataInputStream inFromClient = new DataInputStream(clientSocket.getInputStream());
            while( !isStopped ){
                clientStr = inFromClient.readUTF();
                //System.out.println( clientStr );
                stringParsing( clientStr );
            }
        }catch (IOException e){
            removeWorkerFromClientTable();
            System.out.println(identity + " has left...");
        }
    }
    public void removeWorkerFromClientTable() {
        InetSocketAddress socketAddress =  new InetSocketAddress(clientSocket.getInetAddress(),clientSocket.getPort());
        TCPMultiServer.getInstance().getClientTable().remove(
               socketAddress
        );
        String checkID = ServerGameController.getInstance().removePlayer(socketAddress);
        System.out.print("FUCKYOU " +checkID);
        UDPBroadCastClient.getInstance().getUDPTable().remove(checkID);
        System.out.println(checkID + "is left from UDP");

        for (int i = 0;i<ServerGameController.getInstance().getCmd().getPlayerArrayList().size(); i ++) {
            System.out.println("Id" + ServerGameController.getInstance().getCmd().getPlayerArrayList().get(i).getId());
        }
    }
    public void stringParsing( String str ) { // TURN 5
        String[] token = str.split(" ");
        switch ( token[0] ){
            case "TURN" :
                //TCPMultiServer.fakeCDC.updateDirection(id,Integer.valueOf(token[1]));
                ServerGameController.getInstance().playerMove(identity, Integer.parseInt(token[1]));
               // System.out.println(identity + "move ");
                break;
            case "GET" :
                //TCPMultiServer.fakeCDC.getItem( id );
                break;
            case "IDENTITY" :
                this.identity = token[1];
                try {
                    DataOutputStream outToClient = new DataOutputStream( clientSocket.getOutputStream() );
                    if(!ServerGameController.getInstance().playCreate(identity,  new InetSocketAddress(clientSocket.getInetAddress(),clientSocket.getPort()))) {
                        outToClient.writeUTF("IdentityNotOK");
                        removeWorkerFromClientTable();
                        isStopped = true;
                    }else{
                        outToClient.writeUTF("IdentityOK");
                    }
                }catch ( IOException e ){
                    System.out.println(  "stringParsing : "+ e);
                }
                break;
            case "DISCONNECT" :
                // remove this player
                //System.out.print("hihihi");
                isStopped = true;
                break;
            case "ATK" :
                int dir = Integer.valueOf( token[1] );
               // System.out.println( "Dir : " + dir );
                for (Player p : ServerGameController.getInstance().getCmd().getPlayerArrayList()) {
                    if( p.getId().equals( identity ) && p.getScore() !=0 ){
                        Timer timer = ServerGameController.getInstance().getBulletTimerMap().get(p);
                        timer.schedule( new BulletTask(p, dir) ,0, UDPBroadCastClient.getInstance().interval );
                        if(p.getScore() != 1) {
                            p.setScore( p.getScore()/2 );
                        }
                        break;
                    }
                }
        }
    }
}
