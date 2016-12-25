package ncu.sw.TCPSM;

/**
 * Created by NiHao on 2016/10/18.
 */

import ncu.sw.UDPSM.UDPBroadCastClient;
import ncu.sw.gameServer.BulletTask;
import ncu.sw.gameServer.ServerGameController;
import ncu.sw.gameUtility.Player;

import java.io.DataInputStream;
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

            System.out.println("one client has left...");
        }
    }
    public void removeWorkerFromClientTable() {
        TCPMultiServer.getInstance().getClientTable().remove(
                new InetSocketAddress(clientSocket.getInetAddress(),clientSocket.getPort())
        );
        UDPBroadCastClient.getInstance().removeFromUDPTable(clientSocket.getInetAddress());
        ServerGameController.getInstance().removePlayer(clientSocket.getInetAddress());
        ServerGameController.getInstance().removePlayer( new InetSocketAddress(clientSocket.getInetAddress(),clientSocket.getPort()));

    }
    public void stringParsing( String str ) { // TURN 5
        String[] token = str.split(" ");
        switch ( token[0] ){
            case "TURN" :
                //TCPMultiServer.fakeCDC.updateDirection(id,Integer.valueOf(token[1]));
                ServerGameController.getInstance().playerMove(identity, Integer.parseInt(token[1]));
                break;
            case "GET" :
                //TCPMultiServer.fakeCDC.getItem( id );

                break;
            case "IDENTITY" :
                this.identity = token[1];
                ServerGameController.getInstance().playCreate(identity, clientSocket.getInetAddress());
                break;
            case "DISCONNECT" :
                // remove this player
                System.out.print("hihihi");
                isStopped = true;
                break;
            case "ATK" :
                int dir = Integer.valueOf( token[1] );
                System.out.println( "Dir : " + dir );
                for (Player p : ServerGameController.getInstance().getCmd().getPlayerArrayList()) {
                    if( p.getId().equals( identity ) && p.getScore() !=0 ){
                        Timer timer = ServerGameController.getInstance().getBulletTimerMap().get(p);
                        timer.schedule( new BulletTask(p.getPositionX(),p.getPositionY(), p.getScore()/2, dir) ,0, 250 );
                        p.setScore( p.getScore()/2 );
                        break;
                    }
                }

        }
    }
}
