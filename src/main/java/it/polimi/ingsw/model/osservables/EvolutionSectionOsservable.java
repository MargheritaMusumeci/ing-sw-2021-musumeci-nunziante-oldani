package it.polimi.ingsw.model.osservables;

import it.polimi.ingsw.model.game.EvolutionSection;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.listeners.EvolutionSectionListener;
import it.polimi.ingsw.model.listeners.MarketListener;

import java.util.ArrayList;

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

    public void removeEvolutionSectionListener(EvolutionSectionListener evolutionSectionListener) {
        evolutionSectionListeners.remove(evolutionSectionListener);
    }

    public void removeAll() {
        evolutionSectionListeners = new ArrayList<>();
    }
}
