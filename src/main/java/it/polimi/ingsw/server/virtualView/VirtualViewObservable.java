package it.polimi.ingsw.server.virtualView;



import java.util.ArrayList;

/**
 * class that have the methods to create an observable object for the virtual view
 */
public class VirtualViewObservable {

    private ArrayList<VirtualViewListener> virtualViewListeners = new ArrayList<>();

    /**
     * method that notify all the listener and send the new Dashboard
     * @param dashboard is the new dashboard to be sent
     */
    //TODO is this class useful?
    public void notifyVirtualViewListener(VirtualView dashboard) {
        for (VirtualViewListener virtualViewListener : virtualViewListeners) {
            virtualViewListener.update(dashboard);
        }
    }

    /**
     * method that adds a listener to the list of listener of this class
     * @param virtualViewListener
     */
    public void addVirtualViewListener(VirtualViewListener virtualViewListener){
        virtualViewListeners.add(virtualViewListener);
    }

    public void removeVirtualViewListener(VirtualViewListener virtualViewListener) {
        virtualViewListeners.remove(virtualViewListener);
    }

    public void removeAll() {
        virtualViewListeners = new ArrayList<>();
    }
}
