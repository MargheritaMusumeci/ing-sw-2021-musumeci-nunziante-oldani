package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class binds the client socket with the server socket
 */
public class SocketServer implements Runnable{

    private int port;
    private Server server;
    private ExecutorService executorService;
    private boolean isActive;

    public SocketServer(Server server) {
        this.server = server;
        this.port = server.getPort();
        executorService = Executors.newCachedThreadPool();
        isActive = true;
    }

    public void setActive(boolean value){
        isActive = value;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(Constants.ANSI_CYAN + "Server Socket listening at port: " + Constants.ANSI_RESET +
                Constants.ANSI_GREEN +  port +  Constants.ANSI_RESET +
                Constants.ANSI_CYAN +". \nType " + Constants.ANSI_RESET +
                Constants.ANSI_GREEN + "\"quit\" " +  Constants.ANSI_RESET +
                Constants.ANSI_CYAN + "to exit" + Constants.ANSI_RESET);

        while (isActive){
            Socket s = null;
            try {
                s = serverSocket.accept();
                s.setSoTimeout(20*1000);

            } catch (IOException e) {
                System.err.println("Error while accepting the socket, server still listening");
                continue;
            }

            System.out.println(Constants.ANSI_GREEN + "New client detected: " + Constants.ANSI_RESET +
                    Constants.ANSI_PURPLE + s + Constants.ANSI_RESET);

            ServerClientConnection scc = null;
            try {
                scc = new ServerClientConnection(this.server, s);
                server.getQueue().add(scc);
                executorService.submit(scc);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error while getting output and input streams, socket discharged but server.");
                System.err.println("Server still listening");
                continue;
            }

        }

    }
}
