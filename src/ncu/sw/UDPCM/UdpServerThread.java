package ncu.sw.UDPCM;

import ncu.sw.gameUtility.Cmd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.TimerTask;


public class UdpServerThread extends TimerTask {
    private Cmd cmd;
    public UdpServerThread(Cmd cmd) throws IOException {
        //socket = new DatagramSocket();
        this.cmd = cmd;
      //  getClientIPTable();
      //  System.out.println(cmd);
    }
    public void getClientIPTable() {
        for(int i =0;i<cmd.getPlayerArrayList().size(); i++) {
            System.out.println(cmd.getPlayerArrayList().get(i).getAddress());
        }
    }
    public synchronized void run() {
        try {
            for(int i= 0;i<cmd.getPlayerArrayList().size();i++) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); //這邊應該算 encode 我們不採用message 的方式存取 ， 我們送整個資料給client
                ObjectOutputStream os = new ObjectOutputStream(outputStream);
                os.writeObject(cmd);
                byte[] data = outputStream.toByteArray();

                DatagramPacket  packet = new DatagramPacket(data,  //這邊在送
                        data.length,cmd.getPlayerArrayList().get(i).getAddress(),5000);
                System.out.print(cmd.getPlayerArrayList().get(i).getAddress() +" " );
                DatagramSocket  socket = new DatagramSocket();
                socket.send(packet);
                System.out.println("send!!!!");
                socket.close();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}