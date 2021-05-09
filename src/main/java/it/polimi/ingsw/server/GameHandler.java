package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.InitializationHandler;
import it.polimi.ingsw.controller.TurnHandler;
import it.polimi.ingsw.controller.TurnHandlerMultiPlayer;
import it.polimi.ingsw.controller.TurnHandlerSoloGame;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;
import it.polimi.ingsw.server.virtualView.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;

public class GameHandler implements Runnable{

    private int numberOfPlayers;
    private Game game; //the instance of the game
    private HashMap<HumanPlayer, VirtualView> playerSockets;
    private HashMap<ServerClientConnection, HumanPlayer> playersInGame;
    private InitializationHandler initializationHandler;

    private Player firstPlayer;

    private TurnHandler turnHandler;

    public GameHandler(int numberOfPlayers, ArrayList<ServerClientConnection> playerSockets){

        ArrayList<Player> playersForGame = new ArrayList<>();
        initializationHandler = new InitializationHandler();

        playersInGame = new HashMap<>();
        System.out.println("gioco da " + numberOfPlayers + "giocatori iniziato: ");

        boolean first = true;
        for (ServerClientConnection scc : playerSockets) {
            System.out.println(scc.getNickname());

            //creo un player per ogni scc
            HumanPlayer hPlayer = new HumanPlayer(scc.getNickname(), first);
            playersInGame.put(scc, hPlayer);
            playersForGame.add(hPlayer);
            if (first) firstPlayer=hPlayer;
            first = false;

            //metto a tutti gli scc il game handler di cui fanno parte
            scc.setGameHandler(this);
        }

        if(playersForGame.size() == 1){
            playersForGame.add(new LorenzoPlayer(playersForGame.get(0).getPopeTrack(), playersForGame.get(0).getDashboard()));
        }

        //creo il game
        game = new Game(playersForGame);

        this.numberOfPlayers = numberOfPlayers;


        if(numberOfPlayers == 1){
            turnHandler = new TurnHandlerSoloGame(game);
        }else{
            turnHandler = new TurnHandlerMultiPlayer(game);
        }

        new Thread(this).start();
    }

    public TurnHandler getTurnHandler() {
        return turnHandler;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public Game getGame() {
        return game;
    }

    public HashMap<HumanPlayer, VirtualView> getPlayerSockets() {
        return playerSockets;
    }

    public HashMap<ServerClientConnection, HumanPlayer> getPlayersInGame() {
        return playersInGame;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public InitializationHandler getInitializationHandler() {
        return initializationHandler;
    }

    @Override
    public void run() {
        while(true){
            System.out.println("Partita da " + numberOfPlayers +" giocatori: ");
            for (ServerClientConnection scc :
                    playersInGame.keySet()) {
                System.out.println(scc.getNickname() + scc.isActive());
            }
            try {
                Thread.sleep(5*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
