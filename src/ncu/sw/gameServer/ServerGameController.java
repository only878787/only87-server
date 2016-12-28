package ncu.sw.gameServer;

import ncu.sw.UDPSM.UDPBroadCastClient;
import ncu.sw.gameUtility.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * Created by Arson on 2016/11/1.
 */
public class ServerGameController {
    private final int totalCoin = 250;
    private final int totalItem = 100;
    private final int totalObstacle = 100;
    private final int mapWidth = 5000;
    private final int mapHeight= 3000;
    private final int overlayTimes = 10;
    private final int itemInterval = 4000;
    private Random ran;
    private Cmd cmd;
    public final int fastItem = 1;
    public final int slowItem = 2;
    public final int bigItem = 3;
    public final int inverseItem = 4;
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
    private HashMap<Player, Timer> itemTimerMap;
    private HashMap<Player, Timer> bulletTimerMap;

    public static ServerGameController getInstance() {
        if(ourInstance == null) {
          ourInstance  = new ServerGameController();
        }
        return ourInstance;
    }
    private ServerGameController() {
        cmd = new Cmd();
        itemTimerMap = new HashMap<Player, Timer>();
        bulletTimerMap = new HashMap<Player, Timer>();
        ran = new Random();
        gameInit();

    }
    public synchronized Cmd getCmd() {
        return  this.cmd;
    }
    public synchronized boolean playCreate(String id, InetSocketAddress ipAddress) {
        //String address = ipAddress.toString();
        System.out.println(id +" is created.");
        if(isSameId(id)) {
            return  false;
        }
        else {
            Player player = new Player(0, 0, id, ipAddress);
            int[] position = this.randomPosition(player);
            player.setPosition(position[0],position[1]);
            // add into itemTimer
            itemTimerMap.put( player, new Timer());
            bulletTimerMap.put( player, new Timer() );
            cmd.getPlayerArrayList().add(player);
            xd(id,player);
            return  true;
        }
    }
    private void xd(String id,Player a) {
        if(id.equals("我是外掛")) {
            a.setCount87(50);
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
    public boolean isOverlay(GameObject a, GameObject b) {
        GameObject circleBuf;
        GameObject rectangleBuf;
        if( a.getAttribute() == b.getAttribute() ) { //they are the same shape.
            if( a.getAttribute()== 0 ) { // circle
                if(calcDistance( a.getPositionX(), a.getPositionY(), b.getPositionX(), b.getPositionY() )
                        >= a.getWidth()+b.getWidth() ) {
                    return false;
                }
                else {
                    return true;
                }
            }
            else {// x1-x2 絕對值 比w1/2+w2/2 大 則沒碰撞 // 兩個矩形
                if((Math.abs(a.getPositionX()-b.getPositionX())>=(a.getWidth()/2 + b.getWidth()/2)) &&
                        (Math.abs(a.getPositionY()-b.getPositionY())>= (a.getHeight()/2 + b.getHeight()/2))) {
                    return  false;
                }
                else {
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
    public synchronized  void playerMove(String id, int direction ) {
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
       // System.out.println("X " + player.getPositionX() + "Y " +player.getPositionY());
        player.setPosition(player.getPositionX() + player.getSpeed() * dirX * player.getMoveDir() ,
                player.getPositionY() +  player.getSpeed() * dirY * player.getMoveDir() );
        if(isCanMove(player)) {
        }
        else {
            player.setPosition(player.getPositionX() -  player.getSpeed() * dirX  * player.getMoveDir(),
                    player.getPositionY() -  player.getSpeed() * dirY * player.getMoveDir() );
        }
    }
    private void changePlayerStatus(Item item, Player player) {
        player.setSpeed( 20 );
        player.setRadius( 50 );
        player.setMoveDir( 1 );
        Timer timer = itemTimerMap.get(player);
        timer.cancel();
        itemTimerMap.put( player, new Timer());
        timer = itemTimerMap.get(player);
        int randomNum = ran.nextInt( 4 ) +1 ;  // 1~4
        item.setEfect( randomNum );
        player.setEffectNum( randomNum );
        timer.schedule( new ItemTask(player), itemInterval );
        switch ( item.getEfect() ){
            case fastItem :
                //System.out.println("fastItem");
                player.setSpeed( player.getSpeed() *2 );
                break;
            case slowItem :
                //System.out.println("slowItem");
                player.setSpeed( player.getSpeed() /8);
                break;
            case bigItem:
                //System.out.println("bigItem");
                player.setRadius( player.getRadius() * 2 );
                break;
            case inverseItem:
                //System.out.println("inverseItem");
                player.setMoveDir(  -1 * player.getMoveDir()  );
        }

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
    public synchronized String removePlayer(InetSocketAddress address) {
        String id;
        for (int i= 0; i<cmd.getPlayerArrayList().size(); i++) {
            Player p =  cmd.getPlayerArrayList().get(i);
            if( address.equals(p.getSocketAddress())  ){
                id = cmd.getPlayerArrayList().get(i).getId();
                cmd.getPlayerArrayList().remove(i);
                return id;
            }
        }
        return  null;
    }
    public  boolean boundary (GameObject object) {
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
    public synchronized boolean isCanMove(Player a) {
        if(boundary(a)) {
            return false;
        }
        ArrayList<Coin> coinArrayList = cmd.getCoinArrayList();
        ArrayList<Obstacle> obstacleArrayList = cmd.getObstacleArrayList();
        ArrayList<Item> itemArrayList = cmd.getItemArrayList();
        ArrayList<Player> playerArrayList = cmd.getPlayerArrayList();
        for(int i = 0; i<obstacleArrayList.size();i++) {
            if( isOverlay(obstacleArrayList.get(i), a)) {
             //   System.out.print("There has Obstacle. if u could not see it, that is your problem, not mine\n");
                return false;
            }
        }
        for(int i = 0; i<playerArrayList.size();i++) {
            if( isOverlay( playerArrayList.get(i), a) &&( playerArrayList.get(i) != a )) {
               // System.out.print("Hey! Don't push me \n");
                return  false;
            }
        }
        for(int i = 0; i<itemArrayList.size();i++) {
            if( isOverlay( itemArrayList.get(i), a) ) {
                changePlayerStatus(itemArrayList.get(i),a); // problem 1 如果碰到很多item 吃哪個??
                reRandomObject(itemArrayList.get(i)); // 討論 item 個數 也跟分數一樣 random ???
              //  System.out.print("Eat item \n");
            }
        }
        for(int i = 0; i<coinArrayList.size();i++) {
            if( isOverlay( coinArrayList.get(i), a) ) {
                a.setScore( a.getScore() + coinArrayList.get(i).getPoint() );
                isEightySeven(a);
                coinArrayList.get(i).setPoint( setRandomPoint( ran.nextDouble() ) );
                reRandomObject( coinArrayList.get(i) );
              //  System.out.print("Eat coin \n");
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
            if(player.getCount87()!= 0) {
                player.setCount87(player.getCount87()-1);
            }
        }
    }
    public boolean isOverlayWithCoin(GameObject a) {
        ArrayList<Coin> coinArrayList = cmd.getCoinArrayList();
        for(int i = 0; i<coinArrayList.size();i++) {
            if( isOverlay( coinArrayList.get(i), a) && ( coinArrayList.get(i) != a )) {
               // System.out.print("Overlay with coin\n");
                return true;
            }
        }
        return  false;
    }
    public boolean isOverlayWithObstacle(GameObject a) {
        ArrayList<Obstacle> obstacleArrayList = cmd.getObstacleArrayList();
        for(int i = 0; i<obstacleArrayList.size();i++) {
            if( isOverlay( obstacleArrayList.get(i), a) && ( obstacleArrayList.get(i) != a ) ) {
              // System.out.print("Overlay with obstacle\n");
                return true;
            }
        }
        return  false;
    }
    public boolean isOverlayWithItem(GameObject a) {
        ArrayList<Item> itemArrayList = cmd.getItemArrayList();
        for(int i = 0; i<itemArrayList.size();i++) {
            if( isOverlay( itemArrayList.get(i), a) && ( itemArrayList.get(i) != a )) {
                //System.out.print("Overlay with item\n");
                return true;
            }
        }
        return  false;
    }
    public  boolean isOverlayWithPlayer(GameObject a) {
        ArrayList<Player> playerArrayList = cmd.getPlayerArrayList();
        for(int i = 0; i<playerArrayList.size();i++) {
            if( isOverlay( playerArrayList.get(i), a) && ( playerArrayList.get(i) != a )) {
              //  System.out.print("Overlay with player\n");
                return true;
            }
        }
        return  false;
    }
    public boolean isAllOverlay(GameObject a) {
        if(!isOverlayWithCoin(a)) {
            if(!isOverlayWithItem(a)) {
                if(!isOverlayWithObstacle(a)) {
                    if(!isOverlayWithPlayer(a)) {
                        if(!boundary(a)) {
                            return  false;
                        }
                    }
                }
            }
        }
        return true;
    }
    public HashMap<Integer, Integer> getMoveXMap() {
        return moveXMap;
    }
    public HashMap<Integer, Integer> getMoveYMap() {
        return moveYMap;
    }
    public HashMap<Player, Timer> getBulletTimerMap() {
        return bulletTimerMap;
    }
}