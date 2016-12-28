package ncu.sw.gameServer;
import ncu.sw.TCPSM.TCPMultiServer;
import java.io.IOException;

public class Main {
    public static void main(String[] args)  throws IOException{
        System.out.println("server main");
        ServerGameController.getInstance();
        new Thread( TCPMultiServer.getInstance()).start();
    }
}