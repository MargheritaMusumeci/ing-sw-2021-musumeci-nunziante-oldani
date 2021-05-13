package it.polimi.ingsw.client.CLI;

import com.google.gson.stream.JsonToken;
import com.sun.jdi.ArrayReference;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.actionMessages.ActiveLeaderCardMessage;
import it.polimi.ingsw.messages.actionMessages.BuyFromMarketMessage;
import it.polimi.ingsw.messages.configurationMessages.*;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.popeTrack.PopeTrack;
import it.polimi.ingsw.serializableModel.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class CLI implements Runnable {

    private Scanner scanner;
    private ClientSocket clientSocket;
    private GamePhases gamePhase;
    private boolean isAckArrived;
    private boolean isNackArrived;
    private String nickname;
    private int numberOfPlayers;
    private Socket socket;
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
        gamePhase = GamePhases.IINITIALIZATION;
        isActionBeenDone = false;
        new Thread(this).start();
    }


    /**
     * method that ask for ip and port of the server and creates the socket between client and server
     */
    private void connectionInitialization(){

        int port;
        String address;

        System.out.println("CLI | Master of Renaissance");
        System.out.println("Wilkommen");

        System.out.print("Enter the ip address of the Server: ");
        address = scanner.next();

        System.out.print("Enter the port: ");
        port = scanner.nextInt();

        while(port < 1025 || port > 65535){
            System.out.println("Invalid port number. Pick a porta in range 1025-65535");
            System.out.print("Enter the port: ");
            port = scanner.nextInt();
        }

        try {
            socket = new Socket(address, port);
            System.out.println("test se qui arrivo");
            clientSocket = new ClientSocket(this, socket);
        } catch (IOException e) {
            clientSocket = null;
            System.out.println("There was a problem with the server. Please chek if the ip address and port number" +
                    "are correct and if the server is up and running ");
            //probabilemte qui c'è un errore con il server (o con i dati inseriti ip/port)
        }

    }
    private void nicknameInitialization(){
        String nic;

        System.out.print("Enter your nickname: ");
        nic = scanner.next();
        clientSocket.send(new NickNameMessage(nic));

        try {
            synchronized (this){
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IllegalMonitorStateException e){
            e.printStackTrace();
        }

        if(isAckArrived){
            nickname = nic;
            gamePhase = GamePhases.NUMBEROFPLAYERS;
            isAckArrived = false;
        }else{
            isNackArrived = false;
            System.err.println("The nickname that you have chose is already in use, please pick a different nickname");
        }

    }
    private void numberOfPlayersInitialization() {
        System.out.print("Enter number of players (1 to 4): ");
        numberOfPlayers = scanner.nextInt();

        while(numberOfPlayers < 1 || numberOfPlayers > 4){
            System.out.println("Error! Invalid number of player");
            System.out.print("Enter number of players (1 to 4): ");
            numberOfPlayers = scanner.nextInt();
        }

        clientSocket.send(new NumberOfPlayerMessage(String.valueOf(numberOfPlayers)));
        if(numberOfPlayers != 1){
            gamePhase = GamePhases.WAITINGOTHERPLAYERS;
        }else{
            //implementare partita single game
        }
    }
    public void chooseLeaderCards(){

        clientSocket.send(new RequestLeaderCardMessage("Request leader card"));

        while(leaderCards == null){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int index = 0;
        ArrayList<Integer> lCards = new ArrayList<Integer>();

        System.out.println("Chose 2 cards");
        System.out.println();
        for(int i = 0 ; i < leaderCards.size() ; i++){
            System.out.println("Card " + i + ": " + leaderCards.get(i).getRequiresForActiveLeaderCards() + " , " + leaderCards.get(i).getAbilityType() + "\n");
        }
        for(int i = 0; i < 2; i++){
            index = scanner.nextInt();
            if(index < leaderCards.size() && index >= 0){
                lCards.add(index);
            }
            else{
                i--;
                System.out.println("Carta scelta non valida");
            }
        }
        clientSocket.send(new LeaderCardChoiceMessage("Leader card scelte" , lCards));

        try {
            synchronized (this){
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IllegalMonitorStateException e){
            e.printStackTrace();
        }

        if(isAckArrived){
            gamePhase = GamePhases.INITIALRESOURCESELECTION;
            isAckArrived = false;
        }else{
            isNackArrived = false;
            System.err.println("Error while setting the initial leader card, retry");
        }


    }
    private void chooseInitialResources(){
        int index;

        while(resources == null){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(resources.size() == 4){
            for(int i = 0 ; i < 4 ; i++){
                System.out.println("Risorsa " + i + ": " + resources.get(i));
            }

            index = 0;
            do{
                System.out.println("Scegli una risorsa(tra 0 e 3)");
                index = scanner.nextInt();
            }while (index < 0 || index > 3);

            ArrayList<Resource> selected = new ArrayList<Resource>();
            selected.add(resources.get(index));
            clientSocket.send(new SelectedInitialResourceMessage("Resource chose" , selected));
        }
        else if(resources.size() == 8){
            for(int i = 0 ; i < 8 ; i++){
                System.out.println("Risorsa " + i + ": " + resources.get(i));
            }

            index = 0;
            int index2 = 0;
            do{
                System.out.println("Scegli 2 risorse(tra 0 e 7)");
                index = scanner.nextInt();
                index2 = scanner.nextInt();

            }while (index < 0 || index > 7 || index2 < 0 || index2 > 7 || index2 != index);

            ArrayList<Resource> selected = new ArrayList<Resource>();
            selected.add(resources.get(index));
            selected.add(resources.get(index2));
            clientSocket.send(new SelectedInitialResourceMessage("Resource chose" , selected));

        }else{
            gamePhase = GamePhases.STARTGAME;
            return;
        }

        try {
            synchronized (this){
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IllegalMonitorStateException e){
            e.printStackTrace();
        }

        if(isAckArrived){
            gamePhase = GamePhases.STARTGAME;
            isAckArrived = false;
        }else{
            isNackArrived = false;
            System.err.println("Error while setting the initial resources, retry");
        }
    }

    public void printLockBox(){
        SerializableLockBox lockBox =  clientSocket.getView().getDashboard().getSerializableLockBox();
        System.out.println("LockBox: ");
        for(Resource resource : lockBox.getResources().keySet()){
            System.out.println("Resource: " + resource + " , quantity: " + lockBox.getResources().get(resource));
        }
        System.out.println("#################################################################");
    }
    public void printStock(){
        SerializableStock stock = clientSocket.getView().getDashboard().getSerializableStock();
        int i = 0;
        System.out.println("Stock: ");
        for(i = 0 ; i < stock.getBoxes().size() ; i++){
            System.out.println("Box number " + i);
            for(int j = 0 ; j < stock.getBoxes().get(i).length ; j++){
                System.out.println(stock.getBoxes().get(i)[j]);
            }
            System.out.println("#################################################################");
        }
        if(stock.getBoxPlus() != null){
            for(int j = 0 ; j < stock.getBoxPlus().size() ; j++){
                System.out.println("Box number " + i+j);
                for(int k = 0 ; k < stock.getBoxPlus().get(i).length ; k++){
                    System.out.println(stock.getBoxes().get(j)[k]);
                }
                System.out.println("#################################################################");
            }
        }
    }
    public void printMarket(){
        SerializableMarket market = clientSocket.getView().getMarket();
        System.out.println("Market: ");
        System.out.println("External resource: " + market.getExternalResource());
        for(int i = 0 ; i < market.getMarket().length ; i++){
            System.out.println("Line:" + i);
            for(int j = 0 ; j < market.getMarket()[i].length ; j++){
                //System.out.println("Column j:");
                System.out.println(market.getMarket()[i][j]);
            }
        }
        System.out.println("#################################################################");
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
        System.out.println("Your leader card are: ");
        for(int i = 0 ; i < leaderCards.size() ; i++){
            SerializableLeaderCard leaderCard = leaderCards.get(i);
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
        }
    }
    //A problem because the attribute cards in SerializableProductionZone is null
    public void printProductionZones(){
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
    }
    public void printEvolutionSection(){
        SerializableEvolutionSection evolutionSection = clientSocket.getView().getEvolutionSection();
        System.out.println("Evolution section: ");
        for(int i = 0 ; i < evolutionSection.getEvolutionCards().length ; i++){
            for(int j = 0 ; j < evolutionSection.getEvolutionCards()[i].length ; j++){
                System.out.println("Card in position " + i + " " + j);
                EvolutionCard evolutionCard = evolutionSection.getEvolutionCards()[i][j];
                if(evolutionCard == null){
                    System.out.println("    Empty");
                    continue;
                }
                printEvolutionCard(evolutionCard);
                System.out.println("#################################################################");
            }

        }
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
    private void printSingleLeaderCard(SerializableLeaderCard leaderCard){
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
    }

    private void activeLeaderCards() {

        //check if there is a leader card to be activated
        int possibleLeaderCards = 0;
        for (SerializableLeaderCard leaderCard : clientSocket.getView().getLeaderCards()){
            if (!leaderCard.isActive()){
                possibleLeaderCards++;
            }
        }

        if(possibleLeaderCards == 0){
            System.out.println("You have already activated all your cards!");
            return;
        }

        for (SerializableLeaderCard leaderCard : clientSocket.getView().getLeaderCards()){
            if (!leaderCard.isActive()){
                printSingleLeaderCard(leaderCard);
            }
        }
        boolean controllo;
        controllo = false;
        int number;
        do{
            System.out.println("Choose the leader card to be activated (type the id): ");
            number = scanner.nextInt();



            for(SerializableLeaderCard lcard : clientSocket.getView().getLeaderCards()){
                if(lcard.getId() == number && !lcard.isActive()){
                    controllo = true;
                }
            }
        }while(!controllo);

        //trovo la posizione a cui si trova la leader card nel mio set
        int pos = 0;
        for (int i=0; i<clientSocket.getView().getLeaderCards().size(); i++){
            if(clientSocket.getView().getLeaderCards().get(i).getId() == number){
                pos = i;
            }
        }

        //devo mandare il messsaggio di attiazione
        clientSocket.send(new ActiveLeaderCardMessage("active leader card", pos));
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (isAckArrived){
            System.out.println("Leader card correctly activated");
        }else{
            System.out.println("Error while activating the leader card");
        }

        isAckArrived = false;
        isNackArrived = false;

    }
    private void buyFromMarket() {
        printMarket();
        int scelta = 0;
        do {
            System.out.println("Inserire 1 per scegliere una riga o 2 per scelgiere una colonna:");
            System.out.print(">");

            scelta = scanner.nextInt();

            if(scelta == 1){
                int row = 0;
                do{
                   System.out.println("Inserire la riga che si vuole acquistare: (0,1,2)");
                   row = scanner.nextInt();
                }while (row <0 || row > 2);

                clientSocket.send(new BuyFromMarketMessage("buy from market", row, true));

            }
            if(scelta ==2){
                int col = 0;
                do{
                    System.out.println("Inserire la colonna che si vuole acquistare: (0,1,2,3)");
                    col = scanner.nextInt();
                }while (col <0 || col > 3);

                clientSocket.send(new BuyFromMarketMessage("buy from market", col, false));
            }
        }while (scelta != 1 && scelta != 2);

        //ho acquistato, devo aspettare un ack
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(isAckArrived){
            System.out.println("Risosrse dal mercato prese in modo corretto");
            isAckArrived = false;
            isActionBeenDone = true;
        }else{
            System.out.println("Errore durante l'acquisto delle risorse");
            isNackArrived = false;
        }
    }



    public void setIsAckArrived(boolean value){
        isAckArrived = value;
    }

    public void setIsNackArrived(boolean value){
        isNackArrived = value;
    }

    public void setLeaderCards(ArrayList<SerializableLeaderCard> leaderCards){ this.leaderCards = leaderCards; }

    public void setResources(ArrayList<Resource> resources){ this.resources = resources; }

    public void setGamePhase(GamePhases gamePhase){
        this.gamePhase = gamePhase;
    }

    public GamePhases getGamePhase() {
        return gamePhase;
    }

    public static void main(String[] args){

        new CLI();

    }

    @Override
    public void run() {
        serverIsUp = true;

        while(serverIsUp){

            switch (gamePhase){
                case IINITIALIZATION:
                    connectionInitialization();
                    if(clientSocket == null){
                        serverIsUp = false;
                    }else{
                        gamePhase = GamePhases.NICKNAME;
                        new Thread(clientSocket).start();
                    }
                    break;
                case NICKNAME:
                    nicknameInitialization();
                    break;
                case NUMBEROFPLAYERS:
                    numberOfPlayersInitialization();
                    break;
                case WAITINGOTHERPLAYERS:
                    try {
                        synchronized (this){
                            wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case INITIALLEADERCARDSELECTION:
                    chooseLeaderCards();
                    break;
                case INITIALRESOURCESELECTION:
                    chooseInitialResources();
                    break;
                case STARTGAME:
                    System.out.println("The game is started");
                    try {
                        synchronized (this){
                            wait();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case MYTURN:
                    isActionBeenDone = false;
                    System.out.println("It's your turn");
                    boolean endTurnSelected = false;
                    while(!endTurnSelected) {
                        System.out.println("Possible action: ");
                        System.out.println("0: END TURN");
                        System.out.println("1: Show leaderCards");
                        System.out.println("2: Show stock");
                        System.out.println("3: Show lockBox");
                        System.out.println("4: Show popeTrack");
                        System.out.println("5: Show productionZones");
                        System.out.println("6: Show market");
                        System.out.println("7: Show evolutionSection");
                        System.out.println("8: Active Leader cards");
                        System.out.println("9: Discard Leader cards");
                        System.out.println("10: Buy from Market");
                        System.out.println("11: Active Production");
                        System.out.println("12: Byu Evolution card");
                        System.out.println("13: Manage stock");


                        int action = scanner.nextInt();

                        switch (action) {
                            case 0:
                                endTurnSelected = true;
                                //implementare messsaggio di endTurn
                                break;
                            case 1:
                                printLeaderCards();
                                break;
                            case 2:
                                printStock();
                                break;
                            case 3:
                                printLockBox();
                                break;
                            case 4:
                                printPopeTrack();
                                break;
                            case 5:
                                printProductionZones();
                                break;
                            case 6:
                                printMarket();
                                break;
                            case 7:
                                printEvolutionSection();
                                break;
                            case 8:
                                activeLeaderCards();
                                break;
                            case 9:
                                //to be implemented in the future
                                break;
                            case 10:
                                if(!isActionBeenDone){
                                    buyFromMarket();
                                }else{
                                    System.out.println("You have already make an action, yuo should end your turn now!");
                                }

                                break;


                            default:
                                System.out.println("This action doesn't exist");
                                break;
                        }
                    }


                case OTHERPLAYERSTURN:
                    System.out.println("non tocca a te!");
                    while (true){

                    }

            }
        }

    }




}
