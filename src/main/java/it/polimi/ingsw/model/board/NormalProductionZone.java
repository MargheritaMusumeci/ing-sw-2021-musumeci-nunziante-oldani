package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.InvalidPlaceException;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LevelEnum;

import java.util.ArrayList;


/**
 * Place of the dashboard where are stored Evolution Cards
 */
public class NormalProductionZone extends ProductionZone{

    /**
     * List of cards bought
     */
    private ArrayList<EvolutionCard> cards;

    public NormalProductionZone() {
        cards = new ArrayList<>();
    }

    /**
     * @return the most recent card bought
     */
    public Card getCard() {
        return cards.isEmpty() ? null : cards.get(0);
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
     * Add new card to production zone
     * @param card bought from Evolution Section
     */
    public void addCard(Card card) throws InvalidPlaceException {
        EvolutionCard evolutionCard = (EvolutionCard) card;
        if (isFull() ||
                (!(cards.isEmpty()) && ( compare(cards.get(0).getLevel())!= (compare(evolutionCard.getLevel()) - 1))) ||
                ((cards.isEmpty()) && (evolutionCard.getLevel()!= LevelEnum.FIRST))) {
            throw new InvalidPlaceException("Invalid Position for adding the card");
        }
        cards.add(0, (EvolutionCard) card);

        notifyProductionZoneListener(this);
    }

    /**
     * @return array of Evolution Cards present in the slot
     */
    public ArrayList<Card> getCardList() {
        return cards.isEmpty() ? null : (ArrayList<Card>) cards.clone();
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
