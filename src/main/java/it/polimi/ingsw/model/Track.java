package it.polimi.ingsw.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class initialize the standard track, reading the details from a file
 * Use of the patter singleton -> the track is the same for each game -> if it can change from game to game ->
 *  ->map with key the idGame and with value the instance of the Track
 */
public class Track {
    private static Track instanceOfTrack = null;
    private static Position[] track = null;


    /**
     * Initialize the common track, taking the info from "positionTrack.txt"
     * @index , @point , @isPopeSection , @numPopeSection , @isPopeSection are the future attribute of a Position object
     * i is the current position
     * j is used to determine which attribute I'm reading
     */
    private Track(){
        File info = new File("C:\\Users\\Matteo Nunziante\\IdeaProjects\\ing-sw-2021-musumeci-nunziante-oldani\\src\\main\\resources\\positionTrack.txt");
        String string;
        int index = 0;
        int point = 0;
        int isPopeSection = 0;
        int numPopeSection = 0;
        int isPopePosition = 0;
        int i = 0;
        int j = 0;
        try{
            Scanner scanner = new Scanner(info);
            track = new Position[25];

            while(scanner.hasNextLine() && i < track.length){
                string = scanner.nextLine();
                if(string.charAt(0) != '#'){
                    if(j == 0){
                        index = Integer.parseInt(string);
                    }
                    else if(j == 1)
                        point = Integer.parseInt(string);
                    else if(j == 2)
                        isPopeSection = Integer.parseInt(string);
                    else if(j == 3)
                        numPopeSection = Integer.parseInt(string);
                    else{
                        isPopePosition = Integer.parseInt(string);
                        //System.out.println(index + "," + point + "," + isPopeSection + "," + numPopeSection + "," + isPopePosition);
                        track[i] = new Position(index , point , (isPopeSection == 1) , numPopeSection , (isPopePosition == 1));
                        j = -1;
                        i++;
                    }
                    j++;
                }
            }
            scanner.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /* Just for a fast test of the file
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
