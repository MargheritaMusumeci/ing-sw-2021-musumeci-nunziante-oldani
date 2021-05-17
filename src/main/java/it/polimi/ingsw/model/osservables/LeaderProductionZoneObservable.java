package it.polimi.ingsw.model.osservables;

import it.polimi.ingsw.model.board.LeaderProductionZone;
import it.polimi.ingsw.model.listeners.LeaderProductionZoneListener;

import java.util.ArrayList;


/**
 * Maybe never used -> Dashboard call notifyDashboardListener after creating a new LeaderCardProductionZone
 */
public class LeaderProductionZoneObservable {

    private ArrayList<LeaderProductionZoneListener> leaderProductionZoneListeners = new ArrayList<LeaderProductionZoneListener>();

    public void notifyLeaderProductionZoneListener(LeaderProductionZone leaderProductionZone) {
        for (LeaderProductionZoneListener leaderProductionZoneListener : leaderProductionZoneListeners) {
            leaderProductionZoneListener.update(leaderProductionZone);
        }
    }

    public void addLeaderProductionZoneListener(LeaderProductionZoneListener leaderProductionZoneListener){
        leaderProductionZoneListeners.add(leaderProductionZoneListener);
    }

    public void removeLeaderProductionZoneListener(LeaderProductionZoneListener leaderProductionZoneListener) {
        leaderProductionZoneListeners.remove(leaderProductionZoneListener);
    }

    public void removeAll() {
        leaderProductionZoneListeners = new ArrayList<>();
    }
}
