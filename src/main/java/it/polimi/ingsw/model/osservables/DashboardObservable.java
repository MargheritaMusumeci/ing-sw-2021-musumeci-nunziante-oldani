package it.polimi.ingsw.model.osservables;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.board.Stock;
import it.polimi.ingsw.model.listeners.DashboardListener;
import it.polimi.ingsw.model.listeners.StockListener;

import java.util.ArrayList;

/**
 * class that implements the observable/listener patter for the dashboard
 */
public abstract class DashboardObservable {

    private ArrayList<DashboardListener> dashboardListeners = new ArrayList<>();

    public void notifyDashboardListener(Dashboard dashboard) {
        for (DashboardListener dashboardListener : dashboardListeners) {
            dashboardListener.update(dashboard);
        }
    }

    public void addDashboardListener(DashboardListener dashboardListener){
        dashboardListeners.add(dashboardListener);
    }

}
