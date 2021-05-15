package it.polimi.ingsw.client;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.PingMessage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket implements Runnable{

    private CLI cli;
    private GUI gui;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private MessageHandler messageHandler;
    boolean isActive;
    private View view;

    public ClientSocket(CLI cli, Socket socket) throws IOException {
        this.cli = cli;
        this.gui=null;
        this.socket = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        messageHandler = new MessageHandlerCLI(cli, this);
        isActive = true;

    }

    //just for testing GUI --> we need to distinguish between network interface and graphic interface

    public ClientSocket(GUI gui, Socket socket) throws IOException {
        this.cli = null;
        this.gui=gui;
        this.socket = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        messageHandler = new MessageHandlerGUI(gui, this);
        isActive = true;
    }

    /**
     * method that send the message to the server
     * @param message is the message that has to be sent
     */
    public synchronized void send(Message message){
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    public void close(){
        //Network error, handle this
        isActive = false;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
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
                e.printStackTrace();
                System.out.println("Connection ended, retry later");
                return;
            } catch (ClassNotFoundException e) {
                System.out.println("Server corrupted, retry later");
                return;
            }

        }
    }
}
