package it.polimi.ingsw.model.popeTrack;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.Objects;

/**
 * This class initialize the standard track, reading the details from a json file
 * Use of the patter singleton -> the track is the same for each game -> if it can change from game to game ->
 *  ->map with key the idGame and with value the instance of the Track
 */
public class Track implements Serializable{
    private static Track instanceOfTrack = null;
    private static Position[] track = null;

    /**
     * Initialize the common track, taking the info from "positionTrack.json"
     */
    private Track(){
        track = new Gson().fromJson(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("json/positionTrack.json"))), Position[].class);
    }


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
