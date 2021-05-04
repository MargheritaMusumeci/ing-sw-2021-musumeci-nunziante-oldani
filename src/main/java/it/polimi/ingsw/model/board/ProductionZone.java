package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.exception.InvalidPlaceException;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LevelEnum;

import java.util.ArrayList;

/**
 * Place of the dashboard where are stored Evolution Cards
 */
public class ProductionZone {

    /**
     * List of cards bought
     */
    private ArrayList<EvolutionCard> cards;

    public ProductionZone() {
        cards = new ArrayList<>();
    }

    /**
     * @return the most recent card bought
     */
    public EvolutionCard getCard() {
            return cards.isEmpty() ? null : cards.get(0);
        }

    /**
     * Add new card to production zone
     * @param card bought from Evolution Section
     */
    public void addCard(EvolutionCard card) throws InvalidPlaceException {
        if (isFull() ||
                (!(cards.isEmpty()) && ( compare(cards.get(0).getLevel())!= (compare(card.getLevel()) - 1))) ||
                ((cards.isEmpty()) && (card.getLevel()!= LevelEnum.FIRST))) {
            throw new InvalidPlaceException("Invalid Position for adding the card");
        }
        cards.add(0, card);
    }

    /**
     * Check if there is at least a free space in the slot
     *
     * @return true if the slot isn't full
     */
    public boolean isFull() {
            return cards.size() > 2;
    }

    /**
     * @return array of Evolution Cards present in the slot
     */
    public ArrayList<EvolutionCard> getCardList() {
            return cards.isEmpty() ? null : (ArrayList<EvolutionCard>) cards.clone();
    }

    /**
     *
     * @return level of the last card
     */
    public LevelEnum getLevel() {
            return cards.isEmpty() ? null : cards.get(0).getLevel();
    }

    /**
     * Method added just for simplify checks in addCard
     * @param levelEnum enum variable
     * @return int represent level
     */
    private int compare(LevelEnum levelEnum){
        if(levelEnum.equals(LevelEnum.FIRST)){return 1;}
        else if(levelEnum.equals(LevelEnum.SECOND)){return 2;}
        else{return 3;}
    }
}




