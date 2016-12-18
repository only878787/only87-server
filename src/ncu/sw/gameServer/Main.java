package ncu.sw.gameServer;

import ncu.sw.TCPCM.TCPMultiServer;
import ncu.sw.UDPCM.UDPBroadCastClient;
import ncu.sw.gameUtility.Cmd;
import ncu.sw.gameUtility.Player;

import java.io.IOException;
import java.net.InetAddress;

public class Main {
    public static void main(String[] args)  throws IOException{
        System.out.println("server main");
        //launch(args);
       // ServerGameController gameController = new ServerGameController(100,20,20);
        ServerGameController gameController = ServerGameController.getInstance();



        TCPMultiServer server = new TCPMultiServer(9000);
        server.initTCPServer();
        new Thread(server).start();

        InetAddress address = InetAddress.getByName("127.0.0.1");
        Player player = new Player(20,20,"Kevin",address);
        gameController.getCmd().getPlayerArrayList().add(player);

        System.out.print(gameController.getCmd().getPlayerArrayList());
        UDPBroadCastClient.getInstance().setCmd(gameController.getCmd());
        UDPBroadCastClient.getInstance().startUDPBroadcast();
    }
}