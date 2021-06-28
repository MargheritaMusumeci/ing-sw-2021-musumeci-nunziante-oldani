package it.polimi.ingsw.server.virtualView;

import it.polimi.ingsw.messages.sentByServer.updateMessages.*;
import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.EvolutionSection;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.listeners.*;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.serializableModel.*;
import it.polimi.ingsw.server.ServerClientConnection;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * class that represent the clients view in the server. it is listening for changes on the market, evolution section
 * and dashboards
 */
public class VirtualView extends VirtualViewObservable implements DashboardListener, MarketListener, EvolutionSectionListener,
        VirtualViewListener, LeaderCardListener, PlayerListener {

    private ServerClientConnection scc;
    private Market market;
    private EvolutionSection evolutionSection;
    private Dashboard personalDashboard;
    private HashMap<HumanPlayer, VirtualView> otherPlayersView;

    /**
     * constructor of the class, it is able to add the listeners
     * @param scc is the instance handling the connection from the clint to the server
     * @param market is the game market
     * @param evolutionSection is the game evolution section
     * @param personalDashboard is the dashboard of the player associated with this virtual view
     */
    public VirtualView(ServerClientConnection scc, Market market, EvolutionSection evolutionSection, Dashboard personalDashboard){
        System.err.println("virtual view created");
        this.scc=scc;

        scc.getGameHandler().getPlayersInGame().get(scc).addPlayerListener(this);

        this.evolutionSection = evolutionSection;
        evolutionSection.addEvolutionSectionListener(this);

        this.market = market;
        market.addMarketListener(this);

        this.personalDashboard = personalDashboard;
        personalDashboard.addDashboardListener(this);

        for (LeaderCard leadercard: personalDashboard.getLeaderCards()) {
            leadercard.addLeaderCardListener(this);
        }

    }

    public HashMap<HumanPlayer, VirtualView> getOtherPlayersView() {
        return otherPlayersView;
    }

    public void setOtherPlayersView(HashMap<HumanPlayer, VirtualView> otherPlayersView) {
        this.otherPlayersView = otherPlayersView;
    }

    public ServerClientConnection getScc() {
        return scc;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public EvolutionSection getEvolutionSection() {
        return evolutionSection;
    }

    public void setEvolutionSection(EvolutionSection evolutionSection) {
        this.evolutionSection = evolutionSection;
    }

    public Dashboard getPersonalDashboard() {
        return personalDashboard;
    }

    /**
     * method responsible to update all the clients in the game when the personal dashboard face a change
     * @param dashboard is the updated dashboard
     */
    @Override
    public void update(Dashboard dashboard) {
        this.personalDashboard = dashboard;
        SerializableDashboard serializableDashboard = new SerializableDashboard(dashboard);

        scc.send(new UpdateDashBoardMessage("new dashboard", serializableDashboard));

        //send the message to update the leader cards
        ArrayList<SerializableLeaderCard> newSetOfLeaderCards = new ArrayList<>();
        for(LeaderCard leaderCard : dashboard.getLeaderCards()){
            newSetOfLeaderCards.add(new SerializableLeaderCard(leaderCard));
        }
        scc.send(new UpdateLeaderCardsMessage("new set of leader cards", newSetOfLeaderCards));

        //send the message to update enemy views
        for(ServerClientConnection serverClientConnection: scc.getGameHandler().getPlayersInGame().keySet()){
            if (!scc.equals(serverClientConnection)) {
                serverClientConnection.send(new UpdateOtherPlayerViewMessage("Update other player view",
                        scc.getGameHandler().createView(this), scc.getNickname()));
            }
        }

    }

    /**
     * method responsible to update all the clients in the game when the evolutionSection face a change
     * @param evolutionSection is the updated evolutionSection
     */
    @Override
    public void update(EvolutionSection evolutionSection) {
        this.evolutionSection = evolutionSection;

        for(ServerClientConnection serverClientConnection : scc.getGameHandler().getPlayersInGame().keySet()){

           SerializableEvolutionSection serializableEvolutionSection = new SerializableEvolutionSection(scc.getGameHandler().getGame().getEvolutionSection(),
                   scc.getGameHandler().getPlayersInGame().get(serverClientConnection));
           scc.send(new UpdateEvolutionSectionMessage("update della evolution section", serializableEvolutionSection));
        }
    }

    /**
     * method responsible to update all the clients in the game when the market face a change
     * @param market is the updated market
     */
    @Override
    public void update(Market market) {
        this.market = market;
        SerializableMarket serializableMarket = new SerializableMarket(market);

        for (ServerClientConnection serverClientConnection: scc.getGameHandler().getPlayersInGame().keySet()){
            serverClientConnection.send(new UpdateMarketMessage("new marlet", serializableMarket));
        }
    }

    /**
     * method that update the object that stores the view of other players
     * @param virtualView is the new enemy virtual view to be updated
     */
    @Override
    public void update(VirtualView virtualView) {
       otherPlayersView.put(virtualView.scc.getGameHandler().getPlayersInGame().get(virtualView.scc), virtualView);
    }

    @Override
    /**
     * method that updates the leaderCards attribute in the dashboard changing the leader card that has been updated and
     * send the message to the scc to notifying the changes
     * @param leaderCard id the new leader card
     */
    public void update(LeaderCard leaderCard) {

        ArrayList<LeaderCard> newLeaderCardSet = new ArrayList<>();
        for (LeaderCard lcard : personalDashboard.getLeaderCards()) {
            if(lcard.getId() == leaderCard.getId()){
                newLeaderCardSet.add(leaderCard);
            }else{
                newLeaderCardSet.add(lcard);
            }
        }
        personalDashboard.setLeaderCards(newLeaderCardSet);

        ArrayList<SerializableLeaderCard> newSerializableLeaderCardSet = new ArrayList<>();
        for(LeaderCard lcard: newLeaderCardSet){
            newSerializableLeaderCardSet.add(new SerializableLeaderCard(lcard));
        }

        scc.send(new UpdateLeaderCardsMessage("updated set of leader cards", newSerializableLeaderCardSet));
    }

    //TODO capire se questo posso toglierlo veramente o se ci siamo dimeticati qualcosa
    @Override
    public void update(ArrayList<Resource> resources) {
        //devo dire alla view del client che le risorse comrpate dal mercaro sonos state messe nell'array
       //scc.send(new UpdateResourcesBoughtFromMarketMessage("resources bought from market", resources));
    }
}
