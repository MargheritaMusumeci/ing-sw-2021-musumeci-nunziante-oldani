package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.MessageHandler;
import it.polimi.ingsw.messages.configurationMessages.NickNameMessage;
import it.polimi.ingsw.messages.configurationMessages.NumberOfPlayerMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class CLI {

    private Scanner scanner;
    private ClientSocket clientSocket;
    private MessageHandler messageHandler;
    private boolean isAckArrived;
    private boolean isNackArrived;
    private String nickname;
    private int numberOfPlayers;
    private boolean isGameStarted;


    public CLI(){
        scanner = new Scanner(System.in);
        messageHandler = new MessageHandler(this);
        isNackArrived = false;
        isAckArrived = false;
        isGameStarted = false;

        //creation of the clineSocket
        connectionInitialization();
        if(clientSocket == null){
            return;
        }
        new Thread(clientSocket).start();


        //setting of the nickname
        do{
            nickname =nicknameInitialization();
            if(nickname==null){
                System.out.println("Your nickname has already been chosen");
            }
        }while(nickname==null);

        //setting the number of players
        numberOfPlayersInitialization();

        //wait other player to join
        waitOtherPlayers();

    }

    /**
     * method that wait until the game is going to start and the flag "isGameStarted" is set to TRUE
     */
    private void waitOtherPlayers() {

        System.out.println("Wait other players to reach the game! Have fun and avoid cheating");
        while (!isGameStarted){}
    }

    private void numberOfPlayersInitialization() {
        System.out.print("Enter number of players (1 to 4): ");
        numberOfPlayers = scanner.nextInt();

        while(numberOfPlayers < 2 || numberOfPlayers > 4){
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
            Socket socket = new Socket(address, port);
            clientSocket = new ClientSocket(this, socket);
        } catch (IOException e) {
            e.printStackTrace();
            clientSocket = null;
            //probabilemte qui c'è un errore con il server (o con i dati inseriti ip/port)
        }

    }

    private String nicknameInitialization(){
        String nic;

        System.out.print("Enter your nickname: ");
        nic = scanner.next();
        clientSocket.send(new NickNameMessage(nic));
        if(waitForAck()){
            return nic;
        }else{
            return null;
        }
    }

    /**
     * method that checks if an ack or nack is arrived from the server
     * @return true if ack is recived and false is nack is recived
     */
    private boolean waitForAck(){
        while(!isAckArrived && !isNackArrived){

        }
        if(isAckArrived){
            isAckArrived = false;
            return true;
        }else{
            isNackArrived = false;
            return false;
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

    public static void main(String[] args){

        new CLI();

    }
}
