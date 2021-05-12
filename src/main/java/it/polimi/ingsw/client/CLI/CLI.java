package it.polimi.ingsw.client.CLI;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.configurationMessages.*;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;

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

    public CLI(){
        scanner = new Scanner(System.in);
        isNackArrived = false;
        isAckArrived = false;
        leaderCards = null;
        resources = null;
        gamePhase = GamePhases.IINITIALIZATION;
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

    public void setIsAckArrived(boolean value){
        isAckArrived = value;
    }

    public void setIsNackArrived(boolean value){
        isNackArrived = value;
    }

    public void setLeaderCards(ArrayList<SerializableLeaderCard> leaderCards){ this.leaderCards = leaderCards; }

    public void setResources(ArrayList<Resource> resources){ this.resources = resources; }

    public void setGamePhase(GamePhases gamePahse){
        this.gamePhase = gamePahse;
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
                    System.out.println("wait che implemento anche questa cosa");
                    try {
                        synchronized (this){
                            wait();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case MYTURN:
                    System.out.println("è il tuo turno");
                    while (true){ }
                case OTHERPLAYERSTURN:
                    System.out.println("non tocca a te!");
                    while (true){ }

            }
        }

    }
}
