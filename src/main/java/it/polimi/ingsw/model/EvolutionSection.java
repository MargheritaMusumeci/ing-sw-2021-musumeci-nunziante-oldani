package it.polimi.ingsw.model;

import java.util.ArrayList;

public class EvolutionSection {

    private ArrayList<EvolutionCard>[][] evolutionSection;

    /**
     * constructor that creates the section in which EvolutionCards are stored and instantiates every card
     */
    public EvolutionSection(){
        this.evolutionSection = null;
    }

    /**
     * @return the fist card of each section that can be bought
     */
    public EvolutionCard[][] canBuy(){

        //fake object just to resolve errors before implementing the method
        EvolutionCard[][] c = new EvolutionCard[1][1];
        return c;
    }

    /**
     *
     * @param row indicates the row in which the card to buy is
     * @param col indicates the row in which the card to buy is
     * @return the card bought
     */
    public EvolutionCard buy(int row, int col){
        //fake object just to resolve errors before implementing the method
        EvolutionCard c = new EvolutionCard(1,1,1,ResourceType.COIN, null,null);
        return c;
    }




}
