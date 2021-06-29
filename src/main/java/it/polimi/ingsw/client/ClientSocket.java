package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.messageHandler.MessageHandler;
import it.polimi.ingsw.client.messageHandler.MessageHandlerCLI;
import it.polimi.ingsw.client.messageHandler.MessageHandlerGUI;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.PingMessage;
import it.polimi.ingsw.messages.sentByServer.ServerMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * class that handles the connection between the client and the server
 */
public class ClientSocket implements Runnable{

    private final GUI gui;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final MessageHandler messageHandler;
    boolean isActive;
    private View view;

    /**
     * class constructor for the cli client that extract the streams from the socket and instantiates the
     * correct message handler
     * @param cli is the client's cli
     * @param socket is the socket connection established with the server
     */
    public ClientSocket(CLI cli, Socket socket) throws IOException {
        this.gui=null;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        messageHandler = new MessageHandlerCLI(cli, this);
        isActive = true;
    }

    /**
     * class constructor for the gui client that extract the streams from the socket and instantiates the
     * correct message handler
     * @param gui is the client's gui
     * @param socket is the socket connection established with the server
     */
    public ClientSocket(GUI gui, Socket socket) throws IOException {
        this.gui=gui;
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
            isActive = false;
        }
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
            //read messages
            try {
                Message input = (Message) inputStream.readObject();
                if(! (input instanceof PingMessage)){
                    ((ServerMessage) input).handle(messageHandler);
                }else{
                    send(new PingMessage("Ping response"));
                }
            } catch (IOException | ClassNotFoundException e){
                if(gui!= null){
                    gui.setErrorFromServer("Server disconnected, please close the application and retry later. Your game has been saved");
                    gui.changeScene();
                }
                System.out.println("Server disconnected, please close the application and retry later. Your game has been saved");
                return;
            }
        }
    }
}
