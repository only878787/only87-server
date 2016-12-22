package ncu.sw.UDPSM;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by nwlabclub on 2016/12/21.
 */
public class UDPServerReceive extends Thread{
    private InetAddress address;
    private int port;
    private DatagramPacket packet;
    private DatagramSocket socket;
    private byte [] sayHi = new byte[1];
    private final  boolean check =true;


    public UDPServerReceive(int port) {
        try {
            socket = new DatagramSocket(port);
            packet = new DatagramPacket(sayHi, sayHi.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void run() {
            try {
                socket.receive(packet);
                System.out.print(packet.getData().toString() + "WOW! It do Work!\n");
                UDPBroadCastClient.getInstance().getUDPTable()
                        .add(new InetSocketAddress(packet.getAddress(),packet.getPort()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket.close();
    }
}
