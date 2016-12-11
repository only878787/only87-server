package ncu.sw.gameServer;

import ncu.sw.gameUtility.Cmd;

import java.io.*;


public class UDPBroadCastClient {

    private UdpServerThread udpServerThread;
    private  Cmd cmd;
    private static UDPBroadCastClient ourInstance = new UDPBroadCastClient();
    public static UDPBroadCastClient getInstance() {
        return ourInstance;
    }
    private  UDPBroadCastClient() {
    }
    private UDPBroadCastClient( Cmd cmd) {
        this.cmd =cmd;
        startUDPBroadcast();
        System.out.println("Server gogo");
    }
    public void startUDPBroadcast(){
        try {
            udpServerThread =  new UdpServerThread(this.cmd);
            udpServerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Cmd getCmd() {
        return this.cmd;
    }
    public void setCmd(Cmd cmd) {
        this.cmd = cmd;
    }
    public synchronized void SetInterval(long time) {
        udpServerThread.setInterval(time);
    }

}