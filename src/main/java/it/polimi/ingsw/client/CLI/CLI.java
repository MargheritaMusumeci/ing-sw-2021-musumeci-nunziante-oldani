package it.polimi.ingsw.client.CLI;

import com.google.gson.stream.JsonToken;
import com.sun.jdi.ArrayReference;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.EndTurnMessage;
import it.polimi.ingsw.messages.actionMessages.*;
import it.polimi.ingsw.messages.configurationMessages.*;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.popeTrack.PopeTrack;
import it.polimi.ingsw.serializableModel.*;
import it.polimi.ingsw.utils.Constants;

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

        printTitle();

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
        gamePhase = GamePhases.WAITINGOTHERPLAYERS;

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

    private void printTitle(){
        System.out.println(Constants.ANSI_RED + "\n" +
                "                     _                               \n" +
                " _ __ ___   __ _ ___| |_ ___ _ __                    \n" +
                "| '_ ` _ \\ / _` / __| __/ _ \\ '__|                   \n" +
                "| | | | | | (_| \\__ \\ ||  __/ |                      \n" +
                "|_| |_| |_|\\__,_|___/\\__\\___|_|                      \n" +
                "                                                     \n" +
                "        __                                           \n" +
                "  ___  / _|                                          \n" +
                " / _ \\| |_                                           \n" +
                "| (_) |  _|                                          \n" +
                " \\___/|_|                                            \n" +
                "                                                     \n" +
                "                      _                              \n" +
                " _ __ ___ _ __   __ _(_)___ ___  __ _ _ __   ___ ___ \n" +
                "| '__/ _ \\ '_ \\ / _` | / __/ __|/ _` | '_ \\ / __/ _ \\\n" +
                "| | |  __/ | | | (_| | \\__ \\__ \\ (_| | | | | (_|  __/\n" +
                "|_|  \\___|_| |_|\\__,_|_|___/___/\\__,_|_| |_|\\___\\___|\n" +
                "                                                     \n" + Constants.ANSI_RESET);
    }
    private void printMenu(){
        System.out.println("+--------------------------+\r\n|     " +
                "Possible action:     |\r\n+==========================+\r\n| " +
                "0: END TURN              |\r\n| 1: Show leaderCards      |\r\n| " +
                "2: Show stock            |\r\n| 3: Show lockBox          |\r\n| " +
                "4: Show popeTrack        |\r\n| 5: Show productionZones  |\r\n| " +
                "6: Show market           |\r\n| 7: Show evolutionSection |\r\n| " +
                "8: Active Leader cards   |\r\n| 9: Discard Leader cards  |\r\n| " +
                "10: Buy from Market      |\r\n| 11: Active Production    |\r\n| " +
                "12: Buy Evolution card   |\r\n+--------------------------+\r\n\r\n");
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

        boolean control;
        control = false;
        int number;
        do{
            System.out.println("Choose the leader card to be activated (type the id): ");
            number = scanner.nextInt();

            for(SerializableLeaderCard lCard : clientSocket.getView().getLeaderCards()){
                if(lCard.getId() == number && !lCard.isActive()){
                    control = true;
                }
            }
        }while(!control);

        //trovo la posizione a cui si trova la leader card nel mio set
        int pos = 0;
        for (int i=0; i<clientSocket.getView().getLeaderCards().size(); i++){
            if(clientSocket.getView().getLeaderCards().get(i).getId() == number){
                pos = i;
            }
        }

        //devo mandare il messsaggio di attivazione
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

        //chiedo le risorse
        clientSocket.send(new RequestResourcesBoughtFromMarketMessage(""));

        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //controllo se tra le risorse ottunute ho solo nothing, in quel caso mando un messaggio e termino
        int countNotnull = 0;
        for (Resource resource: clientSocket.getView().getResourcesBoughtFromMarker()){
           if(resource != Resource.NOTHING){
               countNotnull++;
           }
        }

        //erano tutto nulle
        if(countNotnull == 0){
            clientSocket.send(new StoreResourcesMessage("salva risorse", clientSocket.getView().getResourcesBoughtFromMarker()));
            return;
        }


        do{
            //stampo le risorse ottenute
            int i =0;
            System.out.println("seleziona le risorse da inserire nello stock, -1 per terminare e 5 per sceglierle tutte");
            for (Resource resource: clientSocket.getView().getResourcesBoughtFromMarker()){
                System.out.println(i + ") "+resource);
                i++;
            }
            ArrayList<Integer> positions = new ArrayList<>();
            int positionSelected;

            do{
                positionSelected = scanner.nextInt();
                if(positionSelected >= 0 &&
                        positionSelected < clientSocket.getView().getResourcesBoughtFromMarker().size() &&
                        !positions.contains(positionSelected)){
                    System.out.println("prova if");
                    positions.add(positionSelected);
                }else{
                    if(positionSelected == 5){
                        positions = new ArrayList<>();
                        for (int j=0; j<clientSocket.getView().getResourcesBoughtFromMarker().size(); j++){
                            positions.add(j);
                        }
                        break;
                    }
                }
            }while(positionSelected != -1);

            ArrayList<Resource> resourcesToSend = new ArrayList<>();
            for(Integer integer: positions){
                resourcesToSend.add(clientSocket.getView().getResourcesBoughtFromMarker().get(integer));
            }
            clientSocket.send(new StoreResourcesMessage("salva risorse selezionate", resourcesToSend));

            synchronized (this){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }while(!isAckArrived);

        isAckArrived = false;
        isNackArrived = false;

    }

    private void buyEvolutionCard(){
        printEvolutionSection();

        int row, col, pos;

        do{
            System.out.println("Inserisci la riga e la colonna della carta da comprare");
            System.out.print("riga > ");
            row = scanner.nextInt();
            System.out.print("colonna > ");
            col = scanner.nextInt();
            System.out.println("inserisci in quale production zone inserire la carta: (0,1,2)");
            System.out.print("> ");
            pos = scanner.nextInt();
        }while ((row<0 || row > 2) || (col <0 || col>3) || (pos != 0 && pos != 1 && pos != 2));

        clientSocket.send(new BuyEvolutionCardMessage("buy evolution card", row, col, pos));

        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(isAckArrived){
            System.out.println("Carta inserita correttamente");
            isAckArrived = false;
            isActionBeenDone = true;
        }else{
            System.out.println("Errore durante l'acquisto della carta dalla evolution section");
            isNackArrived = false;
        }

    }

    private void discardLeaderCard(){

        if(clientSocket.getView().getLeaderCards().size() == 0){
            System.out.println("Non hai leader card!");
            return;
        }

        //controllo che ci siano ancora carte non attivate
        boolean check = false;
        for (SerializableLeaderCard serializableLeaderCard : clientSocket.getView().getLeaderCards()){
            if(!serializableLeaderCard.isActive()){
                check = true;
            }
        }
        if (!check){
            //non ho carte non attive
            System.out.println("Tutte le tue carte sono attivate, non puoi scartarle");
            return;
        }

        //stampo le carte scartabili
        for (SerializableLeaderCard serializableLeaderCard : clientSocket.getView().getLeaderCards()){
            if(!serializableLeaderCard.isActive()){
                printSingleLeaderCard(serializableLeaderCard);
            }
        }

        boolean controllo;
        controllo = false;
        int number;
        do{
            System.out.println("Choose the leader card to be discard (type the id): ");
            number = scanner.nextInt();

            for(SerializableLeaderCard lCard : clientSocket.getView().getLeaderCards()){
                if(lCard.getId() == number && !lCard.isActive()){
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

        //devo mandare il messsaggio di scarto carta
        clientSocket.send(new DiscardLeaderCardMessage("discard leader card", pos));
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (isAckArrived){
            System.out.println("Leader card correctly discarded");
        }else{
            System.out.println("Error while discarding the leader card");
        }

        isAckArrived = false;
        isNackArrived = false;
    }

    private void activeProductionZones(){
        int position = 0;
        boolean activeBasic = false;
        boolean exit = false;
        ArrayList<Integer> productionZones =new ArrayList<Integer>();
        ArrayList<Resource> resourcesRequires = new ArrayList<Resource>();
        ArrayList<Resource> resourcesEnsures = new ArrayList<Resource>();

        printProductionZones();

        System.out.println("Which production zone do you want to activate? \n" +
                "You can choose between 0 , 1 and 2 \n" +
                "Insert -1 to end");
        do{
            //Insert the position
            do{
                position = scanner.nextInt();
                if(position < -1 || position > 2)
                    System.out.println("Position not valid, insert an other position");
            }while(position < -1 || position > 2);

            //If the player ended his choice
            if(position == -1){
                exit = true;
                break;
            }

            //Add the production if the position is valid
            if(!productionZones.contains(position))
                productionZones.add(position);
            else
                System.out.println("Position already chose");

        }while(!exit && productionZones.size() <= 3);

        //Now the array with the position is ready

        System.out.println("Do you want to activate the basic production zone? Y/N");

        do{
            exit = true;
            String input = scanner.next();
            if(input.equals("Y")){
                activeBasic = true;
            }
            else if(input.equals("N")){
                activeBasic = false;
            }
            else{
                System.out.println("Wrong parameter, try again");
                exit = false;
            }
        }while(!exit);

        //If the player wants to active the basic production
        if(activeBasic){
            System.out.println("Choose 2 resource types to use in basic production:");
            System.out.println("1) COIN");
            System.out.println("2) ROCK");
            System.out.println("3) SHIELD");
            System.out.println("4) SERVANT");

            while(resourcesRequires.size() < 2){
                fillArrayList(resourcesRequires);
            }

            System.out.println("Choose 1 resource type to obtain from basic production:");
            System.out.println("1) COIN");
            System.out.println("2) ROCK");
            System.out.println("3) SHIELD");
            System.out.println("4) SERVANT");

            while(resourcesEnsures.size() < 1){
                fillArrayList(resourcesEnsures);
            }
        }

        //Send activeProductionMessage to the server
        clientSocket.send(new ActiveProductionMessage("Active production zones" , productionZones ,
                activeBasic , resourcesRequires , resourcesEnsures));

        //Wait for a response
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (isAckArrived){
            isActionBeenDone = true;
            System.out.println("Production zones activated");
        }else{
            System.out.println("Something goes wrong, try again");
        }

        isAckArrived = false;
        isNackArrived = false;
    }

    /**
     * Private method that fills the arrayList passed as parameter with a resource type accordin
     *      to the number specified by the user
     * @param resources is the arrayList where put the resource the user chose
     */
    private void fillArrayList(ArrayList<Resource> resources){
        int position = 0;
        position = scanner.nextInt();
        if(position < 1 || position > 4){
            System.out.println("Wrong resources , try again");
            return;
        }
        if(position == 1)
            resources.add(Resource.COIN);
        else if(position == 2)
            resources.add(Resource.ROCK);
        else if(position == 3)
            resources.add(Resource.SHIELD);
        else
            resources.add(Resource.SERVANT);
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
                    if(numberOfPlayers == 1){
                        gamePhase = GamePhases.MYTURN;
                    }else{
                        try {
                            synchronized (this){
                                wait();
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case MYTURN:

                    isActionBeenDone = false;
                    System.out.println("It's your turn");
                    boolean endTurnSelected = false;
                    while(!endTurnSelected) {
                        printMenu();

                        int action = scanner.nextInt();

                        switch (action) {
                            case 0:
                                if(!isActionBeenDone){
                                    System.out.println("Cannot end turn without do an action");
                                }
                                else{
                                    endTurnSelected = true;
                                    clientSocket.send(new EndTurnMessage("Turn ended"));
                                }
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
                                discardLeaderCard();
                                break;
                            case 10:
                                if(!isActionBeenDone){
                                    buyFromMarket();
                                }else{
                                    System.out.println("You have already make an action, yuo should end your turn now!");
                                }
                                break;
                            case 11:
                                if(!isActionBeenDone){
                                    activeProductionZones();
                                }else {
                                    System.out.println("You have already make an action, yuo should end your turn now!");
                                }
                                break;
                            case 12:
                                if (!isActionBeenDone){
                                    buyEvolutionCard();
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
                    synchronized (this){
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

            }
        }

    }

}
