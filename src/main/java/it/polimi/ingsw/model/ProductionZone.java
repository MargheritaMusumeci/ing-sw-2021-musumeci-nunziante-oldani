package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * section of the dashboard in which are showed all the Development cards bought by the player
 */
public class ProductionZone {

    private ArrayList<EvolutionCard>[] cards;

    public ProductionZone() {
        cards = new ArrayList[3];
    }

    /**
     * each section of the production zone could contain at most 3 cards
     *
     * @param posizion distinguish throw the three slot of production
     * @return first Development card of the slot if present
     */
    public EvolutionCard getCard(int posizion) {
        switch (posizion) {
            case (1):
                return cards[0].get(0); //not sure about get method
            case (2):
                return cards[0].get(1);
            case (3):
                return cards[0].get(2);
            default:
                return null;
        }
    }

    /**
     *
     * @param card new card bought from EvolutionSection
     * @param position where card will be set
     * @return true if was successful
     */
    public boolean addCard(EvolutionCard card, int position) {
        if (!(isFull(position)) && ((position == 1) || (position == 2) || (position == 3)) && ((cards[position].get(0)).getLevel()==card.getLevel()+1)) {  //throw exeption ?
            cards[position].add(0, card);
            return true;
        } else return false;
    }

    /**
     * check if there is at least a free space in the slot
     * @param position number of the slot
     * @return true if the slot is not full
     */
    public boolean isFull(int position) {
        if ((position == 1) || (position == 2) || (position == 3)) {  //throw exeption ?
            return cards[position].contains(null);
        } else return false;
    }

    /**
     *
     * @param position number of the slot
     * @return Array of Evolution Card
     */
    public EvolutionCard[] getCardList(int position) {
        if ((position == 1) || (position == 2) || (position == 3)) {  //throw exeption ?
            return (EvolutionCard[]) cards[position].clone();
        } else return null;
    }
}

