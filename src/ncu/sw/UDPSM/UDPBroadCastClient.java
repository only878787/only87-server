package ncu.sw.UDPSM;



import java.io.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.net.InetSocketAddress;


public class UDPBroadCastClient {



    private UDPListenThread udpListenThread;

    public  final int interval = 50;
    private HashMap<String,InetSocketAddress> UDPTable;

    private static UDPBroadCastClient ourInstance;

    public static UDPBroadCastClient getInstance() {
        if(ourInstance == null) {
            ourInstance = new UDPBroadCastClient();
        }
        return ourInstance;
    }
    private UDPBroadCastClient() {
        UDPTable = new HashMap<>();
    }
    public HashMap<String,InetSocketAddress> getUDPTable() {
       return  UDPTable;
    }
    public void startUDPListenThread(int port) {
        udpListenThread = new UDPListenThread(port) ;
        udpListenThread.start();
    }
    public void startUDPBroadcast(int port) {
        startUDPListenThread(port);
        try {
            new Timer().schedule( new UdpServerThread( udpListenThread.getSocket() ), Calendar.getInstance().getTime(), interval);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}