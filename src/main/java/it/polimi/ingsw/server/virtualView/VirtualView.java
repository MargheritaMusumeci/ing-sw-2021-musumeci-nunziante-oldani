package it.polimi.ingsw.server.virtualView;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.EvolutionSection;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.listeners.DashboardListener;
import it.polimi.ingsw.model.listeners.EvolutionSectionListener;
import it.polimi.ingsw.model.listeners.LeaderCardListener;
import it.polimi.ingsw.model.listeners.MarketListener;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.server.ServerClientConnection;

import java.util.HashMap;

public class VirtualView extends VirtualViewObservable implements DashboardListener, MarketListener, EvolutionSectionListener, VirtualViewListener, LeaderCardListener {

    private ServerClientConnection scc;
    private Market market;
    private EvolutionSection evolutionSection;
    private Dashboard personalDashboard;
    private HashMap<HumanPlayer, VirtualView> otherPlayersView;

    public VirtualView(ServerClientConnection scc, Market market, EvolutionSection evolutionSection, Dashboard personalDashboard){
        this.scc=scc;
        this.evolutionSection = evolutionSection;
        evolutionSection.addEvolutionSectionListener(this);
        this.market = market;
        market.addMarketListener(this);
        this.personalDashboard = personalDashboard;
        personalDashboard.addDashboardListener(this);
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
        //inserire la creazione dell'oggetto che deve essere inserito alla vera view come messaggio di update
    }

    @Override
    public void update(EvolutionSection evolutionSection) {
        this.evolutionSection = evolutionSection;
        //inserire la creazione dell'oggetto che deve essere inserito alla vera view come messaggio di update
    }

    @Override
    public void update(Market market) {
        this.market = market;
        //inserire la creazione dell'oggetto che deve essere inserito alla vera view come messaggio di update
    }

    @Override
    public void update(VirtualView virtualView) {
       otherPlayersView.put(virtualView.scc.getGameHandler().getPlayersInGame().get(virtualView.scc), virtualView);
        //inserire la creazione dell'oggetto che deve essere inserito alla vera view come messaggio di update

    }

    @Override
    public void update(LeaderCard leaderCard) {

    }
}
