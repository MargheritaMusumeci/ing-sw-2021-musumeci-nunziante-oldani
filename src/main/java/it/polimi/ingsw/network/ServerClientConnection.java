package it.polimi.ingsw.network;

import it.polimi.ingsw.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that handles the communication between the server and the client
 */
public class ServerClientConnection implements Runnable{

    private Socket socket;
    private String nickname;
    private Server server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private boolean isActive = false;
    private PingSender ps;
    private ExecutorService executorService;


    public ServerClientConnection(Server server, Socket socket) throws IOException{
        this.server = server;
        this.socket = socket;
        isActive = true;
        executorService = Executors.newCachedThreadPool();


        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        isActive = true;
        ps = new PingSender(outputStream, inputStream);

    }

    public void send(Message message) {
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            close();
        }
    }

    public void close(){
        //method that delete the socket and if the socket was in a game calls the method to save the satus in order to accept a reconnection
    }

    @Override
    public void run() {

        try{
            new Thread(ps).start();
            while (isActive){
                System.out.println("inizio con il scc");
                //leggo i messaggi in arrivo e li eseguo
                Message input = (Message) inputStream.readObject();
                System.out.println("Messaggio letto");
                //messageHandler(input);
               // System.out.println(input.getAuthor() + input.getMessage());
            }
        }catch (IOException e){

        }catch (ClassNotFoundException e){

        }
    }
}
