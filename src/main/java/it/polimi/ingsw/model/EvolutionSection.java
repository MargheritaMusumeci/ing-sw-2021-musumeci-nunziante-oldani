package it.polimi.ingsw.model;

import java.util.ArrayList;

public class EvolutionSection {

    private ArrayList<EvolutionCard>[][] evolutionSection;

    /**
     * constructor that creates the section in which EvolutionCards are stored and instantiates every card
     */
    public EvolutionSection(){

        evolutionSection = new ArrayList[3][4];

        for (int i=0; i<evolutionSection.length; i++){
            for(int j=0; j<evolutionSection[i].length; j++){
                evolutionSection[i][j] = new ArrayList<EvolutionCard>();

            }
        }

        populateSection();

    }

    /**
     * method that creates the instances for every card
     */
    private void populateSection(){

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




}
