package it.polimi.ingsw.server.virtualView;



import java.util.ArrayList;

public class VirtualViewObservable {

    private ArrayList<VirtualViewListener> virtualViewListeners = new ArrayList<>();

    public void notifyVirtualViewListener(VirtualView dashboard) {
        for (VirtualViewListener virtualViewListener : virtualViewListeners) {
            virtualViewListener.update(dashboard);
        }
    }

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
