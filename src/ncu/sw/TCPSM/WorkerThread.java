package ncu.sw.TCPSM;

/**
 * Created by NiHao on 2016/10/18.
 */

import ncu.sw.gameServer.ServerGameController;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

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
                new InetSocketAddress(clientSocket.getInetAddress(),clientSocket.getLocalPort())
        );
        ServerGameController.getInstance().removePlayer(clientSocket.getInetAddress());
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
        }
    }
}
