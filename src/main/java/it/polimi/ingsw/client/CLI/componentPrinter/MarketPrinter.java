package it.polimi.ingsw.client.CLI.componentPrinter;

import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableMarket;

import java.util.ArrayList;

import static it.polimi.ingsw.utils.Constants.*;

public class MarketPrinter {

    public static void print(SerializableMarket serializableMarket) {

        String space = " ";
        String verticalIntern = "┃";
        String horizontalIntern = "━";
        String upArrow = "\u2191";
        String leftArrow = "\u2190";

        String halfspace = "\u200A";

        ArrayList<String>[] market = new ArrayList[9];

        for(int i=0; i<market.length;i++){
            market[i] = new ArrayList<>();
        }

        //prima linea
        String firstLine = " ";
        market[0].add(firstLine);

        market[1].add(space);
        addCorrectResource(market[1], serializableMarket.getMarket()[0][0]);
        market[1].add(space);
        market[1].add(verticalIntern);
        market[1].add(space);
        addCorrectResource(market[1], serializableMarket.getMarket()[0][1]);
        market[1].add(space);
        market[1].add(verticalIntern);
        market[1].add(space);
        addCorrectResource(market[1], serializableMarket.getMarket()[0][2]);
        market[1].add(space);
        market[1].add(verticalIntern);
        market[1].add(space);
        addCorrectResource(market[1], serializableMarket.getMarket()[0][3]);
        market[1].add(space);
        market[1].add(space);
        market[1].add(space);
        market[1].add(leftArrow);
        market[1].add(" 0");

        //terzaLinea

        market[2].add(space);
        market[2].add(horizontalIntern);
        market[2].add(horizontalIntern);
        market[2].add(horizontalIntern);
        market[2].add(horizontalIntern);
        market[2].add(horizontalIntern);
        market[2].add(horizontalIntern);
        market[2].add(horizontalIntern);
        market[2].add(horizontalIntern);
        market[2].add(horizontalIntern);
        market[2].add(horizontalIntern);
        market[2].add(horizontalIntern);
        market[2].add(horizontalIntern);
        market[2].add(horizontalIntern);




        // quarta linea

        market[3].add(space);
        addCorrectResource(market[3], serializableMarket.getMarket()[1][0]);
        market[3].add(space);
        market[3].add(verticalIntern);
        market[3].add(space);
        addCorrectResource(market[3], serializableMarket.getMarket()[1][1]);;
        market[3].add(space);
        market[3].add(verticalIntern);
        market[3].add(space);
        addCorrectResource(market[3], serializableMarket.getMarket()[1][2]);
        market[3].add(space);
        market[3].add(verticalIntern);
        market[3].add(space);
        addCorrectResource(market[3], serializableMarket.getMarket()[1][3]);
        market[3].add(space);
        market[3].add(space);
        market[3].add(space);
        market[3].add(leftArrow);
        market[3].add(" 1");

        //quinta
        market[4].add(space);
        market[4].add(horizontalIntern);
        market[4].add(horizontalIntern);
        market[4].add(horizontalIntern);
        market[4].add(horizontalIntern);
        market[4].add(horizontalIntern);
        market[4].add(horizontalIntern);
        market[4].add(horizontalIntern);
        market[4].add(horizontalIntern);
        market[4].add(horizontalIntern);
        market[4].add(horizontalIntern);
        market[4].add(horizontalIntern);
        market[4].add(horizontalIntern);
        market[4].add(horizontalIntern);


        //sesta

        market[5].add(space);
        addCorrectResource(market[5], serializableMarket.getMarket()[2][0]);
        market[5].add(space);
        market[5].add(verticalIntern);
        market[5].add(space);
        addCorrectResource(market[5], serializableMarket.getMarket()[2][1]);
        market[5].add(space);
        market[5].add(verticalIntern);
        market[5].add(space);
        addCorrectResource(market[5], serializableMarket.getMarket()[2][2]);
        market[5].add(space);
        market[5].add(verticalIntern);
        market[5].add(space);
        addCorrectResource(market[5], serializableMarket.getMarket()[2][3]);
        market[5].add(space);
        market[5].add(space);
        market[5].add(space);
        market[5].add(leftArrow);
        market[5].add(" 2");

        //riga frecce

        market[6].add(space);

        market[7].add(halfspace);
        market[7].add(upArrow);
        market[7].add(space);
        market[7].add(space);
        market[7].add(space);
        market[7].add(upArrow);
        market[7].add(space);
        market[7].add(space);
        market[7].add(space);
        market[7].add(upArrow);
        market[7].add(space);
        market[7].add(space);
        market[7].add(space);
        market[7].add(upArrow);
        market[7].add(space);
        market[7].add(space);


        market[8].add(halfspace);
        market[8].add("0");
        market[8].add(space);
        market[8].add(space);
        market[8].add(space);
        market[8].add("1");
        market[8].add(space);
        market[8].add(space);
        market[8].add(space);
        market[8].add("2");
        market[8].add(space);
        market[8].add(space);
        market[8].add(space);
        market[8].add("3");
        market[8].add(space);
        market[8].add(space);
        market[8].add(space);

        System.out.println(ANSI_GREEN + "\n" +
                        "  __  __            _        _   \n" +
                        " |  \\/  |          | |      | |  \n" +
                        " | \\  / | __ _ _ __| | _____| |_ \n" +
                        " | |\\/| |/ _` | '__| |/ / _ \\ __|\n" +
                        " | |  | | (_| | |  |   <  __/ |_ \n" +
                        " |_|  |_|\\__,_|_|  |_|\\_\\___|\\__|\n" +
                        ANSI_RESET);

        for (int i=0; i< market.length; i++){
            for (int j=0; j<market[i].size(); j++){
                if(j==market[i].size()-1){
                    System.out.println(market[i].get(j));
                }else{
                    System.out.print(market[i].get(j));
                }
            }
        }

        switch (serializableMarket.getExternalResource()){
            case COIN:
                System.out.println(ANSI_GREEN + "External Resource: " + ANSI_RESET + coin) ;
                break;
            case ROCK:
                System.out.println(ANSI_GREEN + "External Resource: " + ANSI_RESET + rock );
                break;
            case FAITH:
                System.out.println(ANSI_GREEN + "External Resource: " + ANSI_RESET + faith );
                break;
            case NOTHING:
                System.out.println(ANSI_GREEN + "External Resource: " + ANSI_RESET + nothing );
                break;
            case SERVANT:
                System.out.println(ANSI_GREEN + "External Resource: " + ANSI_RESET + servant);
                break;
            case SHIELD:
                System.out.println(ANSI_GREEN + "External Resource: " + ANSI_RESET + shield );
                break;

        }


    }

    private static void addCorrectResource(ArrayList<String> line, Resource resource) {
        switch (resource) {
            case COIN:
                line.add(coin);
                break;
            case SERVANT:
                line.add(servant);
                break;
            case SHIELD:
                line.add(shield);
                break;
            case ROCK:
                line.add(rock);
                break;
            case FAITH:
                line.add(faith);
                break;
            case NOTHING:
                line.add(nothing);
        }
    }
}
