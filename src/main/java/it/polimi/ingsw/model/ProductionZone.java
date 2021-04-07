package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * section of the dashboard in which are showed all the Development cards bought by the player
 */
public class ProductionZone {

    private ArrayList<EvolutionCard>[] cards;

    public ProductionZone() {
        cards = new ArrayList[3];
        cards[0] = new ArrayList<>();
        cards[1] = new ArrayList<>();
        cards[2] = new ArrayList<>();
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
                return cards[0].isEmpty() ? null : cards[0].get(0);
            case (2):
                return cards[1].isEmpty() ? null : cards[1].get(0);
            case (3):
                return cards[2].isEmpty() ? null : cards[2].get(0);
            default:
                return null;
        }
    }

    /**
     * @param card     new card bought from EvolutionSection
     * @param position where card will be set
     */
    public void addCard(EvolutionCard card, int position) {
        if (!(isFull(position)) && ((position == 1) || (position == 2) || (position == 3)) && ((cards[position].get(0)).getLevel().ordinal() == card.getLevel().ordinal() + 1)) {  //throw exeption ?
            cards[position].add(0, card);
        }
    }

    /**
     * check if there is at least a free space in the slot
     *
     * @param position number of the slot
     * @return true if the slot is not full
     */
    public boolean isFull(int position) {
        if ((position == 1) || (position == 2) || (position == 3)) {  //throw exeption ?
            return cards[position].size() > 2;
        }
        return false;
    }

    /**
     * @param position number of the slot
     * @return Array of Evolution Card
     */
    public EvolutionCard[] getCardList(int position) {
        if ((position == 1) || (position == 2) || (position == 3)) {  //throw exeption ?
            return cards[position].isEmpty() ? null : (EvolutionCard[]) cards[position].clone();
        } else return null;
    }

    /**
     *
     * @param position number of the slot
     * @return level of the highest card
     */
    public LevelEnum getLevel(int position) {
        if ((position == 1) || (position == 2) || (position == 3)) {
            return cards[position].isEmpty() ? null : cards[position].get(0).getLevel();
        }
        return null;
    }
}




