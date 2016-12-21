package ncu.sw.gameUtility;

import javafx.scene.image.Image;

/**
 * Created by Arson on 2016/11/1.
 */
public class Obstacle extends GameObject{

    public Obstacle(int x,int y) {
        super(x,y,50,50);
        setAttribute(1);
    }
}
