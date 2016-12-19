package ncu.sw.UDPSM;

import ncu.sw.TCPSM.TCPMultiServer;
import ncu.sw.gameServer.ServerGameController;
import ncu.sw.gameUtility.Cmd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.TimerTask;


public class UdpServerThread extends TimerTask {
    private Cmd cmd;
    private ArrayList<InetSocketAddress> clientIPTable;

    private InetAddress address;
    private int port;


    public UdpServerThread(Cmd cmd) throws IOException {
        //socket = new DatagramSocket();
        this.cmd = cmd;
        clientIPTable = new ArrayList<>();
     //   clientPortTable = new ArrayList<>();

      //  getClientIPTable();
      //  System.out.println(cmd);
    }
    public synchronized void run() {
        clientIPTable = TCPMultiServer.getInstance().getClientTable();

        cmd = ServerGameController.getInstance().getCmd();
       // clientIPTable = TCP.getInstance().getClientTable();
        try {
          //  System.out.print(clientIPTable.size());
            for(int i =0; i< clientIPTable.size();i++) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); //這邊應該算 encode 我們不採用message 的方式存取 ， 我們送整個資料給client
                ObjectOutputStream os = new ObjectOutputStream(outputStream);
                os.writeObject(cmd);
                byte[] data = outputStream.toByteArray();
                address = clientIPTable.get(i).getAddress();
                port = clientIPTable.get(i).getPort();
                DatagramPacket  packet = new DatagramPacket(data,  //這邊在送
                        data.length,address,port);
              //   System.out.print(address +" "+ port );
                DatagramSocket  socket = new DatagramSocket();
                socket.send(packet);
                //System.out.println("send!!!!");
                socket.close();
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}