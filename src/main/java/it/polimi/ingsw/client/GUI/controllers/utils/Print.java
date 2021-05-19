package it.polimi.ingsw.client.GUI.controllers.utils;

import it.polimi.ingsw.model.game.Resource;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Print {

    public Image fromPathToImageLeader(String path){
        URL url = null;
        try {
            url = new File("src/main/resources/images/leaderCards/" + path + ".png").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new Image(String.valueOf(url));
    }

    public Image fromPathToImageResource(String path){
        URL url = null;
        try {
            url = new File("src/main/resources/images/resources/" + path ).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new Image(String.valueOf(url));
    }

    public Color colorFromResource(Resource resource){
        switch(resource){
            case COIN : return Color.YELLOW;
            case ROCK: return Color.GRAY;
            case FAITH: return Color.RED;
            case SHIELD: return Color.BLUE;
            case SERVANT: return Color.PURPLE;
            default: return Color.WHITE;
        }
    }

    public String pathFromResource(Resource resource){

        switch (resource) {
            case COIN: return "coin.png";
            case ROCK: return "stone.png";
            case SHIELD: return "shield.png";
            case SERVANT: return "servant.png";
        }
        return null;
    }
}
