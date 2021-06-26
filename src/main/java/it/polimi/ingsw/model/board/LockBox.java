package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.NotEnoughResourcesException;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.osservables.LockBoxObservable;

import java.io.Serializable;

public class LockBox extends LockBoxObservable implements Serializable {
    private int coin;
    private int servant;
    private int rock;
    private int shield;

    public LockBox(){
        coin =0;
        servant=0;
        shield=0;
        rock=0;
    }
    /**
     * @return the amount of coins in the LockBox
     */
    private int getCoin(){ return coin; }

    /**
     * @return the amount of servants in the LockBox
     */
    private int getServant(){ return servant; }

    /**
     * @return the amount of rocks in the LockBox
     */
    private int getRock(){ return rock; }

    /**
     * @return the amount of shields in the LockBox
     */
    private int getShield(){ return shield; }

    /**
     * Method that return the amount od a specified resource type
     * @param resourceType is the resource type of which the return number is referred to
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
     * @return the total amount of resources in the lockBox
     */
    public int getTotalAmountOfResources(){
        return (getCoin() + getRock() + getShield() + getServant());
    }

    /**
     * Increment/reduce the quantities of coins
     * @throws NotEnoughResourcesException if the user withdraw more resources than the stored ones
     */
    private void setCoin(int howMany) throws NotEnoughResourcesException {
        if(coin + howMany < 0) throw new NotEnoughResourcesException("Cannot withdraw all these resources");
        coin = coin + howMany;

        notifyLockBoxListener(this);
    }

    /**
     * Increment/reduce the quantities of servants
     * @throws NotEnoughResourcesException if the user withdraw more resources than the stored ones
     */
    private void setServant(int howMany) throws NotEnoughResourcesException {
        if(servant + howMany < 0) throw new NotEnoughResourcesException("Cannot withdraw all these resources");
        servant = servant + howMany;

        notifyLockBoxListener(this);
    }

    /**
     * Increment/reduce the quantities of rocks
     * @throws NotEnoughResourcesException if the user withdraw more resources than the stored ones
     */
    private void setRock(int howMany) throws NotEnoughResourcesException {
        if(rock + howMany < 0) throw new NotEnoughResourcesException("Cannot withdraw all these resources");
        rock = rock + howMany;

        notifyLockBoxListener(this);
    }

    /**
     * Increment/reduce the quantities of shields
     * @throws NotEnoughResourcesException if the user withdraw more resources than the stored ones
     */
    private void setShield(int howMany) throws NotEnoughResourcesException {
        if(shield + howMany < 0) throw new NotEnoughResourcesException("Cannot withdraw all these resources");
        shield = shield + howMany;

        notifyLockBoxListener(this);
    }

    /**
     * Method that call the right setter according to the resourceType
     * @param resourceType the resource the user wants to update
     * @param howMany is the quantity of resources to add + /remove -
     * @throws NotEnoughResourcesException if the setter throw the exception
     */
    public void setAmountOf(Resource resourceType , int howMany) throws NotEnoughResourcesException{
        switch(resourceType){
            case COIN:
                setCoin(howMany);
                break;
            case SERVANT:
                setServant(howMany);
                break;
            case SHIELD:
                setShield(howMany);
                break;
            case ROCK:
                setRock(howMany);
                break;
            default:
        }

        notifyLockBoxListener(this);
    }

}
