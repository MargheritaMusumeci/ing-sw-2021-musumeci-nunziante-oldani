package it.polimi.ingsw.model.lorenzo;

import it.polimi.ingsw.model.cards.CardColor;

import java.io.Serializable;
import java.util.Optional;

/**
 * Create and Manage all possible Solo Action token
 */
public class LorenzoActionCard implements Serializable {

    private final LorenzoAction actionType;

    /**
     * If actionType is discard a Development Card, this attribute represents what kind of card will be discarded
     * else is null
     */
    private final Optional<CardColor> actionColor;

    /**
     * if actionType is move the black cross, this attribute represents how many spaces it has to be taken forward
     * else is null
     */
    private final Optional<Integer> num;

    /**
     * Costructor of 'move Black Cross' tokens equals to 'incrementPopeTrack'
     * @param num represents how many spaces it has to be taken forward
     */
    public LorenzoActionCard(int num){
            this.actionType = LorenzoAction.INCREMENTPOPETRACK;
            this.num = Optional.of(num);
            this.actionColor= Optional.empty();
    }

    /**
     * Costructor of 'discard 2 Development Cards' tokens equals to 'discardEvolution'
     * @param actionColor represents what kind of card will be discarded
     */
    public LorenzoActionCard(CardColor actionColor, int num){
        this.actionType=LorenzoAction.DISCARDEVOLUTION;
        this.actionColor= Optional.ofNullable(actionColor);
        this.num=Optional.of(num);
    }

    public LorenzoAction getActionType() {
        return actionType;
    }

    public Optional<CardColor> getActionColor() {
        return actionColor;
    }

    public Optional<Integer> getNum(){
        return num;
    }
}
