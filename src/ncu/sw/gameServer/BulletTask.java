package ncu.sw.gameServer;

import ncu.sw.gameUtility.Coin;
import ncu.sw.gameUtility.Player;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kevin on 2016/12/25.
 */
public class BulletTask extends TimerTask {
    private Coin bullet;
    private int speed = 60;
    private int cnt = 6;
    private int xDir,yDir;

    public BulletTask( int x, int y ,int score, int dir){

        this.xDir = ServerGameController.getInstance().getMoveXMap().get(dir);
        this.yDir = ServerGameController.getInstance().getMoveYMap().get(dir);
        bullet = new Coin( x+ xDir * speed,y + yDir * speed );
        bullet.setPoint( score );
        ServerGameController.getInstance().getCmd().getCoinArrayList().add( bullet);
    }

    @Override
    public void run() {
        if( cnt > 0 ){
            //System.out.println("BulletTask RUN IF");
            bullet.setPosition( bullet.getPositionX() + xDir * speed, bullet.getPositionY() + yDir * speed );
            for (Player p : ServerGameController.getInstance().getCmd().getPlayerArrayList()){
                if( ServerGameController.getInstance().isOverlay( p , bullet) ){
                    p.setScore( p.getScore() + bullet.getPoint() );
                    ServerGameController.getInstance().isEightySeven(p);
                    ServerGameController.getInstance().getCmd().getCoinArrayList().remove( bullet );
                    cancel();
                }
            }
            cnt--;
        }else{
            //System.out.println("BulletTask RUN Else");
            ServerGameController.getInstance().getCmd().getCoinArrayList().remove( bullet );
            cancel();
        }
    }
}
