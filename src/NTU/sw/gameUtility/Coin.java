package NTU.sw.gameUtility;

import javafx.scene.image.Image;

/**
 * Created by Arson on 2016/11/1.
 */
public class Coin extends  GameObject{

    private int point;

   public Coin(double x,double y) {
       super(x,y);
       this.SetAttribute(0);
   }
    public Coin(double x,double y,Image i,int p){
        super(x,y,i);
        point = p;
        this.SetAttribute(0);
    }

    public int getPoint(){
        return point;
    }
    public void setPoint(int p){
        point = p;
    }

    @Override
    public void collideResponse() {

    }
}
