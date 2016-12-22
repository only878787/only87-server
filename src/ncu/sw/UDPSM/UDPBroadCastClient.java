package ncu.sw.UDPSM;

import ncu.sw.TCPSM.TCPMultiServer;
import ncu.sw.gameUtility.Cmd;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.Vector;


public class UDPBroadCastClient {
    private UdpServerThread udpServerThread;
    private Cmd cmd;
    private Timer sendTimer;


    private final int interval = 50;
    private ArrayList<InetSocketAddress> UDPTable;
    private UDPServerReceive receive;

    private static UDPBroadCastClient ourInstance;
    public static UDPBroadCastClient getInstance() {
        if(ourInstance == null) {
            ourInstance = new UDPBroadCastClient();
        }
        return ourInstance;
    }
    public  void startFirstReceive(int port) {
         receive = new UDPServerReceive(port);
        receive.start();
    }
    private UDPBroadCastClient() {
        cmd = new Cmd();
        sendTimer = new Timer();
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
    public void startUDPBroadcast() {
        try {
            udpServerThread = new UdpServerThread(this.cmd);
            sendTimer.schedule(udpServerThread, Calendar.getInstance().getTime(), interval);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}