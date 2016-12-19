package ncu.sw.gameServer;

import ncu.sw.TCPSM.TCPMultiServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args)  throws IOException{
        System.out.println("server main");
        //launch(args);
       // ServerGameController gameController = new ServerGameController(100,20,20);
        ServerGameController gameController = ServerGameController.getInstance();
        new Thread( TCPMultiServer.getInstance()).start();

        /*InetAddress address = InetAddress.getByName("127.0.0.1");
        Player player = new Player(20,20,"Kevin",address);
        gameController.getCmd().getPlayerArrayList().add(player);*/
        //System.out.print(gameController.getCmd().getPlayerArrayList());


    }
}