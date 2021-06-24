package it.polimi.ingsw.client.gui.controllers.utils;

import it.polimi.ingsw.model.game.Resource;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;

import java.net.URL;

/**
 * Class that contains all methods for print the correct image from path
 */
public class Print {

    /**
     * Method that prints leader card image from leader card id
     * @param path id of leader card
     * @return leader image
     */
    public Image fromPathToImageLeader(String path){
        URL url= getClass().getClassLoader().getResource("images/leaderCards/" + path + ".png");
        return new Image(String.valueOf(url));
    }

    /**
     * Method that prints resource image from resource name
     * @param path resource name .png
     * @return resource Image
     */
    public Image fromPathToImageResource(String path){
        URL url= getClass().getClassLoader().getResource("images/resources/" + path);
        return new Image(String.valueOf(url));
    }

    /**
     * Take the correct evolutionCard
     * @param id of the card
     * @return the image of the card
     */
    public Image fromPathToImageEvolution(int id){
        String path = String.valueOf(id);
        URL url= getClass().getClassLoader().getResource("images/evolutionCards/" + path + ".png");
        return new Image(String.valueOf(url));
    }

    /**
     * Method that prints cross image
     * @return gamer cross Image
     */
    public Image popePosition(){
        URL url= getClass().getClassLoader().getResource("images/popeTrack/croce_giocatore.png");
        return new Image(String.valueOf(url));
    }

    /**
     * Method that fills market sphere from name of resource
     * @param resource resource name
     * @return material color
     */
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

    /**
     * Method that prints
     * @param resource Resource name
     * @return path
     */
    public String pathFromResource(Resource resource){

        switch (resource) {
            case COIN: return "coin.png";
            case ROCK: return "stone.png";
            case SHIELD: return "shield.png";
            case SERVANT: return "servant.png";
        }
        return null;
    }

    /**
     * Method that returns the correct color from resouce name
     * @param resource resource name
     * @return color of resource
     */
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

    /**
     * Method that prints crosses
     * @return image with the two cross
     */
    public Image togetherPopePosition() {
        URL url= getClass().getClassLoader().getResource("images/popeTrack/croci.png");
        return new Image(String.valueOf(url));
    }

    /**
     * Method that print lorenzo crosses
     * @return image of lorenzo cross
     */
    public Image lorenzoPopePosition() {
        URL url= getClass().getClassLoader().getResource("images/popeTrack/croce_nera.png");
        return new Image(String.valueOf(url));
    }

    /**
     * Method that prints Pope Card from path
     * @param path name of file in which is stored the image
     * @return image of pope card
     */
    public Image fromPathToPopeCard(String path){
        URL url= getClass().getClassLoader().getResource("images/popeTrack/" + path);
        return new Image(String.valueOf(url));
    }

    /**
     * Method that return inactive pope card from pope card position
     * @param position pope card position
     * @return path of inactive pope card
     */
    public String fromPositionToDiscardPope(Integer position){
        switch(position){
            case 0: return "quadrato_giallo.png";
            case 1: return "quadrato_arancione.png";
            case 2: return "quadrato_rosso.png";
        }
        return null;
    }

    /**
     * Method that return active pope card from pope card position
     * @param position pope card position
     * @return path of active pope card
     */
    public String fromPositionToActivePope(Integer position){
        switch(position){
            case 0: return "punti_giallo.png";
            case 1: return "punti_arancione.png";
            case 2: return "punti_rosso.png";
        }
        return null;
    }

}
