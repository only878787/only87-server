package ncu.sw.gameServer;

import ncu.sw.gameUtility.GameObject;
import ncu.sw.gameUtility.Obstacle;
import ncu.sw.gameUtility.Coin;
import ncu.sw.gameUtility.Item;
import ncu.sw.gameUtility.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by nwlabclub on 2016/11/10.
 */
public class Initial {

    private int mapX = 5000;
    private int mapY = 3000;
    private int collisionTimes = 10;
    private Random ran = new Random();
    private List<GameObject> objList;
    private List<Coin>  coinList;
    private List<Item> itemList;
    private List<Obstacle> obstacleList;
    private List<Player> playList;
    private int totalCoin;
    private int totalObstacle;
    private int totalItem;


    public Initial(int totalCoin,int totalObstacle,int totalItem) {
        this.totalCoin = totalCoin;
        this.totalItem = totalItem;
        this.totalObstacle = totalObstacle;
        objList = new ArrayList<>();
        coinList = new ArrayList<>();
        itemList = new ArrayList<>();
        obstacleList = new ArrayList<>();
        playList = new ArrayList<>();
        this.randomMap();
    }
    private boolean isCollision(GameObject a, GameObject b) {
        if(a.getAttribute()==b.getAttribute()) {
            if(a.getAttribute()== 0 ) { // circle
                if(calcDistance(a.getPositionX(),a.getPositionY(),b.getPositionX(),b.getPositionY())>=a.getWidth()+b.getWidth()) {
                    return false;
                }
                else {
                    return true;
                }
            }
            else {// x1-x2 絕對值 比w1/2+w2/2 大 則沒碰撞 // 兩個矩形
                if((Math.abs(a.getPositionX()-b.getPositionX())>=(a.getWidth()/2 + b.getWidth()/2)) && (Math.abs(a.getPositionY()-b.getPositionY())>= (a.getHight()/2 + b.getHight()/2))) {
                    return  false;
                }
                else {
                    return true;
                }
            }
        }
        else {
            if(a.getAttribute() == 0) { //circle is a  rectangle is b
                if(isSameQuadrant(a,b)) {
                    if(findClosetCalcDistance(a,b)>=a.getHight()) {
                        return false;
                    }
                    else {
                        return  true;
                    }
                }
                else if(((Math.abs(a.getPositionX()-b.getPositionX())>=(a.getWidth() + b.getWidth()/2)) && (Math.abs(a.getPositionY()-b.getPositionY())>= (a.getHight() + b.getHight()/2)))){
                    return  false;
                }
                else {
                    return true;
                }
            }
            else { //circle is b rectangle is a
                if(isSameQuadrant(b,a)) {
                    if(findClosetCalcDistance(b,a)>=b.getHight()) {
                        return false;
                    }
                    else {
                        return  true;
                    }
                }
                else if(((Math.abs(b.getPositionX()-a.getPositionX())>=(b.getWidth() + a.getWidth()/2)) && (Math.abs(b.getPositionY()-a.getPositionY())>= (b.getHight() + a.getHight()/2)))){
                    return  false;
                }
                else {
                    return true;
                }

            }
        }
    }
    private double findClosetCalcDistance(GameObject a, GameObject b) {
        double buf = calcDistance(a.getPositionX(),a.getPositionY(),(b.getPositionX() - b.getWidth()/2),(b.getPositionY() + b.getHight()/2)); // left up
        double distance = buf;
        buf = calcDistance(a.getPositionX(),a.getPositionY(),(b.getPositionX()+b.getWidth()/2),(b.getPositionY() + b.getHight()/2)); //right up
        if(distance<buf) {
            distance = buf;
        }
        buf = calcDistance(a.getPositionX(),a.getPositionY(),(b.getPositionX()-b.getWidth()/2),(b.getPositionY() - b.getHight()/2)); //lefu down
        if(distance<buf) {
            distance = buf;
        }
        buf = calcDistance(a.getPositionX(),a.getPositionY(),(b.getPositionX()-b.getWidth()/2),(b.getPositionY() - b.getHight()/2)); //Right down
        if(distance<buf) {
            distance = buf;
        }
        return  distance;
    }
    private boolean isSameQuadrant(GameObject a, GameObject b) {
        int circleX = a.getPositionX();
        int circleY = a.getPositionY();
        int leftX = b.getPositionX() - b.getWidth()/2 - circleX;
        int leftY = b.getPositionY() + b.getHight()/2 - circleY;
        int rightX = b.getPositionX() + b.getWidth()/2 - circleX;
        int rightY = b.getPositionY() - b.getHight()/2 - circleY;

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
    private void randomMap() {
        initialObstacle();
        initialItem();
        initialCoin();
        System.out.println(objList.size());
        for(int i=0;i<objList.size();i++) {
            GameObject a = objList.get(i);
            System.out.println(i + " "+a.getPositionX() +" "+ a.getPositionY());
        }
    }
    private void initialObstacle() {
        Obstacle buf  = new Obstacle(0,0);
        int counter = 0;
        int canDrawMapX = mapX-2*buf.getWidth();
        int canDrawMapY = mapY-2*buf.getHight();
        Double setFrame = Math.sqrt((canDrawMapX *canDrawMapY )/totalObstacle);
        int Frame = setFrame.intValue();
        int offsetX = buf.getWidth()/2;
        int offsetY = buf.getHight()/2;
        for(int  i = Frame,j = Frame,count =0;count<totalObstacle;i+=Frame,count ++,counter = 0) {
            if(i>=canDrawMapX) {
                i = Frame;
                j += Frame;
                if(j>=canDrawMapY) {
                    j = canDrawMapY;
                }
            }
            int  x = randomCoordinate(i-Frame,i,ran);
            int  y = randomCoordinate(j-Frame,j,ran);

            buf = new Obstacle (x+offsetX,y+offsetY);
            for(int q = 0;q<objList.size();q++) {
                if(isCollision(objList.get(q),buf) ){
                    x = randomCoordinate(i-Frame,i,ran);
                    y = randomCoordinate(j-Frame,j,ran);
                    buf.setPosition(x+offsetX,y+offsetY);
                    q = 0;
                    counter ++;
                }
                if(counter>collisionTimes) {
                    break;
                }
            }
            if(counter<=collisionTimes) {
                obstacleList.add(buf);
                objList.add(buf);
            }
        }

    }
    private void initialItem() {
        Item buf  = new Item(0,0);
        int counter = 0;
        int canDrawMapX = mapX-2*buf.getWidth();
        int canDrawMapY = mapY-2*buf.getHight();
        Double setFrame = Math.sqrt((canDrawMapX *canDrawMapY )/totalItem);
        int Frame = setFrame.intValue();
        int offsetX = buf.getWidth()/2;
        int offsetY = buf.getHight()/2;
        for(int  i = Frame,j = Frame,count =0;count<totalItem;i+=Frame,count ++,counter = 0) {
            if(i>=canDrawMapX) {
                i = Frame;
                j += Frame;
                if(j>=canDrawMapY) {
                    j = canDrawMapY;
                }
            }
            int  x = randomCoordinate(i-Frame,i,ran);
            int  y = randomCoordinate(j-Frame,j,ran);

            buf = new Item (x+offsetX,y+offsetY);
            for(int q = 0;q<objList.size();q++) {
                if(isCollision(objList.get(q),buf) ){
                    x = randomCoordinate(i-Frame,i,ran);
                    y = randomCoordinate(j-Frame,j,ran);
                    buf.setPosition(x+offsetX,y+offsetY);
                    q = 0;
                    counter ++;
                }
                if(counter>collisionTimes) {
                    break;
                }
            }
            if(counter<=collisionTimes) {
                itemList.add(buf);
                objList.add(buf);
            }
        }
    }
    private void initialCoin() {
        Coin buf  = new Coin(0,0);
        int counter = 0;
        int canDrawMapX = mapX-2*buf.getWidth();
        int canDrawMapY = mapY-2*buf.getHight();
        Double setFrame = Math.sqrt((canDrawMapX *canDrawMapY )/totalCoin);
        int Frame = setFrame.intValue();
        int offsetX = buf.getWidth()/2;
        int offsetY = buf.getHight()/2;
        for(int  i = Frame,j = Frame,count =0;count<totalCoin;i+=Frame,count ++,counter = 0) {
            if(i>=canDrawMapX) {
                i = Frame;
                j += Frame;
                if(j>=canDrawMapY) {
                    j = canDrawMapY;
                }
            }
            int  x = randomCoordinate(i-Frame,i,ran);
            int  y = randomCoordinate(j-Frame,j,ran);

            buf = new Coin (x+offsetX,y+offsetY);
            for(int q = 0;q<objList.size();q++) {
                if(isCollision(objList.get(q),buf) ){
                    x = randomCoordinate(i-Frame,i,ran);
                    y = randomCoordinate(j-Frame,j,ran);
                    buf.setPosition(x+offsetX,y+offsetY);
                    q = 0;
                    counter ++;
                }
                if(counter>collisionTimes) {
                    break;
                }
            }
            if(counter<=collisionTimes) {
                buf.setPoint(setPoint(ran.nextDouble()));
                coinList.add(buf);
                objList.add(buf);
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

