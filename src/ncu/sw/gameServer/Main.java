package ncu.sw.gameServer;

public class Main {
    public static void main(String[] args) {
        System.out.println("server main");
        //launch(args);
      // ServerGameController server = new ServerGameController(100,20,20);
        Initial a = new Initial(10,100,10);
        MultiServer server = new MultiServer(9000);
        new Thread(server).start();
    }
}