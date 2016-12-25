package ncu.sw.gameUtility;


import java.net.InetAddress;

/**
 * Created by Arson on 2016/11/1.
 */
public class Player extends GameObject{
    private String identity;
    private int score;
    private InetAddress address;
    private int count87;
    private int speed;

    public void setCount87(int count87) {
        this.count87 = count87;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Player(int x, int y, String id, InetAddress bufAddress){
        super(x,y,50,50);
        identity = id;
        address = bufAddress;
        setAttribute(0);
        count87  = 0;
        speed = 20;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String getId() {
        return identity;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getCount87() {
        return count87;
    }
}
