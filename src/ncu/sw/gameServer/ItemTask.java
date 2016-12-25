package ncu.sw.gameServer;

import ncu.sw.gameUtility.Player;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by NiHao on 2016/12/25.
 */
public class ItemTask extends TimerTask {
    private int speed;
    private int radius;
    private int moveDir = 1;
    private Player p;
    private int taskCnt = 1;

    public ItemTask(Player p ){
        this.p = p;
        speed = p.getSpeed();
        radius = p.getRadius();
        moveDir = p.getMoveDir();
    }
    @Override
    public void run() {
        if( taskCnt > 0){
            System.out.println( "ItemTask run");
            p.setSpeed( this.speed );
            p.setRadius( this.radius );
            p.setMoveDir( this.moveDir );
            p.setEffectNum(0);
            --taskCnt;
        }
        else{
            System.out.println( "ItemTask cancel");
            cancel();
        }

    }
}
