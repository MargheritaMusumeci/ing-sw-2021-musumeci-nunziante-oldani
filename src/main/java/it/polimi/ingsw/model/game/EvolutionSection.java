package it.polimi.ingsw.model.game;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.osservables.EvolutionSectionOsservable;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class EvolutionSection extends EvolutionSectionOsservable implements Serializable{

    private ArrayList<EvolutionCard>[][] evolutionSection;
    //if we want to implement multigames we need an hashmap of instances related to the gameid

    /**
     * constructor that creates the section in which EvolutionCards are stored and instantiates every card
     */

    public EvolutionSection(){

        evolutionSection = new ArrayList[3][4];

        for (int i=0; i<evolutionSection.length; i++){
            for(int j=0; j<evolutionSection[i].length; j++){
                evolutionSection[i][j] = new ArrayList<>();

            }
        }

        populateSection();

    }

    private void populateSection(){
        EvolutionCard[] evolutionCards = new Gson().fromJson(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("json/productionCards.json"))), EvolutionCard[].class);

        //actually putting the card into the section
        int counter = 0;
        //per ogni riga
            //per oni colonna
                //per ogni posizione ne faccio 4
        for (int i=0; i<evolutionSection.length; i++){
            for(int j=0; j<evolutionSection[i].length; j++){
                for(int k=0; k<4; k++){
                    evolutionSection[i][j].add(k, evolutionCards[counter]);
                    counter++;
                }
            }
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
    public EvolutionCard buy(int row, int col) throws ExcessOfPositionException {
        if(row < 0 || row >= evolutionSection.length || col < 0 || col >= evolutionSection[0].length || evolutionSection[row][col].size() <= 0){
            throw new ExcessOfPositionException("Invalid Position, no card available to be bought");
        }
        EvolutionCard c = evolutionSection[row][col].get(0);
        evolutionSection[row][col].remove(0);

        notifyEvolutionSectionListener(this);
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

    public ArrayList<EvolutionCard>[][] getEvolutionSection() {
        return evolutionSection;
    }

    /**
     * method that allow the loading of a preconfigured evolution section
     * @param evolutionSection is the evolution section to be loaded
     */
    public void setEvolutionSection(ArrayList<EvolutionCard>[][] evolutionSection){
        this.evolutionSection = evolutionSection;
    }


}
