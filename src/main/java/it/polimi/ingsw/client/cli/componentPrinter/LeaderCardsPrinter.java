package it.polimi.ingsw.client.cli.componentPrinter;

import it.polimi.ingsw.model.cards.CardColor;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;

import static it.polimi.ingsw.utils.Constants.*;

public class LeaderCardsPrinter {

    private static final String firstLine = "╔════════════╗";
    private static final String lastLine = "╚════════════╝";
    private static final String vertical = "║";
    private static final String emptyLine = "║            ║";
    private static final String arrow = "\u2192";
    private static final String upArrow = "\u2191";

    public static void print(ArrayList<SerializableLeaderCard> serializableLeaderCards){

        System.out.println(Constants.ANSI_PURPLE + "\n" +  leaderCardTitle + Constants.ANSI_RESET);

        ArrayList<ArrayList<String>[]> deck = new ArrayList<>();
        for (SerializableLeaderCard serializableLeaderCard : serializableLeaderCards){
            switch (serializableLeaderCard.getAbilityType()){
                case SALES:
                    deck.add(makeLeaderCardSales(serializableLeaderCard));
                    break;
                case STOCKPLUS:
                    deck.add(makeLeaderCardStock(serializableLeaderCard));
                    break;
                case NOMOREWHITE:
                    deck.add(makeLeaderCardWhite(serializableLeaderCard));
                    break;
                case PRODUCTIONPOWER:
                    deck.add(makeLeaderCardProduction(serializableLeaderCard));
                    break;
            }
        }

        if(!(deck != null && deck.size() > 0)){
            return;
        }
        for (int i=0; i< deck.get(0).length; i++){
            for(int k=0; k<deck.size(); k++){
                for (int j=0; j<deck.get(k)[i].size(); j++){

                    System.out.print(deck.get(k)[i].get(j));
                }

            }
            System.out.print("\n");
        }

        if(serializableLeaderCards.size() == 4){
            for (int i=0; i<deck.size(); i++){
                System.out.print("      "+upArrow+"       ");
            }
            System.out.println();
            for (int i=0; i<deck.size(); i++){
                System.out.print("      "+i+"       ");
            }
            System.out.println();
        }


    }

    private static ArrayList<String>[] makeLeaderCardProduction(SerializableLeaderCard leaderCard){

        ArrayList<String>[] leaderCardProduction = new ArrayList[15];

        for(int i=0; i<15; i++) {
            leaderCardProduction[i] = new ArrayList<>();
        }

        leaderCardProduction[0].add(firstLine);

        leaderCardProduction[1].add(vertical);
        leaderCardProduction[1].add(" ");
        leaderCardProduction[1].add("PRODUCTION");
        leaderCardProduction[1].add(" ");
        leaderCardProduction[1].add(vertical);

        leaderCardProduction[2].add(emptyLine);

        leaderCardProduction[3].add(vertical);
        leaderCardProduction[3].add("   ");
        leaderCardProduction[3].add("CARD ");
        leaderCardProduction[3].add(leaderCard.getRequiresColor()[0].label);
        leaderCardProduction[3].add("   ");
        leaderCardProduction[3].add(vertical);

        leaderCardProduction[4].add(vertical);
        leaderCardProduction[4].add("  ");
        leaderCardProduction[4].add("LEVEL: 2");
        leaderCardProduction[4].add("  ");
        leaderCardProduction[4].add(vertical);

        leaderCardProduction[5].add(emptyLine);

        leaderCardProduction[6].add(vertical);
        leaderCardProduction[6].add("  ");
        leaderCardProduction[6].add("POINTS:4");
        leaderCardProduction[6].add("  ");
        leaderCardProduction[6].add(vertical);

        leaderCardProduction[7].add(emptyLine);
        leaderCardProduction[8].add(emptyLine);

        leaderCardProduction[9].add(vertical);
        leaderCardProduction[9].add("  ");
        if(leaderCard.getAbilityResource().get(Resource.COIN) != 0){
            leaderCardProduction[9].add(coin);
        }
        if(leaderCard.getAbilityResource().get(Resource.SHIELD) != 0){
            leaderCardProduction[9].add(shield);
        }
        if(leaderCard.getAbilityResource().get(Resource.SERVANT) != 0){
            leaderCardProduction[9].add(servant);
        }
        if(leaderCard.getAbilityResource().get(Resource.ROCK) != 0){
            leaderCardProduction[9].add(rock);
        }

        leaderCardProduction[9].add(" ");
        leaderCardProduction[9].add(arrow);
        leaderCardProduction[9].add(" ");
        leaderCardProduction[9].add(nothing);
        leaderCardProduction[9].add("  ");
        leaderCardProduction[9].add(faith);
        leaderCardProduction[9].add("  ");
        leaderCardProduction[9].add(vertical);

        leaderCardProduction[10].add(emptyLine);

        leaderCardProduction[11].add(vertical);
        leaderCardProduction[11].add("  ");
        if(leaderCard.isActive()){
            leaderCardProduction[11].add("ACTIVE:Y");
        }else{
            leaderCardProduction[11].add("ACTIVE:N");
        }
        leaderCardProduction[11].add("  ");
        leaderCardProduction[11].add(vertical);

        leaderCardProduction[12].add(vertical);
        leaderCardProduction[12].add("  ");
        if(leaderCard.isUsed()){
            leaderCardProduction[12].add("IN USE:Y");
        }else{
            leaderCardProduction[12].add("IN USE:N");
        }
        leaderCardProduction[12].add("  ");
        leaderCardProduction[12].add(vertical);

        leaderCardProduction[13].add(vertical);
        leaderCardProduction[13].add("   ID: ");
        leaderCardProduction[13].add(String.valueOf(leaderCard.getId()));
        if(leaderCard.getId() > 9){
            leaderCardProduction[13].add("   ");
        }else{
            leaderCardProduction[13].add("    ");
        }
        leaderCardProduction[13].add(vertical);
        leaderCardProduction[14].add(lastLine);

        return leaderCardProduction;
    }

    private static ArrayList<String>[] makeLeaderCardWhite(SerializableLeaderCard leaderCard){

        ArrayList<String>[] leaderCardWhite = new ArrayList[15];

        for(int i=0; i<15; i++){
            leaderCardWhite[i] = new ArrayList<>();
        }

        CardColor[] requestedCards = new CardColor[2];
        if(leaderCard.getRequiresColor()[0].equals(leaderCard.getRequiresColor()[1]) || leaderCard.getRequiresColor()[0].equals(leaderCard.getRequiresColor()[2])){
            requestedCards[0] = leaderCard.getRequiresColor()[0];

            if(leaderCard.getRequiresColor()[0].equals(leaderCard.getRequiresColor()[1])){
                requestedCards[1] = leaderCard.getRequiresColor()[2];
            }else{
                requestedCards[1] = leaderCard.getRequiresColor()[1];
            }

        }else{
            requestedCards[1] = leaderCard.getRequiresColor()[0];
            requestedCards[0] = leaderCard.getRequiresColor()[1];
        }

        leaderCardWhite[0].add(firstLine);

        leaderCardWhite[1].add(vertical);
        leaderCardWhite[1].add("   ");
        leaderCardWhite[1].add("WHITE");
        leaderCardWhite[1].add("    ");
        leaderCardWhite[1].add(vertical);

        leaderCardWhite[2].add(emptyLine);

        leaderCardWhite[3].add(vertical);
        leaderCardWhite[3].add(" ");
        leaderCardWhite[3].add(requestedCards[0].label);
        leaderCardWhite[3].add(" CARD:  2");
        leaderCardWhite[3].add(" ");
        leaderCardWhite[3].add(vertical);

        leaderCardWhite[4].add(vertical);
        leaderCardWhite[4].add(" ");
        leaderCardWhite[4].add(requestedCards[1].label);
        leaderCardWhite[4].add(" CARD:  1");
        leaderCardWhite[4].add(" ");
        leaderCardWhite[4].add(vertical);

        leaderCardWhite[5].add(emptyLine);

        leaderCardWhite[6].add(vertical);
        leaderCardWhite[6].add("  ");
        leaderCardWhite[6].add("POINTS:5");
        leaderCardWhite[6].add("  ");
        leaderCardWhite[6].add(vertical);

        leaderCardWhite[7].add(emptyLine);
        leaderCardWhite[8].add(emptyLine);

        leaderCardWhite[9].add(vertical);
        leaderCardWhite[9].add("   ");
        leaderCardWhite[9].add(nothing);
        leaderCardWhite[9].add(" = ");
        if(leaderCard.getAbilityResource().get(Resource.COIN) != 0){
            leaderCardWhite[9].add(coin);
        }
        if(leaderCard.getAbilityResource().get(Resource.SHIELD) != 0){
            leaderCardWhite[9].add(shield);
        }
        if(leaderCard.getAbilityResource().get(Resource.SERVANT) != 0){
            leaderCardWhite[9].add(servant);
        }
        if(leaderCard.getAbilityResource().get(Resource.ROCK) != 0){
            leaderCardWhite[9].add(rock);
        }

        leaderCardWhite[9].add("    ");
        leaderCardWhite[9].add(vertical);

        leaderCardWhite[10].add(emptyLine);

        leaderCardWhite[11].add(vertical);
        leaderCardWhite[11].add("  ");
        if(leaderCard.isActive()){
            leaderCardWhite[11].add("ACTIVE:Y");
        }else{
            leaderCardWhite[11].add("ACTIVE:N");
        }
        leaderCardWhite[11].add("  ");
        leaderCardWhite[11].add(vertical);

        leaderCardWhite[12].add(vertical);
        leaderCardWhite[12].add("  ");
        if(leaderCard.isUsed()){
            leaderCardWhite[12].add("IN USE:Y");
        }else{
            leaderCardWhite[12].add("IN USE:N");
        }
        leaderCardWhite[12].add("  ");
        leaderCardWhite[12].add(vertical);

        leaderCardWhite[13].add(vertical);
        leaderCardWhite[13].add("   ID: ");
        leaderCardWhite[13].add(String.valueOf(leaderCard.getId()));
        if(leaderCard.getId() > 9){
            leaderCardWhite[13].add("   ");
        }else{
            leaderCardWhite[13].add("    ");
        }
        leaderCardWhite[13].add(vertical);
        leaderCardWhite[14].add(lastLine);


        return leaderCardWhite;
    }

    private static ArrayList<String>[] makeLeaderCardStock(SerializableLeaderCard leaderCard){

        ArrayList<String>[] leaderCardStock = new ArrayList[15];

        for(int i=0; i<15; i++){
            leaderCardStock[i] = new ArrayList<>();
        }

        leaderCardStock[0].add(firstLine);

        leaderCardStock[1].add(vertical);
        leaderCardStock[1].add("   ");
        leaderCardStock[1].add("STOCK");
        leaderCardStock[1].add("    ");
        leaderCardStock[1].add(vertical);

        leaderCardStock[2].add(emptyLine);

        leaderCardStock[3].add(vertical);
        leaderCardStock[3].add("  ");
        leaderCardStock[3].add("RESOURCE");
        leaderCardStock[3].add("  ");
        leaderCardStock[3].add(vertical);

        leaderCardStock[4].add(vertical);
        leaderCardStock[4].add("    ");
        if(leaderCard.getRequires().get(Resource.COIN) != 0){
            leaderCardStock[4].add(coin);
        }
        if(leaderCard.getRequires().get(Resource.SHIELD) != 0){
            leaderCardStock[4].add(shield);
        }
        if(leaderCard.getRequires().get(Resource.SERVANT) != 0){
            leaderCardStock[4].add(servant);
        }
        if(leaderCard.getRequires().get(Resource.ROCK) != 0){
            leaderCardStock[4].add(rock);
        }
        leaderCardStock[4].add("  5");
        leaderCardStock[4].add("    ");
        leaderCardStock[4].add(vertical);

        leaderCardStock[5].add(emptyLine);

        leaderCardStock[6].add(vertical);
        leaderCardStock[6].add("  ");
        leaderCardStock[6].add("POINTS:3");
        leaderCardStock[6].add("  ");
        leaderCardStock[6].add(vertical);

        leaderCardStock[7].add(emptyLine);
        leaderCardStock[8].add(emptyLine);

        leaderCardStock[9].add(vertical);
        leaderCardStock[9].add("    ");
        if(leaderCard.getAbilityResource().get(Resource.COIN) != 0){
            leaderCardStock[9].add(coin);
        }
        if(leaderCard.getAbilityResource().get(Resource.SHIELD) != 0){
            leaderCardStock[9].add(shield);
        }
        if(leaderCard.getAbilityResource().get(Resource.SERVANT) != 0){
            leaderCardStock[9].add(servant);
        }
        if(leaderCard.getAbilityResource().get(Resource.ROCK) != 0){
            leaderCardStock[9].add(rock);
        }
        leaderCardStock[9].add("  ");
        if(leaderCard.getAbilityResource().get(Resource.COIN) != 0){
            leaderCardStock[9].add(coin);
        }
        if(leaderCard.getAbilityResource().get(Resource.SHIELD) != 0){
            leaderCardStock[9].add(shield);
        }
        if(leaderCard.getAbilityResource().get(Resource.SERVANT) != 0){
            leaderCardStock[9].add(servant);
        }
        if(leaderCard.getAbilityResource().get(Resource.ROCK) != 0){
            leaderCardStock[9].add(rock);
        }
        leaderCardStock[9].add("    ");
        leaderCardStock[9].add(vertical);

        leaderCardStock[10].add(emptyLine);

        leaderCardStock[11].add(vertical);
        leaderCardStock[11].add("  ");
        if(leaderCard.isActive()){
            leaderCardStock[11].add("ACTIVE:Y");
        }else{
            leaderCardStock[11].add("ACTIVE:N");
        }
        leaderCardStock[11].add("  ");
        leaderCardStock[11].add(vertical);

        leaderCardStock[12].add(vertical);
        leaderCardStock[12].add("  ");
        if(leaderCard.isUsed()){
            leaderCardStock[12].add("IN USE:Y");
        }else{
            leaderCardStock[12].add("IN USE:N");
        }
        leaderCardStock[12].add("  ");
        leaderCardStock[12].add(vertical);

        leaderCardStock[13].add(vertical);
        leaderCardStock[13].add("   ID: ");
        leaderCardStock[13].add(String.valueOf(leaderCard.getId()));
        if(leaderCard.getId() > 9){
            leaderCardStock[13].add("   ");
        }else{
            leaderCardStock[13].add("    ");
        }
        leaderCardStock[13].add(vertical);
        leaderCardStock[14].add(lastLine);

        return leaderCardStock;
    }

    private static ArrayList<String>[] makeLeaderCardSales(SerializableLeaderCard leaderCard){

        ArrayList<String>[] leaderCardSale = new ArrayList[15];

        for(int i=0; i<15; i++){
            leaderCardSale[i] = new ArrayList<>();
        }

        leaderCardSale[0].add(firstLine);

        leaderCardSale[1].add(vertical);
        leaderCardSale[1].add("    ");
        leaderCardSale[1].add("SALE");
        leaderCardSale[1].add("    ");
        leaderCardSale[1].add(vertical);

        leaderCardSale[2].add(emptyLine);

        leaderCardSale[3].add(vertical);
        leaderCardSale[3].add(" ");
        leaderCardSale[3].add(leaderCard.getRequiresColor()[0].label);
        leaderCardSale[3].add(" CARD:  1");
        leaderCardSale[3].add(" ");
        leaderCardSale[3].add(vertical);

        leaderCardSale[4].add(vertical);
        leaderCardSale[4].add(" ");
        leaderCardSale[4].add(leaderCard.getRequiresColor()[1].label);
        leaderCardSale[4].add(" CARD:  1");
        leaderCardSale[4].add(" ");
        leaderCardSale[4].add(vertical);

        leaderCardSale[5].add(emptyLine);

        leaderCardSale[6].add(vertical);
        leaderCardSale[6].add("  ");
        leaderCardSale[6].add("POINTS:2");
        leaderCardSale[6].add("  ");
        leaderCardSale[6].add(vertical);

        leaderCardSale[7].add(emptyLine);
        leaderCardSale[8].add(emptyLine);

        leaderCardSale[9].add(vertical);
        leaderCardSale[9].add("    -1 ");
        if(leaderCard.getAbilityResource().get(Resource.COIN) != 0){
            leaderCardSale[9].add(coin);
        }
        if(leaderCard.getAbilityResource().get(Resource.SHIELD) != 0){
            leaderCardSale[9].add(shield);
        }
        if(leaderCard.getAbilityResource().get(Resource.SERVANT) != 0){
            leaderCardSale[9].add(servant);
        }
        if(leaderCard.getAbilityResource().get(Resource.ROCK) != 0){
            leaderCardSale[9].add(rock);
        }

        leaderCardSale[9].add("    ");
        leaderCardSale[9].add(vertical);

        leaderCardSale[10].add(emptyLine);

        leaderCardSale[11].add(vertical);
        leaderCardSale[11].add("  ");
        if(leaderCard.isActive()){
            leaderCardSale[11].add("ACTIVE:Y");
        }else{
            leaderCardSale[11].add("ACTIVE:N");
        }
        leaderCardSale[11].add("  ");
        leaderCardSale[11].add(vertical);

        leaderCardSale[12].add(vertical);
        leaderCardSale[12].add("  ");
        if(leaderCard.isUsed()){
            leaderCardSale[12].add("IN USE:Y");
        }else{
            leaderCardSale[12].add("IN USE:N");
        }
        leaderCardSale[12].add("  ");
        leaderCardSale[12].add(vertical);

        leaderCardSale[13].add(vertical);
        leaderCardSale[13].add("   ID: ");
        leaderCardSale[13].add(String.valueOf(leaderCard.getId()));
        if(leaderCard.getId() > 9){
            leaderCardSale[13].add("   ");
        }else{
            leaderCardSale[13].add("    ");
        }
        leaderCardSale[13].add(vertical);

        leaderCardSale[14].add(lastLine);

        return leaderCardSale;
    }

}
