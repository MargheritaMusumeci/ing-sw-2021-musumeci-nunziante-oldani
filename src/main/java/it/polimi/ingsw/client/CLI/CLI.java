package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.client.CLI.componentPrinter.*;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.gamePhases.InitializationPhase;
import it.polimi.ingsw.client.gamePhases.Phase;
import it.polimi.ingsw.messages.sentByClient.EndTurnMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.*;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.LeaderCardChoiceMessage;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.NickNameMessage;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.NumberOfPlayerMessage;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.SelectedInitialResourceMessage;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.*;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class CLI implements UI, Runnable {

    private Scanner scanner;
    private ClientSocket clientSocket;
    private Phase gamePhase;
    private boolean isAckArrived;
    private boolean isNackArrived;
    private String nickname;
    private Socket socket;
    private int numberOfPlayers;
    private ArrayList<SerializableLeaderCard> leaderCards;
    private ArrayList<Resource> resources;
    private boolean serverIsUp;
    private boolean isActionBeenDone;


    public CLI(){
        scanner = new Scanner(System.in);
        isNackArrived = false;
        isAckArrived = false;
        leaderCards = null;
        resources = null;
        gamePhase = new InitializationPhase();
        isActionBeenDone = false;
        new Thread(this).start();
    }

    //metodi per stampare le componenti del gioco
    public void printTitle(){

        System.out.println(Constants.ANSI_RED + "\n" +
                "███╗   ███╗ █████╗ ███████╗████████╗███████╗██████╗      ██████╗ ███████╗              \n" +
                "████╗ ████║██╔══██╗██╔════╝╚══██╔══╝██╔════╝██╔══██╗    ██╔═══██╗██╔════╝              \n" +
                "██╔████╔██║███████║███████╗   ██║   █████╗  ██████╔╝    ██║   ██║█████╗                \n" +
                "██║╚██╔╝██║██╔══██║╚════██║   ██║   ██╔══╝  ██╔══██╗    ██║   ██║██╔══╝                \n" +
                "██║ ╚═╝ ██║██║  ██║███████║   ██║   ███████╗██║  ██║    ╚██████╔╝██║                   \n" +
                "╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝     ╚═════╝ ╚═╝                   \n" +
                "                                                                                       \n" +
                "██████╗ ███████╗███╗   ██╗ █████╗ ██╗███████╗███████╗ █████╗ ███╗   ██╗ ██████╗███████╗\n" +
                "██╔══██╗██╔════╝████╗  ██║██╔══██╗██║██╔════╝██╔════╝██╔══██╗████╗  ██║██╔════╝██╔════╝\n" +
                "██████╔╝█████╗  ██╔██╗ ██║███████║██║███████╗███████╗███████║██╔██╗ ██║██║     █████╗  \n" +
                "██╔══██╗██╔══╝  ██║╚██╗██║██╔══██║██║╚════██║╚════██║██╔══██║██║╚██╗██║██║     ██╔══╝  \n" +
                "██║  ██║███████╗██║ ╚████║██║  ██║██║███████║███████║██║  ██║██║ ╚████║╚██████╗███████╗\n" +
                "╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝╚══════╝\n" +
                "                                                                                       \n" +
                Constants.ANSI_RESET);
    }
    public void printMenu(){
        System.out.println("\n" +
                "+--------------------------+---------------------------+\n" +
                "|                  Possible Actions:                   |\n" +
                "+--------------------------+---------------------------+\n" +
                "| Actions                  | Print                     |\n" +
                "| 0: END TURN              | 1: Show Leader Cards      |\n" +
                "| 8: Activate Leader Cards | 2: Show Stock             |\n" +
                "| 9: Discard Leader Cards  | 3: Show Lock Box          |\n" +
                "| 10: Buy From Market      | 4: Show Pope Track        |\n" +
                "| 11: Active Production    | 5: Show Production Zones  |\n" +
                "| 12: Buy Evolution Cards  | 6: Show Market            |\n" +
                "| 13: Use Leader Cards     | 7: Show Evolution section |\n" +
                "|               14: Print Enemy DashBoard              |\n" +
                "+--------------------------+---------------------------+\n" +
                "\n");

        System.out.print(Constants.ANSI_CYAN + "Choose your action: " + Constants.ANSI_RESET);
    }
    public void printMenuOtherTurn(){
        System.out.println("\n" +
                "+---------------------------+\n" +
                "|     Possible Actions      |\n" +
                "+---------------------------+\n" +
                "| 1: Show Leader Cards      |\n" +
                "| 2: Show Stock             |\n" +
                "| 3: Show Lockbox           |\n" +
                "| 4: Show Pope Track        |\n" +
                "| 5: Show Production Zones  |\n" +
                "| 6: Show Market            |\n" +
                "| 7: Show Evolution Section |\n" +
                "| 8: Print Enemy Dashboard  |\n" +
                "+---------------------------+\n" +
                "\n");
        System.out.print(Constants.ANSI_CYAN + "Choose your action: " + Constants.ANSI_RESET);
    }
    public void printLockBox(){
        SerializableLockBox lockBox =  clientSocket.getView().getDashboard().getSerializableLockBox();
        LockBoxPrinter.print(lockBox);

    }
    public void printStock(){
        SerializableStock stock = clientSocket.getView().getDashboard().getSerializableStock();
        StockPrinter.print(stock);
    }
    public void printMarket(){
        SerializableMarket market = clientSocket.getView().getMarket();
        MarketPrinter.print(market);
    }

    public void printPopeTrack(){
        SerializablePopeTack popeTack = clientSocket.getView().getDashboard().getSerializablePopeTack();
        boolean atLeastOneCard = false;
        System.out.println("Pope track: ");
        System.out.println("your current position is: " + popeTack.getPosition());
        System.out.println("Your active pope card are: ");
        for(int i = 0 ; i < popeTack.getActiveCards().length ; i++){
            if(popeTack.getActiveCards()[i] == true){
                System.out.println("    " + i);
                atLeastOneCard = true;
            }
        }
        if(!atLeastOneCard)
            System.out.println("    none");
        if(popeTack.getLorenzoPosition() > 0)
            System.out.println("Position of Lorenzo is: " + popeTack.getLorenzoPosition());
        System.out.println("#################################################################");
    }
    public void printLeaderCards(){
        ArrayList<SerializableLeaderCard> leaderCards = clientSocket.getView().getLeaderCards();
        LeaderCardsPrinter.print(leaderCards);

    }
    public void printProductionZones(){
        ProductionSectionPrinter.print(clientSocket.getView().getDashboard());
        /*
        SerializableProductionZone[] productionZones = clientSocket.getView().getDashboard().getSerializableProductionZones();
        System.out.println("production zones: ");
        for(int i = 0 ; i < productionZones.length ; i++) {
            System.out.println("production zone " + i + ": ");
            SerializableProductionZone productionZone = productionZones[i];
            ArrayList<EvolutionCard> evolutionCards = productionZone.getCards();
            if (evolutionCards != null) {
                for (int j = 0; j < evolutionCards.size(); j++) {
                    EvolutionCard evolutionCard = evolutionCards.get(j);
                    if (evolutionCard == null) {
                        if (i == 0)
                            System.out.println("    Empty");
                        continue;
                    }
                    System.out.println("Card in position " + j + ": ");
                    printEvolutionCard(evolutionCard);
                    System.out.println("#################################################################");
                }
            }
            else {
                System.out.println("    none");
                System.out.println("#################################################################");
            }
        }

         */
    }
    public void printEvolutionSection(){
        SerializableEvolutionSection evolutionSection = clientSocket.getView().getEvolutionSection();
        EvolutionSectionPrinter.print(evolutionSection);
    }
    private void printEvolutionCard(EvolutionCard evolutionCard){
        System.out.println("Color: " + evolutionCard.getColor());
        System.out.println("Level: " + evolutionCard.getLevel());
        System.out.println("Point: " + evolutionCard.getPoint());
        System.out.println("Is active: " + evolutionCard.isActive());
        System.out.println("Cost: ");
        for(Resource resource : evolutionCard.getCost().keySet()){
            System.out.println("    Resource: " + resource + " , quantity: " + evolutionCard.getCost().get(resource));
        }
        System.out.println("Requires: ");
        for(Resource resource : evolutionCard.getRequires().keySet()){
            System.out.println("    Resource: " + resource + " , quantity: " + evolutionCard.getRequires().get(resource));
        }
        System.out.println("Products: ");
        for(Resource resource : evolutionCard.getProduction().keySet()){
            System.out.println("    Resource: " + resource + " , quantity: " + evolutionCard.getProduction().get(resource));
        }
    }
    public void printSetOfLeaderCard(ArrayList<SerializableLeaderCard> leaderCards){

        LeaderCardsPrinter.print(leaderCards);
        /*
        System.out.println("#################################################################");
        System.out.println("Leader card id: " + leaderCard.getId());
        System.out.println("Required color: ");
        if(leaderCard.getRequiresColor() != null) {
            for (int j = 0; j < leaderCard.getRequiresColor().length; j++) {
                System.out.println("    " + leaderCard.getRequiresColor()[j]);
            }
        }
        else {
            System.out.println("    none");
        }
        System.out.println("Required level: ");
        if(leaderCard.getRequiresLevel() != null) {
            for (int j = 0; j < leaderCard.getRequiresLevel().length; j++) {
                System.out.println("    " + leaderCard.getRequiresLevel()[j]);
            }
        }
        else {
            System.out.println("    none");
        }
        System.out.println("Require for activation: " + leaderCard.getRequiresForActiveLeaderCards());
        System.out.println("Ability: " + leaderCard.getAbilityType());
        System.out.println("point: " + leaderCard.getPoint());
        System.out.println("Is active: " + leaderCard.isActive());
        System.out.println("Is used: " + leaderCard.isUsed());
        System.out.println("Requires: ");
        if(leaderCard.getRequires() != null) {
            for (Resource resource : leaderCard.getRequires().keySet()) {
                System.out.println("    Resource: " + resource + " , quantity: " + leaderCard.getRequires().get(resource));
            }
        }
        else{
            System.out.println("    none");
        }
        System.out.println("Ability resources: ");
        for(Resource resource : leaderCard.getAbilityResource().keySet()){
            System.out.println("    Resource: " + resource + " , quantity: " + leaderCard.getAbilityResource().get(resource));
        }
        System.out.println("#################################################################");

         */
    }



    public void setIsAckArrived(boolean value){
        isAckArrived = value;
    }

    public void setIsNackArrived(boolean value){
        isNackArrived = value;
    }

    public void setLeaderCards(ArrayList<SerializableLeaderCard> leaderCards){ this.leaderCards = leaderCards; }

    public void setResources(ArrayList<Resource> resources){ this.resources = resources; }

    public void setGamePhase(Phase gamePhase){
        this.gamePhase = gamePhase;
    }

    public ClientSocket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public boolean isAckArrived() {
        return isAckArrived;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public ArrayList<SerializableLeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void setServerIsUp(boolean serverIsUp) {
        this.serverIsUp = serverIsUp;
    }

    public boolean isActionBeenDone() {
        return isActionBeenDone;
    }

    public void setActionBeenDone(boolean actionBeenDone) {
        isActionBeenDone = actionBeenDone;
    }

    public Phase getGamePhase() {
        return gamePhase;
    }

    public static void main(String[] args){
        new CLI();
    }


    @Override
    public void run() {
        gamePhase.makeAction(this);
    }


    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
