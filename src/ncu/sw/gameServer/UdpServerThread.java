package ncu.sw.gameServer;

import ncu.sw.gameUtility.Cmd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class UdpServerThread extends Thread {

    private boolean check = true;
    private long seconds = 1000;
    private Cmd cmd;

    public UdpServerThread(Cmd cmd) throws IOException {
        //socket = new DatagramSocket();
        this.cmd = cmd;
    }
    public synchronized void setInterval(long seconds) {
        this.seconds = seconds;
    }
    public synchronized void run() {
        while (check) {
            try {
                for(int i= 0;i<cmd.getPlayerArrayList().size();i++) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ObjectOutputStream os = new ObjectOutputStream(outputStream);
                    os.writeObject(cmd);
                    byte[] data = outputStream.toByteArray();
                    DatagramPacket  packet = new DatagramPacket(data, data.length,cmd.getPlayerArrayList().get(i).getAddress(),5000);
                    DatagramSocket  socket = new DatagramSocket();
                    socket.send(packet);
                    socket.close();
                    System.out.print(data.length+ "   ");

                }
            } catch (SocketException e) {
                e.printStackTrace();
                check = false;
            } catch (IOException e) {
                e.printStackTrace();
                check = false;
            }
            try {
                sleep((long) (Math.random() * seconds));
            } catch (InterruptedException e) {
                e.printStackTrace();
                check = false;
            }
        }

    }
}