package NTU.sw.gameUtility;

import javafx.scene.image.Image;

/**
 * Created by Arson on 2016/11/1.
 */
public class Player extends GameObject{

    private String identity;
    public Player(double x,double y,Image i,String id){
        super(x,y,i);
        identity = id;
        this.SetAttribute(0);
    }

    @Override
    public void collideResponse() {

    }
}
