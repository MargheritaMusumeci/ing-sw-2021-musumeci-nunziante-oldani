package it.polimi.ingsw.model;

public class Dashboard {

    private String nickName;
    private int totalScore;
    private Stock personalStock;
    private LockBox personalLockBox;
    private ProductionZone[] personalProductionZone;
    private PopeTrack personalPopeTrack;
    private LeaderCard[] leaderCards;
    private boolean inkwell;

    public Dashboard(String nickName, LeaderCard[] leaderCards, boolean inkwell){
        this.nickName = nickName;
        this.leaderCards = leaderCards;
        this.inkwell = inkwell;
    }

    public String getNickName() {
        return nickName;
    }

    public int getScore() {
        return totalScore;
    }

    public Stock getStock() {
        return personalStock;
    }

    public LockBox getLockBox() {
        return personalLockBox;
    }

    public ProductionZone[] getProductionZone() {
        return personalProductionZone;
    }

    public PopeTrack getPopeTrack() {
        return personalPopeTrack;
    }

    public LeaderCard[] getLeaderCards() {
        return leaderCards;
    }

    public void setPersonalProductionZone(ProductionZone[] personalProductionZone) {
        this.personalProductionZone = personalProductionZone;
    }

    public boolean getInkwell() {
        return inkwell;
    }

    /**
     * method that increment the position of the player into his pope track
     * @param increment is the actual increment to be done
     */
    public void setPopeTrack(int increment) {

    }

    /**
     * method capable of setting the leaders card after the player has selected them
     * @param leaderCards is the array of cards to be added to the dashboard
     */
    public void setLeaderCards(LeaderCard[] leaderCards) {
        this.leaderCards = leaderCards;
    }

    /**
     * method to add a card bought in the EvolutionSection in one of the player's production zone
     * @param card the card to be added
     * @param box the number of the box in which the card has to be added
     */
    public void setProductionZone(EvolutionCard card, int box){

    }

    /**
     * method that set the score
     * @param points is the new value of the score, not the increment
     */
    public void setScore(int points){
        this.totalScore = points;
    }

    /**
     * Probably useless and to be deprecated
     * @param type is the type of resource added to the lockbox
     * @param numResources is the number of resources to add or withdraw
     * @param action defines if its a withdraw or a deposit
     */
    public void setLockBox(ResourceType type, int numResources, String action){

    }




}
