package ncu.sw.gameServer;
import ncu.sw.gameUtility.Coin;
import ncu.sw.gameUtility.Player;
import java.util.TimerTask;

/**
 * Created by Kevin on 2016/12/25.
 */
public class BulletTask extends TimerTask {
    private Coin bullet;
    private int speed = 40;
    private int cnt = 40;
    private int xDir,yDir;

    public BulletTask( Player player, int dir){
        this.xDir = ServerGameController.getInstance().getMoveXMap().get(dir);
        this.yDir = ServerGameController.getInstance().getMoveYMap().get(dir);
        bullet = new Coin( player.getPositionX()+ xDir *player.getWidth()  ,player.getPositionY() + yDir *player.getWidth() );
        if(player.getScore() == 1) {
            bullet.setPoint(1);
        }
        else {
            bullet.setPoint( player.getScore()/2 );
        }
        ServerGameController.getInstance().getCmd().getCoinArrayList().add( bullet);
    }
    @Override
    public void run() {
        if( cnt > 0 ){
            bullet.setPosition( bullet.getPositionX() + xDir * speed, bullet.getPositionY() + yDir * speed );
            for (Player p : ServerGameController.getInstance().getCmd().getPlayerArrayList()){
                if( ServerGameController.getInstance().isOverlay( p , bullet) ){
                    p.setScore( p.getScore() + bullet.getPoint() );
                    ServerGameController.getInstance().isEightySeven(p);
                    ServerGameController.getInstance().getCmd().getCoinArrayList().remove( bullet );
                    cancel();
                }
                else if (ServerGameController.getInstance().isOverlayWithObstacle(bullet) || ServerGameController.getInstance().boundary(bullet)) {
                    ServerGameController.getInstance().getCmd().getCoinArrayList().remove(bullet);
                    cancel();
                }
            }
            cnt--;
        }else{
            ServerGameController.getInstance().getCmd().getCoinArrayList().remove( bullet );
            cancel();
        }
    }
}
