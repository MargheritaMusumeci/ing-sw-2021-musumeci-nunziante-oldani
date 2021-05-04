package it.polimi.ingsw.model.osservables;

import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.listeners.MarketListener;

import java.util.ArrayList;

public abstract class MarketObservable {

    private ArrayList<MarketListener> marketListeners = new ArrayList<>();

    public void notifyMarketListeners(Market market) {
        for (MarketListener marketListener : marketListeners) {
            marketListener.update(market);
        }
    }

    public void addMarketListener(MarketListener marketListener){
        marketListeners.add(marketListener);
    }

    public void removeMarketListener(MarketListener marketListener) {
        marketListeners.remove(marketListener);
    }

    public void removeAll() {
        marketListeners = new ArrayList<>();
    }
}
