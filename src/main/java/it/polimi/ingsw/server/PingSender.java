package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class with the sole purpose of sending a ping message to the client every 10s
 */
public class PingSender implements Runnable{

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    //private Message pingMessage;
    private boolean isActive;

    public PingSender(ObjectOutputStream out, ObjectInputStream in){
        this.out = out;
        this.in = in;

        //pingMessage = new Message(MessageType.PING, null, null);
        isActive = true;
    }

    @Override
    public void run() {
        while(isActive){
            //mando messaggio di ping
            try {

                out.reset();
                //out.writeObject(pingMessage);
                out.flush();
                //System.out.println("ping mandato dal server");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000*10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
