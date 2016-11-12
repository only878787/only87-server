package NTU.sw.gameGui;

import NTU.sw.gameUtility.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Arson on 2016/11/1.
 */
public class ServerGameController {
    private List<GameObject> objList;
    private List<GameObject> playerList;

    private int Total_Coin;
    private int Total_Obstacle;
    private int Total_Item;
    private double MapX = 5000;
    private double MapY = 3000;

    public ServerGameController(int total_Coin, int total_Obstacle, int total_Item) {
        this.Total_Coin = total_Coin;
        this.Total_Item = total_Item;
        this.Total_Obstacle = total_Obstacle;
        // this.randomMap();
    }


}
