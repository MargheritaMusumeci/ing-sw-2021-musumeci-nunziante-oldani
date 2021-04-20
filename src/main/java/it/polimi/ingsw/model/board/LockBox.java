package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.NotEnoughResourcesException;
import it.polimi.ingsw.model.game.Resource;

public class LockBox {
    private int coin;
    private int servant;
    private int rock;
    private int shield;

    /**
     * Return the amount of coins in the LockBox
     * @return
     */
    public int getCoin(){ return coin; }

    /**
     * Return the amount of servants in the LockBox
     * @return
     */
    public int getServant(){ return servant; }

    /**
     * Return the amount of rocks in the LockBox
     * @return
     */
    public int getRock(){ return rock; }

    /**
     * Return the amount of shields in the LockBox
     * @return
     */
    public int getShield(){ return shield; }

    /**
     * Method that return the amount od a specified resource type
     *  Probably the get method above should be private
     * @param resourceType is the resource type I want the amount
     * @return the amount of resourceType in the LockBox
     */
    public int getAmountOf(Resource resourceType){
        switch(resourceType){
            case COIN:     return getCoin();
            case SERVANT:  return getServant();
            case SHIELD:   return getShield();
            case ROCK:     return getRock();
            default:       return 0;
        }
    }

    /**
     * Increment/reduce the quantities of coins
     * @throws NotEnoughResourcesException if the user withdraw more resources than the stored ones
     */
    public void setCoin(int howMany) throws NotEnoughResourcesException {
        if(coin + howMany < 0) throw new NotEnoughResourcesException("Cannot withdraw all these resources");
        coin = coin + howMany;
    }

    /**
     * Increment/reduce the quantities of servants
     * @throws NotEnoughResourcesException if the user withdraw more resources than the stored ones
     */
    public void setServant(int howMany) throws NotEnoughResourcesException {
        if(servant + howMany < 0) throw new NotEnoughResourcesException("Cannot withdraw all these resources");
        servant = servant + howMany;
    }

    /**
     * Increment/reduce the quantities of rocks
     * @throws NotEnoughResourcesException if the user withdraw more resources than the stored ones
     */
    public void setRock(int howMany) throws NotEnoughResourcesException {
        if(rock + howMany < 0) throw new NotEnoughResourcesException("Cannot withdraw all these resources");
        rock = rock + howMany;
    }

    /**
     * Increment/reduce the quantities of shields
     * @throws NotEnoughResourcesException if the user withdraw more resources than the stored ones
     */
    public void setShield(int howMany) throws NotEnoughResourcesException {
        if(shield + howMany < 0) throw new NotEnoughResourcesException("Cannot withdraw all these resources");
        shield = shield + howMany;
    }

}
