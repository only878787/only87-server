package NTU.sw.gameUtility;

import javafx.scene.image.Image;

/**
 * Created by Arson on 2016/11/1.
 */
public class Item extends  GameObject{

    private int efect;
    public Item(double x, double y, Image i, int e){
        super(x,y,i);
        efect = e;
        this.SetAttribute(0);
    }
    public Item(double x,double y) {
        super(x,y);
        this.SetAttribute(0);
    }
    public int getEfect(){
        return efect;
    }
    public void setEfect(int e){
        efect = e;
    }

    @Override
    public void collideResponse() {

    }
}
