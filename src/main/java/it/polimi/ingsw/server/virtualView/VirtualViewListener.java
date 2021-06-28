package it.polimi.ingsw.server.virtualView;

/**
 * interface for all the class that wants to be updated when a virtual view changes
 */
public interface VirtualViewListener {

    void update(VirtualView virtualView);
}
