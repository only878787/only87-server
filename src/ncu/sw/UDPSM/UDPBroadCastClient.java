package ncu.sw.UDPSM;

import ncu.sw.TCPSM.TCPMultiServer;
import ncu.sw.gameUtility.Cmd;

import java.io.*;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Timer;
import java.net.InetSocketAddress;
import java.util.ArrayList;


public class UDPBroadCastClient {



    private UDPListenThread udpListenThread;

    private final int interval = 50;
    private ArrayList<InetSocketAddress> UDPTable;

    private static UDPBroadCastClient ourInstance;

    public static UDPBroadCastClient getInstance() {
        if(ourInstance == null) {
            ourInstance = new UDPBroadCastClient();
        }
        return ourInstance;
    }
    private UDPBroadCastClient() {
        UDPTable = new ArrayList<InetSocketAddress>();
    }
    public ArrayList<InetSocketAddress> getUDPTable() {
       return  UDPTable;
    }
    public void removeFromUDPTable(InetAddress address) {
        System.out.print("remove has been called ");
        for (int i =0;i<UDPTable.size(); i++) {
            if(UDPTable.get(i).getAddress().equals(address)) {
                UDPTable.remove(i);
            }
        }
    }
    public void startUDPBroadcast(int port) {
        System.out.print("!!");
        udpListenThread = new UDPListenThread(port);
        udpListenThread.start();
        try {
            new Timer().schedule( new UdpServerThread( udpListenThread.getSocket() ), Calendar.getInstance().getTime(), interval);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}