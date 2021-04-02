package it.polimi.ingsw.model;

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
     * Increment/reduce the quantities of coins
     * @return
     */
    public void setCoin(int howMany){ coin = coin + howMany; }

    /**
     * Increment/reduce the quantities of servants
     * @return
     *
     */
    public void setServant(int howMany){ servant = servant + howMany; }

    /**
     * Increment/reduce the quantities of rocks
     * @return
     */
    public void setRock(int howMany){ rock = rock + howMany; }

    /**
     * Increment/reduce the quantities of shields
     * @return
     */
    public void setShield(int howMany){ shield = shield + howMany; }

}
