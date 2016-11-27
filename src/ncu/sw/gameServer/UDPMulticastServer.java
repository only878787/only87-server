package ncu.sw.gameServer;

import ncu.sw.gameUtility.Cmd;
import ncu.sw.gameServer.ObjectSizeFetcher;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.lang.instrument.Instrumentation;

/**
 * Created by NiHao on 2016/11/26.
 */
public class UDPMulticastServer {

    private DatagramSocket socket = null;
    private boolean isStopped = false;
    private int udpServerPort;

    public UDPMulticastServer( int port ) throws IOException {
        udpServerPort = port;
        socket = new DatagramSocket( udpServerPort );
    }

    /*public void sendCmdToClient( Cmd c ) {
        try{
            //DatagramPacket packet = new DatagramPacket( c, ObjectSizeFetcher.getObjectSize(c) );
        }catch (IOException e){
            e.printStackTrace();
        }
    }*/
}
