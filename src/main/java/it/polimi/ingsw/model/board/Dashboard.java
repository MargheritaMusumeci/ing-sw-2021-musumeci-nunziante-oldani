package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.NegativeScoreException;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.popeTrack.PopeTrack;

import java.util.ArrayList;

public class Dashboard {

    private String nickName;
    private int totalScore;
    private Stock personalStock;
    private LockBox personalLockBox;
    private ProductionZone[] personalProductionZone;
    private int evolutionCardNumber;
    private PopeTrack personalPopeTrack;
    private ArrayList<LeaderCard> leaderCards;
    private boolean inkwell;

    public Dashboard(String nickName, boolean inkwell, PopeTrack personalPopeTrack){
        this.nickName = nickName;
        this.leaderCards = new ArrayList<LeaderCard>();
        this.inkwell = inkwell;
        this.personalPopeTrack = personalPopeTrack;

        totalScore = 0;
        personalLockBox = new LockBox();
        personalStock = new Stock();

        personalProductionZone = new ProductionZone[3];

        for (int i=0; i<personalProductionZone.length; i++){
            personalProductionZone[i] = new ProductionZone();
        }
    }


    public int getEvolutionCardNumber() {
        return evolutionCardNumber;
    }

    public void setEvolutionCardNumber(int evolutionCardNumber) {
        this.evolutionCardNumber = evolutionCardNumber;
    }

    /**
     *
     * @return the nickname of the player who owns the dashboard
     */
    public String getNickName() {
        return nickName;
    }

    /**
     *
     * @return the score of the player who owns the dashboard
     */
    public int getScore() {
        return totalScore;
    }

    /**
     *
     * @return the instance of the actual personal stock of the player, not a copy
     */
    public Stock getStock() {
        return personalStock;
    }

    /**
     *
     * @return the instance of the actual personal lockbox of the player, not a copy
     */
    public LockBox getLockBox() {
        return personalLockBox;
    }

    /**
     *
     * @return the instance of the actual production zone (array of three production zones) of the player, not a copy
     */
    public ProductionZone[] getProductionZone() {
        return personalProductionZone;
    }

    /**
     *
     * @return the instance of the actual pope track of the player, not a copy
     */
    public PopeTrack getPopeTrack() {
        return personalPopeTrack;
    }

    /**
     *
     * @return the instance of the actual leaders card (array of two leader cards) of the player, not a copy
     */
    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    /**
     * Set the leader card)
     * @param leaderCards is the card for the player: 4 in the start , 2 after the player chose them
     */
    public void setLeaderCards(ArrayList<LeaderCard> leaderCards){
        for(LeaderCard card : leaderCards)
            this.leaderCards.add(card);
    }

    /**
     *
     * @return true if the player is the first one of the tourn, false otherwise
     */
    public boolean getInkwell() {
        return inkwell;
    }

    /**
     * method that set the score
     * @param points is the new value of the score, not the increment
     */
    public void setScore(int points) throws NegativeScoreException {
        if(points < 0){
            throw new NegativeScoreException("You cannot subtract points to a player");
        }
        this.totalScore = points;
    }



    /*
     * maybe useless, we'll see
     * @param personalProductionZone

    public void setPersonalProductionZone(ProductionZone[] personalProductionZone) {
        this.personalProductionZone = personalProductionZone;
    }
    */



    /*
     * method that increment the position of the player into his pope track
     * @param increment is the actual increment to be done

    public void setPopeTrack(int increment) {

    }
    */


    /*
     * method to add a card bought in the EvolutionSection in one of the player's production zone
     * @param card the card to be added
     * @param box the number of the box in which the card has to be added

    public void setProductionZone(EvolutionCard card, int box){

    }

     */


    /*
     * Probably useless and to be deprecated
     * @param type is the type of resource added to the lockbox
     * @param numResources is the number of resources to add or withdraw
     * @param action defines if its a withdraw or a deposit

    public void setLockBox(ResourceType type, int numResources, String action){

    }
    */





}
