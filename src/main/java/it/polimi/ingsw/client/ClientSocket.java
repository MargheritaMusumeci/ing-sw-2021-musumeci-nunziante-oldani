package it.polimi.ingsw.client;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.PingMessage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket implements Runnable{

    private CLI cli;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private MessageHandler messageHandler;
    boolean isActive;

    public ClientSocket(CLI cli, Socket socket) throws IOException {
        this.cli = cli;
        this.socket = socket;

        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        messageHandler = new MessageHandler(cli, this);

        isActive = true;

    }

    /**
     * method that send the message to the server
     * @param message is the message that has to be sent
     */
    public void send(Message message){
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            close();
        }
    }

    public void close(){
        //Network error, handle this
        isActive = false;
    }

    /**
     * method in which messaged from the server are received
     */
    @Override
    public void run() {
        while(isActive){
            //leggo i messaggi in arrivo e li eseguo
            try {
                Message input = (Message) inputStream.readObject();
                if(! (input instanceof PingMessage))
                    System.out.println("Messaggio letto: " + input.getMessage());
                messageHandler.handleMessage(input);
            } catch (EOFException e){
                System.out.println("Server disconnected, retry later");
                return;
            } catch (IOException e) {
                System.out.println("Connection ended, retry later");
                return;
            } catch (ClassNotFoundException e) {
                System.out.println("Server corrupted, retry later");
                return;
            }

        }
    }
}
