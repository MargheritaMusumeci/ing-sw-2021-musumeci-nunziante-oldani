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

    @Override
    public void run() {
        while(isActive){

            //mando messaggio di ping
            scc.send(new PingMessage("Ping"));
            isActive = false;

            try {
                Thread.sleep(1000*10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Socket disconnesso, non devo più mandare ping e devo fare tutte le cose di quando un client si disconnette
        scc.disconnect();
        System.out.println(scc.getNickname() + ": disconnesso nel ping");
    }

    public void pingRecived() {
        isActive = true;
    }
}
