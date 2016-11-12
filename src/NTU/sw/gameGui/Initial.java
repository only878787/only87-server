package NTU.sw.gameGui;

import NTU.sw.gameUtility.Coin;
import NTU.sw.gameUtility.GameObject;
import NTU.sw.gameUtility.Item;
import NTU.sw.gameUtility.Obstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by nwlabclub on 2016/11/10.
 */
public class Initial {
    private int Total_Coin;
    private int Total_Obstacle;
    private int Total_Item;
    private double MapX = 5000;
    private double MapY = 3000;

    public Initial(int total_Coin,int total_Obstacle,int total_Item) {
        this.Total_Coin = total_Coin;
        this.Total_Item = total_Item;
        this.Total_Obstacle = total_Obstacle;
        this.randomMap();

    }
    private boolean Collision(GameObject a, GameObject b) {
        if(a.GetAttribute()==b.GetAttribute()) {
            if(a.GetAttribute()== 0 ) { // circle
                if(Distance(a.GetPositionX(),a.GetPositionY(),b.GetPositionX(),b.GetPositionY())>=a.GetWidth()+b.GetWidth()) {
                    return false;
                }
                else {
                    return true;
                }
            }
            else {// x1-x2 絕對值 比w1/2+w2/2 大 則沒碰撞 // 兩個矩形
                if((Math.abs(a.GetPositionX()-b.GetPositionX())>=(a.GetWidth()/2 + b.GetWidth()/2)) && (Math.abs(a.GetPositionY()-b.GetPositionY())>= (a.GetHight()/2 + b.GetHight()/2))) {
                    return  false;
                }
                else {
                    return true;
                }
            }
        }
        else {
            if(a.GetAttribute() == 0) { //circle is a  rectangle is b
                if(isSameQuadrant(a,b)) {
                    if(FindClosetDistance(a,b)>=a.GetHight()) {
                        return false;
                    }
                    else {
                        return  true;
                    }
                }
                else if(((Math.abs(a.GetPositionX()-b.GetPositionX())>=(a.GetWidth() + b.GetWidth()/2)) && (Math.abs(a.GetPositionY()-b.GetPositionY())>= (a.GetHight() + b.GetHight()/2)))){
                    return  false;
                }
                else {
                    return true;
                }
            }
            else { //circle is b rectangle is a
                if(isSameQuadrant(b,a)) {
                    if(FindClosetDistance(b,a)>=b.GetHight()) {
                        return false;
                    }
                    else {
                        return  true;
                    }
                }
                else if(((Math.abs(b.GetPositionX()-a.GetPositionX())>=(b.GetWidth() + a.GetWidth()/2)) && (Math.abs(b.GetPositionY()-a.GetPositionY())>= (b.GetHight() + a.GetHight()/2)))){
                    return  false;
                }
                else {
                    return true;
                }

            }
        }
    }
    private double FindClosetDistance(GameObject a, GameObject b) {
        double buf = Distance(a.GetPositionX(),a.GetPositionY(),(b.GetPositionX() - b.GetWidth()/2),(b.GetPositionY() + b.GetHight()/2)); // left up
        double distance = buf;
        buf = Distance(a.GetPositionX(),a.GetPositionY(),(b.GetPositionX()+b.GetWidth()/2),(b.GetPositionY() + b.GetHight()/2)); //right up
        if(distance<buf) {
            distance = buf;
        }
        buf = Distance(a.GetPositionX(),a.GetPositionY(),(b.GetPositionX()-b.GetWidth()/2),(b.GetPositionY() - b.GetHight()/2)); //lefu down
        if(distance<buf) {
            distance = buf;
        }
        buf = Distance(a.GetPositionX(),a.GetPositionY(),(b.GetPositionX()-b.GetWidth()/2),(b.GetPositionY() - b.GetHight()/2)); //Right down
        if(distance<buf) {
            distance = buf;
        }
        return  distance;
    }
    private boolean isSameQuadrant(GameObject a, GameObject b) {
        double circleX = a.GetPositionX();
        double circleY = a.GetPositionY();
        double LeftX = b.GetPositionX() - b.GetWidth()/2 - circleX;
        double LeftY = b.GetPositionY() + b.GetHight()/2 - circleY;
        double RightX = b.GetPositionX() + b.GetWidth()/2 - circleX;
        double RightY = b.GetPositionY() - b.GetHight()/2 - circleY;

        if(LeftX>0 &&RightX>0) {
            if((LeftY>0 && RightY>0)|| (LeftY<0 && RightY<0)){
                return  true;
            }
            return false;
        }
        else if(LeftX<0&& RightX<0) {
            if((LeftY>0 && RightY>0)|| (LeftY<0 && RightY<0)){
                return  true;
            }
            return  false;
        }
        else {
            return  false;
        }

    }
    private double Distance(double x1,double y1,double x2,double y2 ) {
        return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }
    private double   RandomCoordinate(double  Start,double  End, Random aRandom) {
        long range = (long)End - (long)Start + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * aRandom.nextDouble());
        double  randomNumber =  (double )(fraction + Start);
        return randomNumber;
    }
    private int SetPoint(double input) {
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
        Random ran = new Random();
        List<GameObject> objList = new ArrayList<>();
        int Counter = 0; // check collision 次數
        int Collision_times = 20;
        Obstacle create  = new Obstacle(0,0);
        create.SetPosition(5,10);
        create.SetHightandWidth(20,50);
        double Obstacle_MapX = MapX-2*create.GetWidth();
        double Obstacle_MapY = MapY-2*create.GetHight();
        double  obstacle_frame = Math.sqrt((Obstacle_MapX *Obstacle_MapY )/Total_Obstacle);
        double offsetX = create.GetPositionX();
        double offsetY = create.GetPositionY();
        for(double  i = obstacle_frame,j = obstacle_frame,count =0;count<Total_Obstacle;i+=obstacle_frame,count ++,Counter = 0) {
            if(i>=Obstacle_MapX) {
                i = obstacle_frame;
                j += obstacle_frame;
                if(j>=Obstacle_MapY) {
                    j =(int) Obstacle_MapY;
                }
            }
            double  x = RandomCoordinate(i-obstacle_frame,i,ran);
            double  y = RandomCoordinate(j-obstacle_frame,j,ran);

            Obstacle buf = new Obstacle (x+offsetX,y+offsetY);
            for(int q = 0;q<objList.size();q++) {
                if(Collision(objList.get(q),buf) ){
                    x = RandomCoordinate(i-obstacle_frame,i,ran);
                    y = RandomCoordinate(j-obstacle_frame,j,ran);
                    buf.SetPosition(x+offsetX,y+offsetY);
                    q = 0;
                    Counter ++;
                }
                if(Counter>Collision_times) {
                    break;
                }
            }
            if(Counter<=Collision_times) {
                objList.add(buf);
            }
        }
        /*int coin_frame = (int)Math.sqrt((MapX * MapY)/Total_Coin);
        for(int i = coin_frame,j = coin_frame,count =0;count<Total_Coin;count ++) {
            if(i>=MapX) {
                i= coin_frame;
                j+=coin_frame;
                if(j>=MapY) {
                    j =(int) MapY;
                }
            }
            double  x = RandomCoordinate(i-coin_frame,i,ran);
            double  y = RandomCoordinate(j-coin_frame,j,ran);
            Coin buf = new Coin (x,y);
            buf.setPoint(SetPoint(ran.nextDouble()));
            i+=coin_frame;
            objList.add(buf);
        }
        int item_frame = (int)Math.sqrt((MapX * MapY)/Total_Item);
        for(int i = item_frame,j = item_frame,count =0;count<Total_Item;count ++) {
            if(i>=MapX) {
                i= item_frame;
                j+=item_frame;
                if(j>=MapY) {
                    j =(int) MapY;
                }
            }
            double x = RandomCoordinate(i-item_frame,i,ran);
            double  y = RandomCoordinate(j-item_frame,j,ran);
            Item buf = new Item (x,y);
            i+=item_frame;
            objList.add(buf);
        }*/
        System.out.println(objList.size());
        for(int i=0;i<objList.size();i++) {
            GameObject a = objList.get(i);
            System.out.println(a.GetPositionX() +" "+ a.GetPositionY());
        }
    }
}

