package it.polimi.ingsw.model.osservables;

import it.polimi.ingsw.model.game.EvolutionSection;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.listeners.EvolutionSectionListener;
import it.polimi.ingsw.model.listeners.MarketListener;

import java.util.ArrayList;

/**
 * class that implements the observable/listener patter for the evolution section
 */
public abstract class EvolutionSectionOsservable {

    private ArrayList<EvolutionSectionListener> evolutionSectionListeners = new ArrayList<>();

    public void notifyEvolutionSectionListener(EvolutionSection evolutionSection) {
        for (EvolutionSectionListener evolutionSectionListener : evolutionSectionListeners) {
            evolutionSectionListener.update(evolutionSection);
        }
    }

    public void addEvolutionSectionListener(EvolutionSectionListener evolutionSectionListener){
        evolutionSectionListeners.add(evolutionSectionListener);
    }

}
