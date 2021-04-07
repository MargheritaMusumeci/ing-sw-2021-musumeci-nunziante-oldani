package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * This class initialize the standard track, reading the details from a json file
 * Use of the patter singleton -> the track is the same for each game -> if it can change from game to game ->
 *  ->map with key the idGame and with value the instance of the Track
 */
public class Track {
    private static Track instanceOfTrack = null;
    private static Position[] track = null;

    /**
     * Initialize the common track, taking the info from "positionTrack.json"
     */
    private Track(){
        String path = "C:\\Users\\Matteo Nunziante\\IdeaProjects\\ing-sw-2021-musumeci-nunziante-oldani\\src\\main\\resources\\positionTrack.json";
        try {
            JsonReader reader = new JsonReader(new FileReader(path));
            track = new Gson().fromJson(reader , Position[].class);
            /*for (Position p:track) {
                System.out.println(p.getIndex() + " " + p.getPoint() + " " + p.getPopeSection() + " " + p.getNumPopeSection() + " " + p.getPopePosition());
            }*/
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /*//Just for a fast test of the file
    public static void main(String[] args) {
        new Track();
        return;
    }*/

    /**
     *
     * @return the single instance of the track, if it doesn't exist invoke the private constructor
     */
    public static Track getInstanceOfTrack(){
        if(instanceOfTrack == null){
            instanceOfTrack = new Track();
        }
        return instanceOfTrack;
    }

    /**
     *
     * @return the track
     */
    public Position[] getTrack(){
        return instanceOfTrack.track.clone();
    }

}
