package ncu.sw.gameServer;

import ncu.sw.gameUtility.Cmd;

import java.io.*;
import java.util.Calendar;
import java.util.Timer;


public class UDPBroadCastClient {
    private UdpServerThread udpServerThread;
    private  Cmd cmd;
    private Timer sendTimer;
    private final int interval = 200;
    private static UDPBroadCastClient ourInstance = new UDPBroadCastClient();
    public static UDPBroadCastClient getInstance() {
        return ourInstance;
    }
    private  UDPBroadCastClient() {
        cmd = new Cmd();
        sendTimer = new Timer();
    }
    public void startUDPBroadcast(){
        try {
            udpServerThread =  new UdpServerThread(this.cmd);
            sendTimer.schedule(udpServerThread, Calendar.getInstance().getTime(),interval);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setCmd(Cmd cmd) {
        this.cmd = cmd;
    }

}