package it.polimi.ingsw.client.cli.componentPrinter;

import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableEvolutionSection;

import java.util.ArrayList;
import java.util.Collections;

import static it.polimi.ingsw.utils.Constants.*;

/**
 * Class able to print the evolution section in the cli
 */
public class EvolutionSectionPrinter {

    /**
     * method that organize the creation of the section and his printing order
     * @param serializableEvolutionSection it the evolution section that has to be printed
     */
    public static void print(SerializableEvolutionSection serializableEvolutionSection){
        System.out.println(ANSI_YELLOW + "\n" + evolutionSectionTitle + ANSI_RESET);

        for (int x=0; x<serializableEvolutionSection.getEvolutionCards().length; x++){
            ArrayList<ArrayList<String>[]> line = createLines(serializableEvolutionSection.getEvolutionCards()[x]);
            for (int i=0; i< line.get(0).length; i++){
                for (ArrayList<String>[] arrayLists : Collections.unmodifiableList(line)) {
                    for (int j = 0; j < arrayLists[i].size(); j++) {
                        System.out.print(arrayLists[i].get(j));
                    }
                }
                System.out.print("\n");
            }
            System.out.println();
        }
    }

    /**
     * method that is able to create the ArrayList of string that contains a single evolution card
     * @param evolutionCard is the evolution card that has to be created
     * @return the arrayList of strings that compose the card
     */
    public static ArrayList<String>[] createEvolutionCard(EvolutionCard evolutionCard){

        String color = "Color:";
        String level = "Level:";
        String points = "Points: ";
        String arrow = "\u2192";
        String space = " ";
        String fiveSpaces = "     ";
        String emptyLine = "                 ";
        String threespaces = "   ";
        String fourSpaces = "    ";
        String vertical = "║";
        String firstLine = "╔═════════════════╗";
        String lastLine = "╚═════════════════╝";

        ArrayList<String>[] card = new ArrayList[15];
        for (int i= 0; i< card.length; i++){
            card[i] = new ArrayList<>();
        }

        card[0].add(firstLine);
        card[14].add(lastLine);
        for (int i=1; i< card.length-1; i++){
            card[i].add(vertical);
        }

        //second line
        card[1].add(fiveSpaces);
        card[1].add(color);
        card[1].add(evolutionCard.getColor().label);
        card[1].add(fiveSpaces);
        card[1].add(vertical);

        //third line
        card[2].add(fiveSpaces);
        card[2].add(level);
        card[2].add(String.valueOf(evolutionCard.getLevel().ordinal()+1));
        card[2].add(fiveSpaces);
        card[2].add(vertical);

        //fourth line
        card[3].add(emptyLine);
        card[3].add(vertical);

        //fifth line
        card[4].add(fiveSpaces);
        card[4].add(rock);
        card[4].add(":");
        card[4].add(String.valueOf(evolutionCard.getCost().get(Resource.ROCK)));
        card[4].add(space);
        card[4].add(shield);
        card[4].add(":");
        card[4].add(String.valueOf(evolutionCard.getCost().get(Resource.SHIELD)));
        card[4].add(fiveSpaces);
        card[4].add(vertical);

        //sixth
        card[5].add(fiveSpaces);
        card[5].add(coin);
        card[5].add(":");
        card[5].add(String.valueOf(evolutionCard.getCost().get(Resource.COIN)));
        card[5].add(space);
        card[5].add(servant);
        card[5].add(":");
        card[5].add(String.valueOf(evolutionCard.getCost().get(Resource.SERVANT)));
        card[5].add(fiveSpaces);
        card[5].add(vertical);

        //seventh line
        card[6].add(emptyLine);
        card[6].add(vertical);

        //eighth line
        card[7].add(fourSpaces);
        card[7].add(rock);
        card[7].add(":");
        card[7].add(String.valueOf(evolutionCard.getRequires().get(Resource.ROCK)));
        card[7].add(threespaces);
        card[7].add(rock);
        card[7].add(":");
        card[7].add(String.valueOf(evolutionCard.getProduction().get(Resource.ROCK)));
        card[7].add(fourSpaces);
        card[7].add(vertical);

        //ninth line
        card[8].add(fourSpaces);
        card[8].add(shield);
        card[8].add(":");
        card[8].add(String.valueOf(evolutionCard.getRequires().get(Resource.SHIELD)));
        card[8].add(space);
        card[8].add(arrow);
        card[8].add(space);
        card[8].add(shield);
        card[8].add(":");
        card[8].add(String.valueOf(evolutionCard.getProduction().get(Resource.SHIELD)));
        card[8].add(fourSpaces);
        card[8].add(vertical);

        //tenth line
        card[9].add(fourSpaces);
        card[9].add(coin);
        card[9].add(":");
        card[9].add(String.valueOf(evolutionCard.getRequires().get(Resource.COIN)));
        card[9].add(threespaces);
        card[9].add(coin);
        card[9].add(":");
        card[9].add(String.valueOf(evolutionCard.getProduction().get(Resource.COIN)));
        card[9].add(fourSpaces);
        card[9].add(vertical);

        //eleventh line
        card[10].add(fourSpaces);
        card[10].add(servant);
        card[10].add(":");
        card[10].add(String.valueOf(evolutionCard.getRequires().get(Resource.SERVANT)));
        card[10].add(threespaces);
        card[10].add(servant);
        card[10].add(":");
        card[10].add(String.valueOf(evolutionCard.getProduction().get(Resource.SERVANT)));
        card[10].add(fourSpaces);
        card[10].add(vertical);

        //twelfth
        card[11].add(fiveSpaces);
        card[11].add(fiveSpaces);
        card[11].add(faith);
        card[11].add(":");
        card[11].add(String.valueOf(evolutionCard.getProduction().get(Resource.FAITH)));
        card[11].add(fourSpaces);
        card[11].add(vertical);

        //thirteenth
        card[12].add(emptyLine);
        card[12].add(vertical);

        //fourteenth
        card[13].add(fourSpaces);
        card[13].add(points);
        card[13].add(String.valueOf(evolutionCard.getPoint()));
        if(evolutionCard.getPoint() < 10){
            card[13].add(fourSpaces);
        }else{
            card[13].add(threespaces);
        }

        card[13].add(vertical);

        return card;
    }

    /**
     * method that create a line that compose the evolution section
     * @param evolutionCards is the array of cards that compose the line
     * @return all the strings used to define a line of the evolution section
     */
    private static  ArrayList<ArrayList<String>[]> createLines(EvolutionCard[] evolutionCards){
        ArrayList<ArrayList<String>[]> line = new ArrayList<>();
        for (EvolutionCard evolutionCard : evolutionCards) {
            if (evolutionCard != null) {
                line.add(createEvolutionCard(evolutionCard));
            } else {
                line.add(createEmptyCard());
            }

        }
        return line;
    }

    /**
     * method that creates the strings to reproduce an empty card
     * @return the arraylist representing the empty card
     */
    private static ArrayList<String>[] createEmptyCard(){
        String firstLine = "╔═════════════════╗";
        String lastLine = "╚═════════════════╝";
        String emptyLine = "║                 ║";
        ArrayList<String>[] card;
        card = new ArrayList[15];
        for (int i= 0; i< card.length; i++){
            card[i] = new ArrayList<>();

        }

        card[0].add(firstLine);
        card[14].add(lastLine);

        for (int i=1; i<14; i++){
            card[i].add(emptyLine);
        }

        return card;
    }
}
