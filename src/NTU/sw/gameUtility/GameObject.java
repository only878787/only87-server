package NTU.sw.gameUtility;

import javafx.scene.image.Image;


/**
 * Created by Vincent on 10/10/2016.
 */
public abstract class GameObject {
    private double positionX;
    private double positionY;
    private Image image;
    private double height;
    private double width;
    private int attribute; // 加了一個attribute
    public GameObject(double x,double y,Image i){
        positionX = x;
        positionY = y;
        image = i;
        height = image.getHeight();
        width = image.getWidth();
    }
    public GameObject(double x,double y) {
        positionX = x;
        positionY = y;
    }
    public void SetAttribute(int a) {
        this.attribute = a;
    }
    public int GetAttribute() {
        return this.attribute;
    }
    public void SetPosition(double X,double Y) {
        this.positionX = X;
        this.positionY = Y;
    }
    public void SetHightandWidth(double height,double width) { //for test
        this.height = height;
        this.width = width;
    }
    public double GetPositionX() {
        return  this.positionX;
    }
    public double GetPositionY() {
        return  this.positionY;
    }
    public double GetHight() {
        return  height;
    }
    public double GetWidth() {
        return  width;
    }
    public abstract void collideResponse();
}
