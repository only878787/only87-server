package NTU.sw.gameGui;

import NTU.sw.gameUtility.GameObject;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Random;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        Scene gameScene = new Scene(root,1000,800);
        primaryStage.setTitle("Game Scene");
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        //launch(args);
      // ServerGameController server = new ServerGameController(100,20,20);
        Initial a = new Initial(10,100,10);

    }
}
