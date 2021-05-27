package it.polimi.ingsw.client.GUI.controllers.utils;

import it.polimi.ingsw.model.game.Resource;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;

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

    /**
     * Take the correct evolutionCard
     * @param id of the card
     * @return the image of the card
     */
    public Image fromPathToImageEvolution(int id){
        URL url = null;
        try {
            String path = String.valueOf(id);
            url = new File("src/main/resources/images/evolutionCards/" + path + ".png").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new Image(String.valueOf(url));
    }

    public Image popePosition(){
        URL url = null;
        try {
            url = new File("src/main/resources/images/popeTrack/croce_giocatore.png").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new Image(String.valueOf(url));
    }

    public Material materialFromResource(Resource resource) {

        PhongMaterial material = new PhongMaterial();
        switch (resource) {
            case COIN: {
                material.setDiffuseColor(Color.rgb(247, 210, 57));
                material.setSpecularColor(Color.BLACK);
                return material;
            }
            case ROCK: {
                material.setDiffuseColor(Color.rgb(137, 135, 130));
                material.setSpecularColor(Color.BLACK);
                return material;
            }
            case FAITH: {
                material.setDiffuseColor(Color.rgb(225, 79, 53));
                material.setSpecularColor(Color.BLACK);
                return material;
            }
            case SHIELD: {
                material.setDiffuseColor(Color.rgb(32, 178, 227));
                material.setSpecularColor(Color.BLACK);
                return material;
            }
            case SERVANT: {
                material.setDiffuseColor(Color.rgb(110, 98, 162));
                material.setSpecularColor(Color.BLACK);
                return material;
            }
            default: {
                material.setDiffuseColor(Color.WHITE);
                material.setSpecularColor(Color.BLACK);
                return material;
            }
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

    public Color colorFromResource(Resource resource){
        switch (resource) {
            case COIN: {
               return (Color.rgb(247, 210, 57));
            }
            case ROCK: {
                return (Color.rgb(137, 135, 130));
            }
            case SHIELD: {
                return (Color.rgb(32, 178, 227));
            }
            case SERVANT: {
                return (Color.rgb(110, 98, 162));
            }
            default: {
              return Color.WHITE;
            }
        }
    }

    public Image togetherPopePosition() {
        URL url = null;
        try {
            url = new File("src/main/resources/images/popeTrack/croci.png").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new Image(String.valueOf(url));
    }

    public Image lorenzoPopePosition() {
        URL url = null;
        try {
            url = new File("src/main/resources/images/popeTrack/croce_nera.png").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new Image(String.valueOf(url));
    }

    public Image fromPathToPopeCard(String path){
        URL url = null;
        try {
            url = new File("src/main/resources/images/popeTrack/" + path).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new Image(String.valueOf(url));
    }

    public String fromPositionToDiscardPope(Integer position){
        switch(position){
            case 0: return "quadrato_giallo.png";
            case 1: return "quadrato_arancione.png";
            case 2: return "quadrato_rosso.png";
        }
        return null;
    }

    public String fromPositionToActivePope(Integer position){
        switch(position){
            case 0: return "punti_giallo.png";
            case 1: return "punti_arancione.png";
            case 2: return "punti_rosso.png";
        }
        return null;
    }

}
