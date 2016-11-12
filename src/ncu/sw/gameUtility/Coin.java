package ncu.sw.gameUtility;

import javafx.scene.image.Image;

/**
 * Created by Arson on 2016/11/1.
 */
public class Coin extends  GameObject{
    private int point;
    public Coin(int x,int y) {
       super(x,y,20,20);
       setAttribute(0);
   }
   public int getPoint(){
        return point;
   }
   public void setPoint(int p){
        point = p;
    }
}
