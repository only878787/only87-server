package ncu.sw.gameServer;

import ncu.sw.gameUtility.*;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Arson on 2016/11/1.
 */
public class ServerGameController {
   
    private int totalCoin;
    private int totalItem;
    private int totalObstacle;
    private final int mapWidth = 5000;
    private final int mapHeight= 3000;
    private final int overlayTimes = 10;
    private Random ran;
    private Cmd cmd;


    public final static int TURNEAST = 0;
    public final static int TURNSOUTH = 1;
    public final static int TURNNORTH = 2;
    public final static int TURNWEST = 3;
    public final static int GET = 4;
    public final static int TURNEASTNORTH = 5;
    public final static int TURNEASTSOUTH = 6;
    public final static int TURNWESTNORTH = 7;
    public final static int TURNWESTSOUTH = 8;
    public final static int DISCONNECT = 9;
    private static ServerGameController ourInstance ;

    private HashMap<Integer, Integer> moveXMap,moveYMap;

    public static ServerGameController getInstance() {
        if(ourInstance == null) {
          ourInstance  = new ServerGameController(200,20,30);
        }
        return ourInstance;
    }
    private ServerGameController(int totalCoin, int totalItem, int totalObstacle) {
        this.totalCoin = totalCoin;
        this.totalItem = totalItem;
        this.totalObstacle = totalObstacle;
        cmd = new Cmd();

        ran = new Random();
        gameInit();

    }
    public Cmd getCmd() {
        return  this.cmd;
    }
    public boolean playCreate(String id, InetAddress ipAddress) {
        //String address = ipAddress.toString();
        System.out.println(id +" is created.");
        if(isSameId(id)) {
            return  false;
        }
        else {
            Player player = new Player(0, 0, id, ipAddress);
            int[] position = this.randomPosition(player);
            player.setPosition(position[0],position[1]);
            cmd.getPlayerArrayList().add(player);
            return  true;
        }
    }
    private boolean isSameId(String id) {
        for(int i=0;i<cmd.getPlayerArrayList().size();i++) {
            Player player = cmd.getPlayerArrayList().get(i);
            if(id.equals(player.getId())) {
                return true;
            }
        }
        return false;
    }
    private int [] randomPosition(GameObject object) {
        int canDrawMapWidth = mapWidth-2*object.getWidth(); // random 範圍
        int canDrawMapHeight = mapHeight-2*object.getHeight();
        int  x = randomCoordinate(0,canDrawMapWidth,ran); // 0 ~ canDrawMapWidth random一個值
        int  y = randomCoordinate(0,canDrawMapHeight,ran);
        object.setPosition(x,y);
        while(isAllOverlay(object)) { //重複 直到不重複
            x = randomCoordinate(0,canDrawMapWidth,ran);
            y = randomCoordinate(0,canDrawMapHeight,ran);
            object.setPosition(x,y);
        }
        int []position = new int[2];
        position[0] = x;
        position[1] = y;
        return position; // 回傳其object的
    }
    private boolean isOverlay(GameObject a, GameObject b) {
        GameObject circleBuf;
        GameObject rectangleBuf;
        if( a.getAttribute() == b.getAttribute() ) { //they are the same shape.
            if( a.getAttribute()== 0 ) { // circle
                if(calcDistance( a.getPositionX(), a.getPositionY(), b.getPositionX(), b.getPositionY() )
                        >= a.getWidth()+b.getWidth() ) {
                    return false;
                }
                else {
                    System.out.println(" circle same shape overlay");
                    return true;
                }
            }
            else {// x1-x2 絕對值 比w1/2+w2/2 大 則沒碰撞 // 兩個矩形
                if((Math.abs(a.getPositionX()-b.getPositionX())>=(a.getWidth()/2 + b.getWidth()/2)) &&
                        (Math.abs(a.getPositionY()-b.getPositionY())>= (a.getHeight()/2 + b.getHeight()/2))) {
                    return  false;
                }
                else {
                    System.out.println("rectangle same shape overlay");
                    return true;
                }
            }
        }
        else {//circle is a.  rectangle is b.
            if(a.getAttribute() == 0) {
                circleBuf = a;
                rectangleBuf = b;
            }
            else {
                circleBuf = b;
                rectangleBuf = a;
            }
            if(isSameQuadrant(circleBuf,rectangleBuf)) {
                if(findClosetCalcDistance(circleBuf,rectangleBuf)>=circleBuf.getHeight()) {
                    return false;

                }
                else {
                    System.out.println("findCloseDistance and not same shape overlay");
                    return  true;
                }
            }
            else if(((Math.abs(circleBuf.getPositionX()-rectangleBuf.getPositionX())
                    >=(circleBuf.getWidth() + rectangleBuf.getWidth()/2))
                    || (Math.abs(circleBuf.getPositionY()-rectangleBuf.getPositionY())
                    >= (circleBuf.getHeight() + rectangleBuf.getHeight()/2)))){
                return  false;
            }
            else {
                System.out.println("not same shape overlay");
                return true;
            }
        }
    }

    private double findClosetCalcDistance(GameObject a, GameObject b) {
        double min = Double.POSITIVE_INFINITY;
        double [] buf = new double [4];
        buf[0] = calcDistance(a.getPositionX(),a.getPositionY(),(b.getPositionX() - b.getWidth()/2),(b.getPositionY() - b.getHeight()/2)); // left up
        buf[1] = calcDistance(a.getPositionX(),a.getPositionY(),(b.getPositionX()+b.getWidth()/2),(b.getPositionY() - b.getHeight()/2)); //right up
        buf[2] = calcDistance(a.getPositionX(),a.getPositionY(),(b.getPositionX()-b.getWidth()/2),(b.getPositionY() + b.getHeight()/2)); //left down
        buf[3] = calcDistance(a.getPositionX(),a.getPositionY(),(b.getPositionX()+b.getWidth()/2),(b.getPositionY() + b.getHeight()/2)); //Right down
        for(int i=0;i<buf.length;i++) {
            if(buf[i]<min) {
                min =buf[i];
            }
        }
        return  min;
    }
    private boolean isSameQuadrant(GameObject a, GameObject b) {
        int circleX = a.getPositionX();
        int circleY = a.getPositionY();
        int leftX = b.getPositionX() - b.getWidth()/2 - circleX;
        int leftY = b.getPositionY() - b.getHeight()/2 - circleY;
        int rightX = b.getPositionX() + b.getWidth()/2 - circleX;
        int rightY = b.getPositionY() + b.getHeight()/2 - circleY;

        if(leftX>0 &&rightX>0) {
            if((leftY>0 && rightY>0)|| (leftY<0 && rightY<0)){
                return  true;
            }
            return false;
        }
        else if(leftX<0&& rightX<0) {
            if((leftY>0 && rightY>0)|| (leftY<0 && rightY<0)){
                return  true;
            }
            return  false;
        }
        else {
            return  false;
        }

    }
    private double calcDistance(double x1,double y1,double x2,double y2 ) {
        return Math.sqrt( Math.pow(x1-x2,2)+Math.pow(y1-y2,2) );
    }
    private int  randomCoordinate(int  start,int  end, Random aRandom) {
        long range = (long)end - (long)start + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * aRandom.nextDouble());
        int  randomNumber =  (int )(fraction + start);
        return randomNumber;
    }
    private int setRandomPoint(double input) {
        if(input<0.05) {
            return 50;
        }
        else if(input <0.1) {
            return 25;
        }
        else if(input <0.2) {
            return 20;
        }
        else if(input <0.3) {
            return  15;
        }
        else if(input <0.4) {
            return 10;
        }
        else if(input <0.6) {
            return  5;
        }
        else  if(input <0.8) {
            return  2;
        }
        return  1;
    }
    private void gameInit() {
        initialObstacle();
        initialItem();
        initialCoin();
        initXYMAP();
    }
    private void initialObstacle() {
        Obstacle buf  = new Obstacle(0,0);
        ArrayList<Obstacle> obstacleArrayList = cmd.getObstacleArrayList();
        int canDrawMapWidth = mapWidth-2*buf.getWidth();
        int canDrawMapHeight = mapHeight-2*buf.getHeight();
        int Frame = (int)Math.sqrt((canDrawMapWidth *canDrawMapHeight )/totalObstacle);

        int offsetX = buf.getWidth()/2;
        int offsetY = buf.getHeight()/2;
        for(int  i = Frame,j = Frame,count =0;count<totalObstacle;i+=Frame,count ++) {
            if(i>=canDrawMapWidth) {
                i = Frame;
                j += Frame;
                if(j>=canDrawMapHeight) {
                    j = canDrawMapHeight;
                }
            }
            int  x = randomCoordinate(i-Frame,i,ran);
            int  y = randomCoordinate(j-Frame,j,ran);

            buf = new Obstacle (x+offsetX,y+offsetY);
            for(int q = 0; q< overlayTimes; q++) {
                if(isAllOverlay(buf)) {
                    x = randomCoordinate(i-Frame,i,ran);
                    y = randomCoordinate(j-Frame,j,ran);
                    buf.setPosition(x+offsetX,y+offsetY);
                }
                else {
                    obstacleArrayList.add(buf);
                    break;
                }
            }
        }
        System.out.print("obstacle size" +obstacleArrayList.size());
    }
    private void initialItem() {
        Item buf  = new Item(0,0);
        ArrayList<Item> itemArrayList = cmd.getItemArrayList();
        int canDrawMapWidth = mapWidth-2*buf.getWidth();
        int canDrawMapHeight = mapHeight-2*buf.getHeight();
        Double setFrame = Math.sqrt((canDrawMapWidth *canDrawMapHeight )/totalItem);
        int Frame = setFrame.intValue();
        int offsetX = buf.getWidth()/2;
        int offsetY = buf.getHeight()/2;
        for(int  i = Frame,j = Frame,count =0;count<totalItem;i+=Frame,count ++) {
            if(i>=canDrawMapWidth) {
                i = Frame;
                j += Frame;
                if(j>=canDrawMapHeight) {
                    j = canDrawMapHeight;
                }
            }
            int  x = randomCoordinate(i-Frame,i,ran);
            int  y = randomCoordinate(j-Frame,j,ran);

            buf = new Item (x+offsetX,y+offsetY);
            for(int q = 0; q< overlayTimes; q++) {
                if(isAllOverlay(buf)) {
                    x = randomCoordinate(i-Frame,i,ran);
                    y = randomCoordinate(j-Frame,j,ran);
                    buf.setPosition(x+offsetX,y+offsetY);
                }
                else {
                    itemArrayList.add(buf);

                    break;
                }
            }
        }
    }
    private  void initXYMAP(){

        moveXMap = new HashMap<Integer, Integer>();
        moveYMap = new HashMap<Integer, Integer>();

        moveXMap.put( TURNEAST, 1 );
        moveYMap.put( TURNEAST, 0 );

        moveXMap.put( TURNSOUTH, 0 );
        moveYMap.put( TURNSOUTH, 1 );

        moveXMap.put( TURNNORTH, 0 );
        moveYMap.put( TURNNORTH, -1 );

        moveXMap.put( TURNWEST, -1 );
        moveYMap.put( TURNWEST, 0 );

        moveXMap.put( TURNEASTNORTH, 1 );
        moveYMap.put( TURNEASTNORTH, -1 );

        moveXMap.put( TURNEASTSOUTH, 1);
        moveYMap.put( TURNEASTSOUTH, 1);

        moveXMap.put( TURNWESTNORTH, -1);
        moveYMap.put( TURNWESTNORTH, -1);

        moveXMap.put( TURNWESTSOUTH, -1);
        moveYMap.put( TURNWESTSOUTH, 1);
    }
    private void initialCoin() {
        Coin buf  = new Coin(0,0);
        ArrayList<Coin> coinArrayList = cmd.getCoinArrayList();
        int canDrawMapWidth = mapWidth-2*buf.getWidth();
        int canDrawMapHeight = mapHeight-2*buf.getHeight();
        Double setFrame = Math.sqrt((canDrawMapWidth *canDrawMapHeight )/totalCoin);
        int Frame = setFrame.intValue();
        int offsetX = buf.getWidth()/2;
        int offsetY = buf.getHeight()/2;
        for(int  i = Frame,j = Frame,count =0;count<totalCoin;i+=Frame,count ++) {
            if (i >= canDrawMapWidth) {
                i = Frame;
                j += Frame;
                if (j >= canDrawMapHeight) {
                    j = canDrawMapHeight;
                }
            }
            int x = randomCoordinate(i - Frame, i, ran);
            int y = randomCoordinate(j - Frame, j, ran);

            buf = new Coin(x + offsetX, y + offsetY);

            for (int q = 0; q < overlayTimes; q++) {
                if (isAllOverlay(buf)) {
                    x = randomCoordinate(i - Frame, i, ran);
                    y = randomCoordinate(j - Frame, j, ran);
                    buf.setPosition(x + offsetX, y + offsetY);
                } else {
                    buf.setPoint(setRandomPoint(ran.nextDouble()));
                    coinArrayList.add(buf);
                    break;
                }
            }
        }
    }
   public  void playerMove(String id, int direction ) {
       //search id for this player
       Player player = null;
       for(int index = 0; index<cmd.getPlayerArrayList().size(); index++) {
           player = cmd.getPlayerArrayList().get(index);
           if (id.equals(player.getId())) {
               break;
           }
       }
       int dirX = moveXMap.get( direction );
       int dirY = moveYMap.get( direction );
       System.out.println("X " + player.getPositionX() + "Y " +player.getPositionY());
           player.setPosition(player.getPositionX() + player.getSpeed() * dirX ,
                   player.getPositionY() +  player.getSpeed() * dirY );
           if(isCanMove(player)) {
             // YAYA
           }
           else {
               player.setPosition(player.getPositionX() -  player.getSpeed() * dirX ,
                       player.getPositionY() -  player.getSpeed() * dirY );
           }

   }
    private void changePlayerStatus(Item item, Player player) {

    }
    private void reRandomObject(GameObject a) {
        int x = randomCoordinate(0, mapWidth, ran);
        int y = randomCoordinate(0, mapHeight, ran);
        a.setPosition(x,y);
        while(isAllOverlay(a)) { // 除了自己有沒有overlay
            x = randomCoordinate(0, mapWidth, ran);
            y = randomCoordinate(0, mapHeight, ran);
            a.setPosition(x,y);
        }
    }
    public void removePlayer(InetAddress address) {
        for (int i= 0; i<cmd.getPlayerArrayList().size(); i++) {
            if(address.equals(cmd.getPlayerArrayList().get(i).getAddress())) {
              //  System.out.println(cmd.getPlayerArrayList().get(i).getAddress() + "removePlayer is called.");
                cmd.getPlayerArrayList().remove(i);
                break;
            }
        }
    }
    /*private boolean boundary(Player player) {
        int playerMapWidth = mapWidth-player.getWidth();
        int playerMapHeight = mapHeight-player.getHeight();
        if(player.getPositionX() >playerMapWidth || player.getPositionX()<player.getWidth()) {
            return  true;
        }
        else if (player.getPositionY() > playerMapHeight || player.getPositionY()<player.getHeight()) {
            return  true;
        }
        return  false;
    }*/
    private boolean boundary (GameObject object) {
        int playerMapWidth = mapWidth-object.getWidth();
        int playerMapHeight = mapHeight-object.getHeight();
        if(object.getPositionX() >playerMapWidth || object.getPositionX()<object.getWidth()) {
            return  true;
        }
        else if (object.getPositionY() > playerMapHeight || object.getPositionY()<object.getHeight()) {
            return  true;
        }
        return  false;
    }
    private boolean isCanMove(Player a) {
        if(boundary(a)) {
            return false;
        }
        ArrayList<Coin> coinArrayList = cmd.getCoinArrayList();
        ArrayList<Obstacle> obstacleArrayList = cmd.getObstacleArrayList();
        ArrayList<Item> itemArrayList = cmd.getItemArrayList();
        ArrayList<Player> playerArrayList = cmd.getPlayerArrayList();
        for(int i = 0; i<obstacleArrayList.size();i++) {
            if( isOverlay(obstacleArrayList.get(i), a)) {
               // System.out.println("X "+ obstacleArrayList.get(i).getPositionX()+ " Y " + obstacleArrayList.get(i).getPositionY());
                //System.out.println("playX " + a.getPositionX() + "playY " +a.getPositionY());
                //show(obstacleArrayList);
                System.out.print("There has Obstacle. if u could not see it, that is your problem, not mine\n");
                return false;
            }
        }
        for(int i = 0; i<playerArrayList.size();i++) {
            if( isOverlay( playerArrayList.get(i), a) &&( playerArrayList.get(i) != a )) {
                return  false;
            }
        }
        for(int i = 0; i<itemArrayList.size();i++) {
            if( isOverlay( itemArrayList.get(i), a) ) {
                changePlayerStatus(itemArrayList.get(i),a); // problem 1 如果碰到很多item 吃哪個??
                reRandomObject(itemArrayList.get(i)); // 討論 item 個數 也跟分數一樣 random ???
                //bufcmd.getItemArrayList().add(itemArrayList.get(i));

            }
        }
        for(int i = 0; i<coinArrayList.size();i++) {
            if( isOverlay( coinArrayList.get(i), a) ) {
                a.setScore( a.getScore() + coinArrayList.get(i).getPoint() );
                isEightySeven(a);
                coinArrayList.get(i).setPoint( setRandomPoint( ran.nextDouble() ) );
                reRandomObject( coinArrayList.get(i) );
                //bufcmd.getCoinArrayList().add(coinArrayList.get(i));
            }
        }
        return  true;
    }
    public void isEightySeven(Player player) {
        if(player.getScore()==87) {
            // Upgrade
            player.setCount87(player.getCount87()+1);
            player.setScore(0);
        }
        else if (player.getScore()>87) {
            player.setScore(0);
        }
    }
    private boolean isAllOverlay(GameObject a) {
        ArrayList<Coin> coinArrayList = cmd.getCoinArrayList();
        ArrayList<Obstacle> obstacleArrayList = cmd.getObstacleArrayList();
        ArrayList<Item> itemArrayList = cmd.getItemArrayList();
        ArrayList<Player> playerArrayList = cmd.getPlayerArrayList();
        for(int i = 0; i<coinArrayList.size();i++) {
            if( isOverlay( coinArrayList.get(i), a) && ( coinArrayList.get(i) != a )) {
                return true;
            }
        }
        for(int i = 0; i<itemArrayList.size();i++) {
            if( isOverlay( itemArrayList.get(i), a) && ( itemArrayList.get(i) != a )) {
                return true;
            }
        }
        for(int i = 0; i<obstacleArrayList.size();i++) {
            if( isOverlay( obstacleArrayList.get(i), a) && ( obstacleArrayList.get(i) != a ) ) {
                return true;
            }
        }
        for(int i = 0; i<playerArrayList.size();i++) {
            if( isOverlay( playerArrayList.get(i), a) && ( playerArrayList.get(i) != a )) {
                return true;
            }
        }
        return  boundary(a);
    }
}

