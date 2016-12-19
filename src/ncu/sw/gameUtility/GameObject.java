package ncu.sw.gameUtility;



/**
 * Created by Vincent on 10/10/2016.
 */
public abstract class GameObject implements java.io.Serializable {
    private int positionX;
    private int positionY;
    private int height;
    private int width;
    private int attribute; // 加了一個attribute
    private int identity;
    public GameObject(int x,int y,int h,int w) {
        positionX = x;
        positionY = y;
        height = h;
        width = w;
    }
    public void setAttribute(int a) {
        this.attribute = a;
    }
    public int getAttribute() {
        return this.attribute;
    }
    public void setPosition(int X,int Y) {
        this.positionX = X;
        this.positionY = Y;
    }
    public void setHeightandWidth(int height,int width) { //for test
        this.height = height;
        this.width = width;
    }
    public int getPositionX() {
        return  this.positionX;
    }
    public int getPositionY() {
        return  this.positionY;
    }
    public int getHeight() {
        return  height;
    }
    public int getWidth() {
        return  width;
    }
}
