package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.cards.EvolutionCard;

import java.io.Serializable;

public class SerializableEvolutionSection implements Serializable {

    private EvolutionCard[][] evolutionCards;
    private boolean[][] canBuyEvolutionCards;

    public SerializableEvolutionSection(EvolutionCard[][] evolutionCards, boolean[][] canBuyEvolutionCards) {
        this.evolutionCards = evolutionCards;
        this.canBuyEvolutionCards = canBuyEvolutionCards;
    }

    public EvolutionCard[][] getEvolutionCards() {
        return evolutionCards;
    }

    public void setEvolutionCards(EvolutionCard[][] evolutionCards) {
        this.evolutionCards = evolutionCards;
    }

    public boolean[][] getCanBuyEvolutionCards() {
        return canBuyEvolutionCards;
    }

    public void setCanBuyEvolutionCards(boolean[][] canBuyEvolutionCards) {
        this.canBuyEvolutionCards = canBuyEvolutionCards;
    }
}
