package it.polimi.ingsw.model;

/**
 * Create and Manage all possible Solo Action token
 */
public class LorenzoActionCard {

    private LorenzoAction actionType;

    /**
     * if actionType is discard a Development Card, this attribute represents what kind of card will be discarded
     * else is null
     */
    private CardColor actionColor;

    /**
     * if actionType is move the black cross, this attribute represents how many spaces it has to be taken forward
     * else is null
     */
    private int num;

    /**
     * costructor of 'move Black Cross' tokens equals to 'incrementPopeTrack'
     * @param num represents how many spaces it has to be taken forward
     */
    public LorenzoActionCard(int num){
       this.actionType=LorenzoAction.incrementPopeTrack;
       this.num=num;
    }

    /**
     * costructor of 'discard 2 Development Cards' tokens equals to 'discardEvolution'
     * @param actionColor represents what kind of card will be discarded
     */
    public LorenzoActionCard(CardColor actionColor){
        this.actionType=LorenzoAction.discardEvolution;
        this.actionColor=actionColor;
    }

    public LorenzoAction getActionType() {
        return actionType;
    }

    public CardColor getActionColor() {
        return actionColor;
    }

    public int getNum() {
        return num;
    }
}
