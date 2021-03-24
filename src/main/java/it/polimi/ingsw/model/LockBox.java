package it.polimi.ingsw.model;

public class LockBox {
    private int coin;
    private int servant;
    private int rock;
    private int shield;

    public int getCoin(){ return coin; }

    public int getServant(){ return servant; }

    public int getRock(){ return rock; }

    public int getShield(){ return shield; }

    public void setCoin(int howMany , String action){//fare howMany >= 0 se aggiungo e < 0 se prelevo?
        coin = coin + howMany;
    }

    public void setServant(int howMany){ servant = servant + howMany; }

    public void setRock(int howMany){ rock = rock + howMany; }

    public void setShield(int howMany){ shield = shield + howMany; }

}
