package ncu.sw.UDPSM;

import ncu.sw.TCPSM.TCPMultiServer;
import ncu.sw.gameUtility.Cmd;

import java.io.*;
import java.util.Calendar;
import java.util.Timer;


public class UDPBroadCastClient {
    private UdpServerThread udpServerThread;
    private Cmd cmd;
    private Timer sendTimer;

    private final int interval = 50;
    private static UDPBroadCastClient ourInstance;

    public static UDPBroadCastClient getInstance() {
        if(ourInstance == null) {
            ourInstance = new UDPBroadCastClient();
        }
        return ourInstance;
    }

    private UDPBroadCastClient() {
        cmd = new Cmd();
        sendTimer = new Timer();
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