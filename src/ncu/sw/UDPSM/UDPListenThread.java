package ncu.sw.UDPSM;
import java.net.*;
/**
 * Created by Kevin on 2016/12/22.
 */
public class UDPListenThread extends  Thread {
    private boolean check = true;
    private DatagramSocket socket;
    private InetAddress address;
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
                byte[] buf = new byte[200];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                System.out.print( "I have to receive to go on.\n");
                socket.receive(packet);
                address = packet.getAddress();
                port = packet.getPort();
                InetSocketAddress socketAddress = new InetSocketAddress(address, port);
                String id =  new String (packet.getData(),"UTF-8");
                UDPBroadCastClient.getInstance().getUDPTable().put(id.trim(),socketAddress);
                System.out.print("it is first\n");
            }
                socket.close();
        } catch (Exception e) {
            e.printStackTrace();
           // check = false;
        }
    }
}