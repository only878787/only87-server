package ncu.sw.gameServer;

import ncu.sw.gameUtility.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Arson on 2016/11/1.
 */
public class ServerGameController {
    private List<GameObject> objList;
    private List<Coin>  coinList;
    private List<Item> itemList;
    private List<Obstacle> obstacleList;
    private List<Player> playList;
    private int totalCoin;
    private int totalItem;
    private int totalObstacle;
    private final int mapWidth = 5000;
    private final int mapHeight= 3000;
    private final int collisionTimes = 10;
    private Random ran;

    public ServerGameController(int totalCoin, int totalItem, int totalObstacle) {
        this.totalCoin = totalCoin;
        this.totalItem = totalItem;
        this.totalObstacle = totalObstacle;
        objList = new ArrayList<>();
        coinList = new ArrayList<>();
        itemList = new ArrayList<>();
        obstacleList = new ArrayList<>();
        playList = new ArrayList<>();
        ran = new Random();
        gameInit();
        playCreate("1232");
        show();
    }
    public void show() {
        System.out.println(objList.size());
        for(int i=0;i<objList.size();i++) {
            GameObject a = objList.get(i);
            System.out.println(i + " "+a.getPositionX() +" "+ a.getPositionY());
        }
    }
    public boolean playCreate(String id) {
        if(isSameId(id)) {
            return  false;
        }
        else {
            Player player = new Player(0, 0, id);
            int[] position = this.randomPosition(player);
            player.setPosition(position[0],position[1]);
            playList.add(player);
            objList.add(player);
            return  true;
        }
    }
    private boolean isSameId(String id) {
        for(int i=0;i<playList.size();i++) {
            Player player = playList.get(i);
            if(id.equals(player.getId())) {
                return true;
            }
        }
        return false;
    }
    private int [] randomPosition(GameObject object) {
        int canDrawMapWidth = mapWidth-2*object.getWidth();
        int canDrawMapHeight = mapHeight-2*object.getHeight();
        int  x = randomCoordinate(0,canDrawMapWidth,ran);
        int  y = randomCoordinate(0,canDrawMapHeight,ran);
        object.setPosition(x,y);
        while(isAllCollision(object)) {
            x = randomCoordinate(0,canDrawMapWidth,ran);
            y = randomCoordinate(0,canDrawMapHeight,ran);
            object.setPosition(x,y);
        }
        int []position = new int[2];
        position[0] = x;
        position[1] = y;
        return position;
    }
    private boolean isCollision(GameObject a, GameObject b) {
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
            if(b.getAttribute() == 0) {
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
                    && (Math.abs(circleBuf.getPositionY()-rectangleBuf.getPositionY())
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
        buf[3] = calcDistance(a.getPositionX(),a.getPositionY(),(b.getPositionX()-b.getWidth()/2),(b.getPositionY() + b.getHeight()/2)); //Right down
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
    private int setPoint(double input) {
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
    }
    private void initialObstacle() {
        Obstacle buf  = new Obstacle(0,0);

        int canDrawMapWidth = mapWidth-2*buf.getWidth();
        int canDrawMapHeight = mapHeight-2*buf.getHeight();
        Double setFrame = Math.sqrt((canDrawMapWidth *canDrawMapHeight )/totalObstacle);
        int Frame = setFrame.intValue();
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
            for(int q=0;q<collisionTimes;q++) {
                if(isAllCollision(buf)) {
                    x = randomCoordinate(i-Frame,i,ran);
                    y = randomCoordinate(j-Frame,j,ran);
                    buf.setPosition(x+offsetX,y+offsetY);
                }
                else {
                    obstacleList.add(buf);
                    objList.add(buf);
                    break;
                }
            }
        }
    }
    private void initialItem() {
        Item buf  = new Item(0,0);

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
            for(int q=0;q<collisionTimes;q++) {
                if(isAllCollision(buf)) {
                    x = randomCoordinate(i-Frame,i,ran);
                    y = randomCoordinate(j-Frame,j,ran);
                    buf.setPosition(x+offsetX,y+offsetY);
                }
                else {
                    itemList.add(buf);
                    objList.add(buf);
                    break;
                }
            }
        }
    }
    private void initialCoin() {
        Coin buf  = new Coin(0,0);

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

            for (int q = 0; q < collisionTimes; q++) {
                if (isAllCollision(buf)) {
                    x = randomCoordinate(i - Frame, i, ran);
                    y = randomCoordinate(j - Frame, j, ran);
                    buf.setPosition(x + offsetX, y + offsetY);
                } else {
                    buf.setPoint(setPoint(ran.nextDouble()));
                    coinList.add(buf);
                    objList.add(buf);
                    break;
                }
            }
        }
    }
    private boolean isAllCollision(GameObject a) {
        for(int i = 0; i<objList.size(); i++) {
            if( isCollision( objList.get(i), a) ) {
                return true;
            }
        }
        return  false;
    }
}

