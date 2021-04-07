package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

public class EvolutionSection {

    private ArrayList<EvolutionCard>[][] evolutionSection;
    //if we want to implement multigames we need an hashmap of instances related to the gameid
    private static EvolutionSection instanceOfEvolutionSection = null;

    /**
     * constructor that creates the section in which EvolutionCards are stored and instantiates every card
     */

    private EvolutionSection(){

        evolutionSection = new ArrayList[3][4];

        for (int i=0; i<evolutionSection.length; i++){
            for(int j=0; j<evolutionSection[i].length; j++){
                evolutionSection[i][j] = new ArrayList<EvolutionCard>();

            }
        }

        populateSection();

    }
    /**
     *
     * @return the single instance of the EvolutionSection, if it doesn't exist invoke the private constructor
     */
    //if we would impelemente the multigame we need the game id as argument of this method
    public static EvolutionSection getInstanceOfEvolutionSection(){
        if(instanceOfEvolutionSection == null){
            instanceOfEvolutionSection = new EvolutionSection();
        }
        return instanceOfEvolutionSection;
    }


    private void populateSection(){

        String path = "/Users/matteoldani/IdeaProjects/ing-sw-2021-musumeci-nunziante-oldani/src/main/resources/productionCards.json"; //need to find the correct path
        try {
            JsonReader reader = new JsonReader(new FileReader(path));
            EvolutionCard[] evolutionCards = new Gson().fromJson(reader, EvolutionCard[].class);

            //actually putting the card into the section
            int counter = 0;
            for (int i=0; i<evolutionSection.length; i++){
                for (int j=0; j<evolutionSection[i].length; j++){
                    for (int k=0; k<4; k++)
                        evolutionSection[i][j].add(k, evolutionCards[counter]);
                        counter++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * @return the fist card of each section that can be bought, if the cards in that postion are finished
     * the position is set to null
     */
    public EvolutionCard[][] canBuy(){

        EvolutionCard[][] c = new EvolutionCard[3][4];
        for (int i=0; i<evolutionSection.length; i++){
            for(int j=0; j<evolutionSection[i].length; j++){
                if(evolutionSection[i][j].size() > 0){
                    c[i][j] = evolutionSection[i][j].get(0);
                }else{
                    c[i][j] = null;
                }
            }
        }

        return c;
    }

    /**
     *
     * @param row indicates the row in which the card to buy is
     * @param col indicates the row in which the card to buy is
     * @return the card bought
     */
    public EvolutionCard buy(int row, int col){
        EvolutionCard c = evolutionSection[row][col].get(0);
        evolutionSection[row][col].remove(0);
        return c;
    }


    /**
     * Method uses for debugging purpose --> to be removed
     * @param row row of needed card
     * @param col column of needed card
     * @param pos pos inside the arraylist
     * @return the selected card
     */
    public EvolutionCard getCard(int row, int col, int pos){
        return evolutionSection[row][col].get(pos);
    }




}
