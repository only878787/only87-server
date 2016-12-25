package ncu.sw.UDPSM;

import java.net.*;
import java.util.Calendar;
import java.util.Timer;

/**
 * Created by Kevin on 2016/12/22.
 */
public class UDPListenThread extends  Thread {
    private boolean check = true;
    private DatagramSocket socket;
    private InetAddress address;
    private InetSocketAddress socketAddress;
    private int port;
    public UDPListenThread(int port) {
        try {
            socket = new DatagramSocket(port);
            this.port = port;
        } catch (SocketException e) {

        }
    }
    public DatagramSocket getSocket() {
        return this.socket;
    }
    public synchronized void run() {
        try {
            while(check) {
                byte[] buf = new byte[2];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                System.out.print( "I have to receive to go on.\n");
                socket.receive(packet);
                address = packet.getAddress();
                port = packet.getPort();
                UDPBroadCastClient.getInstance().getUDPTable().add(new InetSocketAddress(address, port));
                System.out.print("it is first\n");
                }
                socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            check = false;
        }
    }
}
