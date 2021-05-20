package it.polimi.ingsw.client.CLI.componentPrinter;

import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableEvolutionSection;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;

import static it.polimi.ingsw.utils.Constants.*;

public class EvolutionSectionPrinter {

    public static void print(SerializableEvolutionSection serializableEvolutionSection){
        System.out.println(ANSI_YELLOW + "\n" +
                "  ______          _       _   _             \n" +
                " |  ____|        | |     | | (_)            \n" +
                " | |____   _____ | |_   _| |_ _  ___  _ __  \n" +
                " |  __\\ \\ / / _ \\| | | | | __| |/ _ \\| '_ \\ \n" +
                " | |___\\ V / (_) | | |_| | |_| | (_) | | | |\n" +
                " |______\\_/ \\___/|_|\\__,_|\\__|_|\\___/|_| |_|\n" +
                "   _____           _   _                    \n" +
                "  / ____|         | | (_)                   \n" +
                " | (___   ___  ___| |_ _  ___  _ __         \n" +
                "  \\___ \\ / _ \\/ __| __| |/ _ \\| '_ \\        \n" +
                "  ____) |  __/ (__| |_| | (_) | | | |       \n" +
                " |_____/ \\___|\\___|\\__|_|\\___/|_| |_|       \n" +
                "                                            \n" +
                ANSI_RESET);

        for (int x=0; x<serializableEvolutionSection.getEvolutionCards().length; x++){
            ArrayList<ArrayList<String>[]> line = createLines(serializableEvolutionSection.getEvolutionCards()[x]);
            for (int i=0; i< line.get(0).length; i++){
                for(int k=0; k<line.size(); k++){
                    for (int j=0; j<line.get(0)[i].size(); j++){

                        System.out.print(line.get(k)[i].get(j));
                    }

                }
                System.out.print("\n");
            }
            System.out.println();
        }
    }

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
        card[13].add(fourSpaces);
        card[13].add(vertical);

        return card;
    }

    private static  ArrayList<ArrayList<String>[]> createLines(EvolutionCard[] evolutionCards){
        ArrayList<ArrayList<String>[]> line = new ArrayList<>();
        for(int i = 0; i<evolutionCards.length; i++){
            line.add(createEvolutionCard(evolutionCards[i]));
        }
        return line;
    }
}
