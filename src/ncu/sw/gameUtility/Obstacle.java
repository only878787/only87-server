package ncu.sw.gameUtility;

import javafx.scene.image.Image;

/**
 * Created by Arson on 2016/11/1.
 */
public class Obstacle extends GameObject{

    public Obstacle(double x, double y, Image i){
        super(x,y,i);
        this.SetAttribute(1);
    }

    public Obstacle(double x,double y) {
        super(x,y);
        this.SetAttribute(1);
    }
    @Override
    public void collideResponse() {

    }
}
