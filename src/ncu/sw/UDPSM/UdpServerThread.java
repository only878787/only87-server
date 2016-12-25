package ncu.sw.UDPSM;


import ncu.sw.gameServer.ServerGameController;
import ncu.sw.gameUtility.Cmd;
import ncu.sw.gameUtility.Coin;
import ncu.sw.gameUtility.Player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.TimerTask;
public class UdpServerThread extends TimerTask {
    private Cmd cmd;
    private InetAddress address;
    private int port;
    private DatagramSocket socket;
    private DatagramPacket packet;

    public UdpServerThread(DatagramSocket socket) throws IOException {
        this.cmd = ServerGameController.getInstance().getCmd();
        this.socket = socket;
    }
    public synchronized void run() {
        try {
           for(int i = 0; i<UDPBroadCastClient.getInstance().getUDPTable().size() ; i ++) {
               address = UDPBroadCastClient.getInstance().getUDPTable().get(i).getAddress();
               port = UDPBroadCastClient.getInstance().getUDPTable().get(i).getPort();
               ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); //這邊應該算 encode 我們不採用message 的方式存取 ， 我們送整個資料給client
               ObjectOutputStream os = new ObjectOutputStream(outputStream);
               os.writeObject(cmd);
               byte[] data = outputStream.toByteArray();
               packet = new DatagramPacket(data,  //這邊在送
                        data.length, address, port);
               socket.send(packet);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}