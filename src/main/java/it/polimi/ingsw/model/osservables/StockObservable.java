package it.polimi.ingsw.model.osservables;

import it.polimi.ingsw.model.board.Stock;
import it.polimi.ingsw.model.listeners.StockListener;

import java.util.ArrayList;

public abstract class StockObservable {

    private ArrayList<StockListener> stockListeners = new ArrayList<>();

    public void notifyStockListener(Stock stock) {
        for (StockListener stockListener : stockListeners) {
            stockListener.update(stock);
        }
    }

    public void addStockListener(StockListener stockListener){
        stockListeners.add(stockListener);
    }

    public void removeStockListener(StockListener stockListener) {
        stockListeners.remove(stockListener);
    }

    public void removeAll() {
        stockListeners = new ArrayList<>();
    }
}
