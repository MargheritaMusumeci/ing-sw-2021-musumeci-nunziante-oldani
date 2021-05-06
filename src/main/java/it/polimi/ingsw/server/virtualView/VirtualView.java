package it.polimi.ingsw.server.virtualView;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.game.EvolutionSection;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.listeners.DashboardListener;
import it.polimi.ingsw.model.listeners.EvolutionSectionListener;
import it.polimi.ingsw.model.listeners.MarketListener;
import it.polimi.ingsw.server.ServerClientConnection;

import java.util.HashMap;

public class VirtualView extends VirtualViewObservable implements DashboardListener, MarketListener, EvolutionSectionListener, VirtualViewListener {

    private ServerClientConnection scc;
    private Market market;
    private EvolutionSection evolutionSection;
    private Dashboard personalDashboard;
    private HashMap<ServerClientConnection, VirtualView> otherPlayersView;

    public VirtualView(ServerClientConnection scc, Market market, EvolutionSection evolutionSection, Dashboard personalDashboard){
        this.scc=scc;
        this.evolutionSection = evolutionSection;
        this.market = market;
        this.personalDashboard = personalDashboard;
        //vedere se aggiungere other player dashboard gia nel costruttore
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
       otherPlayersView.put(virtualView.scc, virtualView);
        //inserire la creazione dell'oggetto che deve essere inserito alla vera view come messaggio di update

    }
}
