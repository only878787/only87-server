package ncu.sw.TCPCM;

import ncu.sw.TCPCM.WorkerThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
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
    private InputStream input;
    private OutputStream output;
    private int idCnt = 0;

    public TCPMultiServer(int portNum){
        this.serverPort = portNum;

    }
    public void initTCPServer(){
        try {
            welcomeSocket = new ServerSocket(this.serverPort);
            clientIPTable = new ArrayList<InetAddress>();
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort , e);
        }
    }

    public void run(){
        while( !isStopped() ) {
            Socket clientSocket = null;
            /* Accept connection */
            try {
                clientSocket = this.welcomeSocket.accept();
                /*System.out.println("Addr : "+ clientSocket.getInetAddress()
                        + " Port : " + clientSocket.getPort() );*/
                clientIPTable.add( clientSocket.getInetAddress() );
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
