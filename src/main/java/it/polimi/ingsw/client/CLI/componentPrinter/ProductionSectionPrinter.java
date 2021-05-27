package it.polimi.ingsw.client.CLI.componentPrinter;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardColor;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.serializableModel.SerializableDashboard;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import it.polimi.ingsw.serializableModel.SerializableLeaderProductionZone;
import it.polimi.ingsw.serializableModel.SerializableProductionZone;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;

public class ProductionSectionPrinter {

    public static void print(SerializableDashboard serializableDashboard){

        SerializableProductionZone[] serializableProductionZones = serializableDashboard.getSerializableProductionZones();
        SerializableLeaderProductionZone[] serializableLeaderProductionZone= serializableDashboard.getSerializableLeaderProductionZones();

        System.out.println(Constants.ANSI_PURPLE + "\n" + Constants.productionZoneTitle + Constants.ANSI_RESET);
        ArrayList<ArrayList<String>[]> setOfProductionZones = new ArrayList<>();

        //aggiungo tutte le carte che ci sono nelle evolution section
        ArrayList<String>[] card = null;
        for(int i=0; i< serializableProductionZones.length; i++){
            if(serializableProductionZones[i].getCards()!= null && serializableProductionZones[i].getCards().size() != 0){
                card = EvolutionSectionPrinter.createEvolutionCard(serializableProductionZones[i].getCards().get(0)); //mettere l'ultima se è al contrario
            }else{
                card = emptyCard();
            }

            setOfProductionZones.add(card);
        }

        //agggiungo le scrtitte con i resoconti di tutta la production zone
        int numberOfTotalCard = 0;
        int numberOfGreenCards = 0;
        int numberOfPurpleCards = 0;
        int numberOfYellowCards = 0;
        int numberOfBlueCards = 0;
        if(serializableProductionZones != null){
            for(int i=0; i< serializableProductionZones.length; i++){
                if(serializableProductionZones[i].getCards() != null){
                    numberOfTotalCard += serializableProductionZones[i].getCards().size();
                    for (EvolutionCard evolutionCard: serializableProductionZones[i].getCards()){
                        if(evolutionCard.getColor() == CardColor.BLUE){
                            numberOfBlueCards++;
                        }else if (evolutionCard.getColor()== CardColor.GREEN){
                            numberOfGreenCards++;
                        }else if (evolutionCard.getColor() == CardColor.PURPLE){
                            numberOfPurpleCards++;
                        }else{
                            numberOfYellowCards++;
                        }
                    }
                }
            }
        }


        ArrayList<String>[] info = new ArrayList[15];
        for (int i=0; i<15; i++){
            info[i] = new ArrayList<>();
        }

        info[0].add("     Total Cards: " + numberOfTotalCard);
        info[1].add("     " + Constants.green + " Cards: " + numberOfGreenCards);
        info[2].add("     " + Constants.yellow+ " Cards: " + numberOfYellowCards);
        info[3].add("     " + Constants.purple + " Cards: " + numberOfPurpleCards);
        info[4].add("     " + Constants.blue + " Cards: " + numberOfBlueCards);

        for (int j=5; j<15; j++){
            info[j].add(" ");
        }

        setOfProductionZones.add(info);

        //stampo la prima parte senza contare le production zone plus
        for (int i=0; i< setOfProductionZones.get(0).length; i++){
            for(int k=0; k<setOfProductionZones.size(); k++){
                for (int j=0; j<setOfProductionZones.get(k)[i].size(); j++){

                    System.out.print(setOfProductionZones.get(k)[i].get(j));
                }

            }
            System.out.print("\n");
        }
        System.out.println();

        if(serializableLeaderProductionZone != null && serializableLeaderProductionZone.length > 0){
            System.out.println("PRODUCTION ZONE PLUS");
            System.out.println();
            ArrayList<SerializableLeaderCard> leaderCards = new ArrayList<>();
            for (int i=0; i< serializableLeaderProductionZone.length; i++){
                leaderCards.add(serializableLeaderProductionZone[i].getCard());
            }

            LeaderCardsPrinter.print(leaderCards);
        }


    }

    private static ArrayList<String>[] emptyCard() {

        String emptyLine = "║                 ║";
        String firstLine = "╔═════════════════╗";
        String lastLine = "╚═════════════════╝";

        ArrayList<String>[] card = new ArrayList[15];
        for (int i= 0; i< card.length; i++){
            card[i] = new ArrayList<>();
        }

        card[0].add(firstLine);
        for (int i=1; i<14; i++){
            card[i].add(emptyLine);
        }
        card[14].add(lastLine);

        return card;
    }
}
