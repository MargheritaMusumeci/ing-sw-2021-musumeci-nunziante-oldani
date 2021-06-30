package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.PingMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class with the sole purpose of sending a ping message to the client every 10s
 */
public class PingSender implements Runnable{

    private ServerClientConnection scc;
    private boolean isActive;

    public PingSender(ServerClientConnection scc){
        this.scc = scc;
        isActive = true;
    }

    /**
     * method that sends a ping every 20 second and wait for a response. If the answer does not come mark the
     * server client connection as disconnected
     */
    @Override
    public void run() {
        while(isActive){

            //send ping message
            scc.send(new PingMessage("Ping"));
            isActive = false;

            try {
                Thread.sleep(1000*20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Socket has not responded to ping and have disconnected
        if(scc.isActive()){
            scc.disconnect();
        }

        synchronized (scc.getHasDisconnectionBeenCalled()){
            scc.setHasDisconnectionBeenCalled(true);
            scc.disconnect();
        }

        System.out.println(scc.getNickname() + ": ping disconnection");
    }

    /**
     * method called when the ping message is received
     */
    public void pingReceived() {
        isActive = true;
    }

    public void setActive(boolean value){
        isActive = value;
    }
}
