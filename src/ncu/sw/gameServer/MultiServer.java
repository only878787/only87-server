package ncu.sw.gameServer;

/**
 * Created by NiHao on 2016/10/18.
 */

import java.net.*;
import  java.io.*;
import java.io.IOException;
import java.util.*;

public class MultiServer implements Runnable{
    ServerSocket welcomeSocket = null ;
    boolean      isStopped    = false;
    int          serverPort ;
    InputStream input  ;
    OutputStream output ;
    int idCnt = 0;

    public MultiServer(int portNum){
        this.serverPort = portNum;
    }
    private void openWelcomeSocket() {
        try {
            this.welcomeSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 8787", e);
        }
    }

    public void run(){
        openWelcomeSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            /* Accept connection */
            try {
                clientSocket = this.welcomeSocket.accept();
                System.out.println("Addr : "+ clientSocket.getInetAddress()
                        + " Port : " + clientSocket.getPort() );
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            new Thread(
                    new WorkerThread(clientSocket, ++idCnt )
            ).start();
        }
        System.out.println("Server Stopped.") ;
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }
    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.welcomeSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
}
