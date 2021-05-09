package it.polimi.ingsw.client;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.messages.Message;

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
        messageHandler = new MessageHandler(cli);

        isActive = true;

    }

    /**
     * method that send the message to the server
     * @param message is the message that has to be sent
     */
    public void send(Message message){
        try {
            System.err.println("sto per mandare il messaggio");
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
            System.err.println("messaggio mandato");
        } catch (IOException e) {
            close();
        }
    }

    public void close(){
        //Network error, handle this
        isActive = false;
    }


    @Override
    public void run() {
        while(isActive){
            //leggo i messaggi in arrivo e li eseguo
            try {
                Message input = (Message) inputStream.readObject();
                System.err.println("Messaggio letto: " + input.getMessage());
                messageHandler.handleMessage(input);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
