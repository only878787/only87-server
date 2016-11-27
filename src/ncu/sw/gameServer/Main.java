package ncu.sw.gameServer;

public class Main {
    public static void main(String[] args) {
        System.out.println("server main");
        //launch(args);
        ServerGameController gameController = new ServerGameController(100,20,20);

        MultiServer server = new MultiServer(9000);
        new Thread(server).start();
    }
}