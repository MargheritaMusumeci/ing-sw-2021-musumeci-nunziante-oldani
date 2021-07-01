package it.polimi.ingsw.model.osservables;


import it.polimi.ingsw.model.board.LockBox;
import it.polimi.ingsw.model.listeners.LockBoxListener;

import java.util.ArrayList;
/**
 * class that implements the observable/listener patter for the lock box
 */
public abstract class LockBoxObservable {

    private ArrayList<LockBoxListener> lockBoxListeners = new ArrayList<>();

    public void notifyLockBoxListener(LockBox lockBox) {
        for (LockBoxListener lockBoxListener : lockBoxListeners) {
            lockBoxListener.update(lockBox);
        }
    }

    public void addLockBoxListener(LockBoxListener lockBoxListener) {
        lockBoxListeners.add(lockBoxListener);
    }

}
