package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * section of the dashboard in which are showed all the Development cards bought by the player
 */
public class ProductionZone {

    private ArrayList<EvolutionCard> cards;

    public ProductionZone() {
        cards = new ArrayList<>();
    }

    /**
     *
     * @return first card of the production section
     */
    public EvolutionCard getCard() {
            return cards.isEmpty() ? null : cards.get(0);
        }

    /**
     * if possible add a card to production section
     * @param card     new card bought from EvolutionSection
     */
    public void addCard(EvolutionCard card) {
        if (isFull() ||
                (!(cards.isEmpty()) && ( compare(cards.get(0).getLevel())!= (compare(card.getLevel()) - 1))) ||
                ((cards.isEmpty()) && (card.getLevel()!=LevelEnum.FIRST))) {
            return;
        }
        cards.add(0, card);
    }

    /**
     * check if there is at least a free space in the slot
     *
     * @return true if the slot is not full
     */
    public boolean isFull() {
            return cards.size() > 2;
    }

    /**
     * @return Array of Evolution Card
     */
    public ArrayList<EvolutionCard> getCardList() {
            return cards.isEmpty() ? null : (ArrayList<EvolutionCard>) cards.clone();
    }

    /**
     *
     * @return level of the highest card
     */
    public LevelEnum getLevel() {
            return cards.isEmpty() ? null : cards.get(0).getLevel();
    }

    /**
     * method added just for simplify cheks in addCard
     * @param levelEnum enum variable
     * @return int represent level
     */
    public int compare(LevelEnum levelEnum){
        if(levelEnum.equals(LevelEnum.FIRST)){return 1;}
        else if(levelEnum.equals(LevelEnum.SECOND)){return 2;}
        else{return 3;}
    }
}




