package ncu.sw.TCPSM;



import ncu.sw.UDPSM.UDPBroadCastClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by NiHao on 2016/11/26.
 */
public class TCPMultiServer implements Runnable{
    protected ServerSocket welcomeSocket = null ;
    private boolean isStopped = false;
    private int serverPort;

    private ArrayList<InetAddress> clientIPTable;

    private ArrayList<InetSocketAddress> clientTable;
    private InputStream input;
    private OutputStream output;
    private int idCnt = 0;

    private static TCPMultiServer tcpServer = null ;

    private TCPMultiServer(int portNum){
        this.serverPort = portNum;
        initTCPServer();
    }

    private void initTCPServer(){
        try {
            welcomeSocket = new ServerSocket(this.serverPort);
            clientIPTable = new ArrayList<InetAddress>();
            clientTable = new ArrayList<InetSocketAddress>();
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort , e);
        }
    }

    public static TCPMultiServer getInstance(){
        if( tcpServer == null ){
            tcpServer = new TCPMultiServer( 9487 );
        }
        return tcpServer;
    }

    public void run(){
        while( !isStopped() ) {
            Socket clientSocket = null;
            /* Accept connection */
            try {
                clientSocket = this.welcomeSocket.accept();
                /*System.out.println("Addr : "+ clientSocket.getInetAddress()
                        + " Port : " + clientSocket.getPort() );*/

              //  UDPBroadCastClient.getInstance().startFirstReceive(5000);
                System.out.print("IP is " +clientSocket.getInetAddress()+"\n");
                UDPBroadCastClient.getInstance().startUDPBroadcast(5000);
                clientIPTable.add( clientSocket.getInetAddress() );
                clientTable.add( new InetSocketAddress(clientSocket.getInetAddress(), clientSocket.getPort()));
                //System.out.println("TCP Server " + clientSocket.getPort() +" " + clientSocket.getLocalPort());
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            //System.out.println("WorkerThread") ;
            new Thread(
                    new WorkerThread(clientSocket, ++idCnt )
            ).start();
        }
        System.out.println("Server Stopped.") ;
    }

    public ArrayList<InetAddress> getClientIPTable() {
        return clientIPTable;
    }


    public ArrayList<InetSocketAddress> getClientTable() {
        return clientTable;
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            if( welcomeSocket != null){
                this.welcomeSocket.close();
            }
            else{
                System.out.println("TCPMultiServer stop() : welcomeSocket is null") ;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
}
