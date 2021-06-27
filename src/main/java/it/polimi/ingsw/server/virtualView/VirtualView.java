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

public class VirtualView extends VirtualViewObservable implements DashboardListener, MarketListener, EvolutionSectionListener,
        VirtualViewListener, LeaderCardListener, PlayerListener {

    private ServerClientConnection scc;
    private Market market;
    private EvolutionSection evolutionSection;
    private Dashboard personalDashboard;
    private HashMap<HumanPlayer, VirtualView> otherPlayersView;

    public VirtualView(ServerClientConnection scc, Market market, EvolutionSection evolutionSection, Dashboard personalDashboard){
        System.err.println("virtual view created");
        this.scc=scc;
        //devo registrare questa view ai listener del mio player
        scc.getGameHandler().getPlayersInGame().get(scc).addPlayerListener(this);

        this.evolutionSection = evolutionSection;
        evolutionSection.addEvolutionSectionListener(this);

        this.market = market;
        market.addMarketListener(this);

        this.personalDashboard = personalDashboard;
        personalDashboard.addDashboardListener(this);

        //aggioungo questa virtual view al listener delle leader card della dashboard
        for (LeaderCard leadercard: personalDashboard.getLeaderCards()) {
            leadercard.addLeaderCardListener(this);
        }
        //vedere se aggiungere other player dashboard gia nel costruttore
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

    public void setScc(ServerClientConnection scc) {
        this.scc = scc;
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

    public void setPersonalDashboard(Dashboard personalDashboard) {
        this.personalDashboard = personalDashboard;
    }

    @Override
    public void update(Dashboard dashboard) {
        this.personalDashboard = dashboard;
        SerializableDashboard serializableDashboard = new SerializableDashboard(dashboard);
        //System.out.println("sono nella virtual view e dovrei madnare il messaggio per aggiornare la dashboead");

        scc.send(new UpdateDashBoardMessage("new dashboard", serializableDashboard));

        //mando anche il messsaggio per aggiornare le leader cards
        ArrayList<SerializableLeaderCard> newSetOfLeaderCards = new ArrayList<>();
        for(LeaderCard leaderCard : dashboard.getLeaderCards()){
            newSetOfLeaderCards.add(new SerializableLeaderCard(leaderCard));
        }
        scc.send(new UpdateLeaderCardsMessage("new set of leader cards", newSetOfLeaderCards));

        //avendo aggiornato la mia dashboad devo avvisare tutti gli altri player di questo aggiornamento e quindi mando
        //a tutti il messaggio che devono aggiornare la view dei nemici
        for(ServerClientConnection serverClientConnection: scc.getGameHandler().getPlayersInGame().keySet()){
            if (!scc.equals(serverClientConnection)) {
                serverClientConnection.send(new UpdateOtherPlayerViewMessage("Update other player view",
                        scc.getGameHandler().createView(this), scc.getNickname()));
            }
        }

    }

    @Override
    public void update(EvolutionSection evolutionSection) {
        this.evolutionSection = evolutionSection;

        //per ogni player devo creare la sua serializable evolutionsection
        for(ServerClientConnection serverClientConnection : scc.getGameHandler().getPlayersInGame().keySet()){

           SerializableEvolutionSection serializableEvolutionSection = new SerializableEvolutionSection(scc.getGameHandler().getGame().getEvolutionSection(),
                   scc.getGameHandler().getPlayersInGame().get(serverClientConnection));
           scc.send(new UpdateEvolutionSectionMessage("update della evolutuon section", serializableEvolutionSection));
        }
    }

    @Override
    public void update(Market market) {
        this.market = market;
        SerializableMarket serializableMarket = new SerializableMarket(market);

        //l'update del market lo devo mandare a tutti i partecipanti della partita
        for (ServerClientConnection serverClientConnection: scc.getGameHandler().getPlayersInGame().keySet()){
            serverClientConnection.send(new UpdateMarketMessage("new marlet", serializableMarket));
        }
    }

    @Override
    public void update(VirtualView virtualView) {
       otherPlayersView.put(virtualView.scc.getGameHandler().getPlayersInGame().get(virtualView.scc), virtualView);
        //inserire la creazione dell'oggetto che deve essere inserito alla vera view come messaggio di update

    }

    @Override
    /**
     * method that updates the leaderCards attribute in the dashboard changing the leader card that has been updated and
     * send the message to the scc to notifying the changes
     */
    public void update(LeaderCard leaderCard) {
        //devo aggiornare la mia dashboard con la carta giusta e mandare il messaggio che aggiorna tutto il set di carte
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

        //mando il messaggio di update
        scc.send(new UpdateLeaderCardsMessage("updated set of leader cards", newSerializableLeaderCardSet));
    }

    @Override
    public void update(ArrayList<Resource> resources) {
        //devo dire alla view del client che le risorse comrpate dal mercaro sonos state messe nell'array
       //scc.send(new UpdateResourcesBoughtFromMarketMessage("resources bought from market", resources));
    }
}
