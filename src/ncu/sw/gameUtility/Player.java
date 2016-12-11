package ncu.sw.gameUtility;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Arson on 2016/11/1.
 */
public class Player extends GameObject{
    private String identity;
    private int score;
    private InetAddress address;
    public Player(int x,int y,String id, String bufAddress){
        super(x,y,20,20);
        identity = id;
        try {
            address = InetAddress.getByName(bufAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setAttribute(0);
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

}
