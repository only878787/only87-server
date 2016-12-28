package ncu.sw.gameUtility;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by NiHao on 2016/11/12.
 */
public class Cmd implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<Coin> coinArrayList;
    private ArrayList<Item> itemArrayList;
    private ArrayList<Obstacle> obstacleArrayList;
    private ArrayList<Player> playerArrayList;

    public Cmd(){
        coinArrayList = new ArrayList<Coin>();
        itemArrayList = new ArrayList<Item>();
        obstacleArrayList = new ArrayList<Obstacle>();
        playerArrayList = new ArrayList<Player>();
    }
    public ArrayList<Coin> getCoinArrayList() {
        return coinArrayList;
    }

    public void setCoinArrayList(ArrayList<Coin> coinArrayList) {
        this.coinArrayList = coinArrayList;
    }

    public ArrayList<Item> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(ArrayList<Item> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    public ArrayList<Obstacle> getObstacleArrayList() {
        return obstacleArrayList;
    }

    public void setObstacleArrayList(ArrayList<Obstacle> obstacleArrayList) {
        this.obstacleArrayList = obstacleArrayList;
    }

    public ArrayList<Player> getPlayerArrayList() {
        return playerArrayList;
    }

    public void setPlayerArrayList(ArrayList<Player> playerArrayList) {
        this.playerArrayList = playerArrayList;
    }
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeObject(coinArrayList);
        stream.writeObject(itemArrayList);
        stream.writeObject(obstacleArrayList);
        stream.writeObject(playerArrayList);
    }
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        coinArrayList = (ArrayList<Coin>) stream.readObject();
        itemArrayList = (ArrayList<Item>) stream.readObject();
        obstacleArrayList = (ArrayList<Obstacle>) stream.readObject();
        playerArrayList = (ArrayList<Player>) stream.readObject();
    }
    @Override
    public String toString() {
        return "coinArrayList=" + coinArrayList + ";   itemArrayList="
                + itemArrayList + ";   obstacleArrayList=" + obstacleArrayList
                + ";   playerArrayList=" + playerArrayList;
    }
}
