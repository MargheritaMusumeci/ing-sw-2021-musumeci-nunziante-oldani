package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.NegativeScoreException;
import it.polimi.ingsw.exception.NotEnoughResourcesException;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.EvolutionSection;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.listeners.*;
import it.polimi.ingsw.model.osservables.DashboardObservable;
import it.polimi.ingsw.model.popeTrack.PopeCard;
import it.polimi.ingsw.model.popeTrack.PopeTrack;
import it.polimi.ingsw.serializableModel.SerializableLeaderProductionZone;
import it.polimi.ingsw.serializableModel.SerializableLockBox;
import it.polimi.ingsw.serializableModel.SerializableProductionZone;
import it.polimi.ingsw.serializableModel.SerializableStock;

import java.io.Serializable;
import java.util.ArrayList;

public class Dashboard extends DashboardObservable implements LockBoxListener, PopeTrackListener,
            ProductionZoneListener, StockListener, Serializable {

    private String nickName;
    private int totalScore;
    private Stock personalStock;
    private LockBox personalLockBox;
    private NormalProductionZone[] personalProductionZones;
    private ArrayList<LeaderProductionZone> leaderProductionZones;
    private int evolutionCardNumber;
    private PopeTrack personalPopeTrack;
    private ArrayList<LeaderCard> leaderCards;
    private boolean inkwell;

    public Dashboard(String nickName, boolean inkwell, PopeTrack personalPopeTrack){
        this.nickName = nickName;
        this.leaderCards = new ArrayList<LeaderCard>();
        this.inkwell = inkwell;
        this.personalPopeTrack = personalPopeTrack;
        this.personalPopeTrack.addPopeTrackListener(this);

        totalScore = 0;

        personalLockBox = new LockBox();
        personalLockBox.addLockBoxListener(this);

        personalStock = new Stock();
        personalStock.addStockListener(this);

        personalProductionZones = new NormalProductionZone[3];
        for (int i = 0; i< personalProductionZones.length; i++){
            personalProductionZones[i] = new NormalProductionZone();
            personalProductionZones[i].addProductionZoneListener(this);
        }
        leaderProductionZones = new ArrayList<LeaderProductionZone>();

    }

    public Dashboard(String nickName, boolean inkwell, PopeTrack personalPopeTrack, ArrayList<LeaderCard> leaderCards, SerializableStock stock,
                     SerializableLockBox lockBox, SerializableProductionZone[] productionZones, ArrayList<LeaderProductionZone> leaderProductionZones ){
        try {

        this.nickName = nickName;
        if (leaderCards == null ) this.leaderCards = new ArrayList<>();
        else this.leaderCards = leaderCards;
        this.inkwell = inkwell;
        this.personalPopeTrack = personalPopeTrack;
        this.personalPopeTrack.addPopeTrackListener(this);

        personalLockBox = new LockBox();
        for (Resource resource: lockBox.getResources().keySet()) {
            try {
                personalLockBox.setAmountOf(resource,lockBox.getResources().get(resource));
            } catch (NotEnoughResourcesException e) {
                e.printStackTrace();
            }
        }
        personalLockBox.addLockBoxListener(this);

        personalStock = new Stock();
        personalStock.setBoxes(stock.getBoxes());
        personalStock.setBoxPlus(stock.getBoxPlus());
        personalStock.setResourcesPlus(stock.getResourcesPlus());
        personalStock.addStockListener(this);

        personalProductionZones = new NormalProductionZone[3];

        for (int i = 0; i< personalProductionZones.length; i++){
            personalProductionZones[i] = new NormalProductionZone();
            if (productionZones[i].getCards() == null ) personalProductionZones[i].setCards(new ArrayList<>());
            else personalProductionZones[i].setCards(productionZones[i].getCards());
            personalProductionZones[i].addProductionZoneListener(this);
        }
        if (leaderProductionZones == null) this.leaderProductionZones = new ArrayList<>();
        else this.leaderProductionZones = leaderProductionZones;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getEvolutionCardNumber() {
        return evolutionCardNumber;
    }

    public void setEvolutionCardNumber(int evolutionCardNumber) {
        this.evolutionCardNumber = evolutionCardNumber;
        notifyDashboardListener(this);
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
            //get total resource
            int totalResources = (personalStock.getTotalNumberOfResources() + personalLockBox.getTotalAmountOfResources());
            totalScore = totalResources/5;

            //leader cards
            for(LeaderCard leaderCard: leaderCards){
                if(leaderCard.isActive())
                    totalScore += leaderCard.getPoint();
            }

            //production zones
            for(NormalProductionZone productionZone : personalProductionZones){
                if(productionZone.getCardList() != null){
                    for (Card evolutionCard : productionZone.getCardList()){
                        totalScore += evolutionCard.getPoint();
                    }
                }
            }

            totalScore += personalPopeTrack.getGamerPosition().getPoint();

            for (PopeCard popeCard: personalPopeTrack.getPopeCard()) {
                if (popeCard.isUsed()) {
                    totalScore += popeCard.getPoint();
                }
            }

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
     * @return the instance of the actual normal production zone (array of three production zones) of the player, not a copy
     */
    public NormalProductionZone[] getProductionZone() {
        return personalProductionZones;
    }

    /**
     *
     * @return the instance of the actual leader production zone (array list of max 2 zones) of the player, not a copy
     */
    public ArrayList<LeaderProductionZone> getLeaderProductionZones(){return leaderProductionZones;}

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
        this.leaderCards = new ArrayList<LeaderCard>();
        for(LeaderCard card : leaderCards)
            this.leaderCards.add(card);

        notifyDashboardListener(this);
    }

    /**
     *
     * @return true if the player is the first one of the tourn, false otherwise
     */
    public boolean getInkwell() {
        return inkwell;
    }

    /**
     * method that create a new Leader production zone when a leader card with that power is activated is acivated
     * @param leaderCard is the leader card that has been activated
     */
    public void addLeaderCardProductionZone(LeaderCard leaderCard){
        if(leaderProductionZones.size()<2){
            leaderProductionZones.add(new LeaderProductionZone(leaderCard));
        }else{
            //possibilità di lanciare un'eccezione
        }

        notifyDashboardListener(this);
    }

    /**
     * method able to manage the basic production of every dashboard
     * @param requires1 the first resource to be converted
     * @param requires2 the second resource to be converted
     * @param ensures the resource to be generated and added to the personal lock box
     * @throws NotEnoughResourcesException
     */
    public void activeBasicProduction(Resource requires1, Resource requires2, Resource ensures) throws NotEnoughResourcesException {

        if(personalStock.getTotalQuantitiesOf(requires1) > 0){
            personalStock.useResources(1, requires1);
        }else{
            personalLockBox.setAmountOf(requires1, -1);
        }

        if(personalStock.getTotalQuantitiesOf(requires2) > 0){
            personalStock.useResources(1, requires2);
        }else{
            personalLockBox.setAmountOf(requires2, -1);
        }

        personalLockBox.setAmountOf(ensures, 1);
    }

    @Override
    public void update(LockBox lockbox) {
        notifyDashboardListener(this);

    }

    @Override
    public void update(PopeTrack popeTrack) {
        notifyDashboardListener(this);

    }

    @Override
    public void update(ProductionZone productionZone) {
        notifyDashboardListener(this);

    }

    @Override
    public void update(Stock stock) {
        notifyDashboardListener(this);

    }
}
