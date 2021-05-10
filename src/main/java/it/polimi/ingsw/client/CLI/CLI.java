package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.MessageHandler;
import it.polimi.ingsw.messages.configurationMessages.*;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class CLI implements Runnable {

    private Scanner scanner;
    private ClientSocket clientSocket;
    private MessageHandler messageHandler;
    private boolean isAckArrived;
    private boolean isNackArrived;
    private String nickname;
    private int numberOfPlayers;
    private boolean isGameStarted;
    private Socket socket;
    private ArrayList<SerializableLeaderCard> leaderCards;
    private  ArrayList<Resource> resources;


    public CLI(){
        scanner = new Scanner(System.in);
        isNackArrived = false;
        isAckArrived = false;
        isGameStarted = false;
        leaderCards = null;
        resources = null;

        new Thread(this).start();

    }

    /**
     * method that wait until the game is going to start and the flag "isGameStarted" is set to TRUE
     */
    private void waitOtherPlayers() throws InterruptedException {

        System.out.println("Wait other players to reach the game! Have fun and avoid cheating");
        while (!isGameStarted){
            Thread.sleep(1000);
        }

    }

    /**
     * method that checks if an ack or nack is arrived from the server
     * @return true if ack is recived and false is nack is recived
     */
    private boolean waitForAck(){

        while(!isAckArrived && !isNackArrived){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.err.println("esco dal for del wait");
        if(isAckArrived){
            isAckArrived = false;
            return true;
        }else{
            isNackArrived = false;
            return false;
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

    private String nicknameInitialization() throws InterruptedException {
        String nic;

        System.out.print("Enter your nickname: ");
        nic = scanner.next();
        clientSocket.send(new NickNameMessage(nic));

        if(waitForAck()){
            isNackArrived = false;
            return nic;
        }else{
            isNackArrived = false;
            return null;
        }
    }


    public void setIsAckArrived(boolean value){
        isAckArrived = value;
    }

    public boolean isAckArrived() {
        return isAckArrived;
    }

    public void setIsNackArrived(boolean value){
        isNackArrived = value;
    }

    public boolean isNackArrived() {
        return isNackArrived;
    }

    public void setIsGameStarted(boolean value){
        isGameStarted = value;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setLeaderCards(ArrayList<SerializableLeaderCard> leaderCards){ this.leaderCards = leaderCards; }

    public void setResources(ArrayList<Resource> resources){ this.resources = resources; }

    private void waitForLeaderCards(){
        while(leaderCards == null){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void chooseLeaderCards(){
        clientSocket.send(new RequestLeaderCardMessage("Request leader card"));
        int index = 0;
        ArrayList<Integer> lCards = new ArrayList<Integer>();

        waitForLeaderCards();

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
    }

    private void waitForResources(){
        while(resources == null){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void chooseInitialResources(){
        int index;

        waitForResources();

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
        }
    }

    public static void main(String[] args){

        new CLI();

    }

    @Override
    public void run() {

        //creation of the clineSocket
        connectionInitialization();
        if(clientSocket == null){
            return;
        }
        new Thread(clientSocket).start();

        //setting of the nickname
        do{
            try {
                nickname = nicknameInitialization();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(nickname==null){
                System.out.println("Your nickname has already been chosen");
            }
        }while(nickname==null);

        //setting the number of players
        numberOfPlayersInitialization();

        //wait other player to join
        try {
            waitOtherPlayers();
        } catch (InterruptedException e) {
            // make the player restart the cli because of a disconnection
            e.printStackTrace();
        }

        do{
            chooseLeaderCards();
        }while(!waitForAck());
        System.out.println("Ack aggiunte bello");

        do{
            chooseInitialResources();
        }while(!waitForAck());

        System.out.println("Ci siamoooooooooooooooooooooooooooooooooooooooo");

    }
}
