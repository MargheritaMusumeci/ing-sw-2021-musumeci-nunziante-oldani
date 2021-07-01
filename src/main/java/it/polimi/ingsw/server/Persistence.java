package it.polimi.ingsw.server;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.board.LeaderProductionZone;
import it.polimi.ingsw.model.board.Stock;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.EvolutionSection;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;
import it.polimi.ingsw.model.popeTrack.PopeTrack;
import it.polimi.ingsw.serializableModel.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * class that contains the main methods for the persistance. It is able to reload the games when the server is restarted
 * and also has the method to store the game status after every turn.
 */
public class Persistence implements Runnable{
    Server server;
    HashMap<String, Game> playerGame;

    private Game gameToBeSaved;

    public Persistence(Server server){
        this.server = server;
        playerGame = new HashMap<>();
    }


    /**
     * method that gets the game saved at the given path
     * @param path is the path where to find the persistence file with the game
     * @return the objcet store into the file
     */
    public PersistenceSerializableGame readGame(String path) {

        PersistenceSerializableGame game = null;

        try {
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream streamer = new ObjectInputStream(file);

            game = (PersistenceSerializableGame) streamer.readObject();
            streamer.close();

            System.out.println("READ A GAME");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return game;
    }

    /**
     * method that save in an attribute the game to be stored and creates a thread to store it
     * @param game is the gae to be stored
     */
    public void saveGame(Game game) {
        if(game.getActivePlayer() == null){
            //if there aren't active player due to a disconnection i don't have to save the game because the last turn
            //was not eligible for the game
            return;
        }
        gameToBeSaved = game;
        new Thread(this).start();
    }

    /**
     * method able to delete the file related to a game after the game is completed
     * @param absolutePath is the path of the game that has to be deleted
     */
    public void deleteGame(String absolutePath) {
        File file = new File(absolutePath);
        file.delete();
    }

    /**
     * method that checks if there are games that have to loaded when the server is restarted.
     * It also creates the temp folder if it is not present.
     */
    public void initializeGame() {

        //the path changes based on OS due to a difference in path slashes
        String OS = (System.getProperty("os.name")).toUpperCase();
        String savedGamePath;

        if(OS.contains("MAC") || OS.contains("WIN")){
            System.out.println("MAC or WIN system");
            savedGamePath = "savedGames";
        }else{
            System.out.println("Linux system");
            savedGamePath = "/savedGames";
        }

        try {
            String tempPath = System.getProperty("java.io.tmpdir");
            System.out.println("Temp Path: " + tempPath);
            File[] files = new File(tempPath).listFiles();

            //check if there is a directory with the saved games
            String dirPath = null;
            for(File file: files){

                if(file.getAbsolutePath().contains((tempPath+savedGamePath))){
                    dirPath = file.getAbsolutePath();
                    break;

                }

            }
            if (dirPath == null) {
                server.setPersistenceWaitingList(null);
                return;
            }

            files = new File(dirPath).listFiles();

            if (files == null) {
                server.setPersistenceWaitingList(null);
                return;
            }
            Game game = null;
            for (File file : files) {
                PersistenceSerializableGame persistenceSerializableGame = readGame(file.getAbsolutePath());

                game = recreateGameFromPersistence(persistenceSerializableGame);

                server.getPersistenceWaitingList().put(game, new ArrayList<>());

                for (Player player : game.getPlayers()) {
                    if(!player.getNickName().equals("LorenzoIlMagnifico")) {
                        server.getPersistenceNicknameList().add(player.getNickName());
                        playerGame.put(player.getNickName(), game);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * method able to recreate a game from the object stores in the file
     * @param persistenceSerializableGame is the object containing the game's information
     * @return the game
     */
    private Game recreateGameFromPersistence(PersistenceSerializableGame persistenceSerializableGame) {

        try {
            Player activePlayer = null;
            Market market = new Market();
            market.setMarketBoard(persistenceSerializableGame.getMarket().getMarket(), persistenceSerializableGame.getMarket().getExternalResource());

            EvolutionSection evolutionSection = new EvolutionSection();
            evolutionSection.setEvolutionSection(persistenceSerializableGame.getEvolutionSection());

            //dlooks for the players
            ArrayList<Player> players = new ArrayList<>();
            for (String playerNickname : persistenceSerializableGame.getPlayerNicknames()) {

                if (!playerNickname.equals("LorenzoIlMagnifico")) {

                    boolean inkwell;
                    if (playerNickname.equals(persistenceSerializableGame.getPlayerNicknames().get(0))) {
                        inkwell = true;
                    } else {
                        inkwell = false;
                    }

                    SerializableStock stock = persistenceSerializableGame.getPlayerDashboards().get(playerNickname).getSerializableStock();
                    SerializableLockBox lockBox = persistenceSerializableGame.getPlayerDashboards().get(playerNickname).getSerializableLockBox();

                    SerializableProductionZone[] normalProductionZones = persistenceSerializableGame.getPlayerDashboards().get(playerNickname).getSerializableProductionZones();
                    ArrayList<LeaderProductionZone> leaderProductionZone = persistenceSerializableGame.getLeaderProductionZoneHashMap().get(playerNickname);

                    PopeTrack popeTrack = persistenceSerializableGame.getPopeTrackHashMap().get(playerNickname);
                    System.out.println("recreating game popeTrakc lorenzo Position: " + popeTrack.getLorenzoPosition().getIndex());
                    popeTrack.setTrack();

                    ArrayList<LeaderCard> leaderCards = persistenceSerializableGame.getLeaderCards().get(playerNickname);

                    Dashboard dashboard = new Dashboard(playerNickname, inkwell, popeTrack, leaderCards, stock, lockBox, normalProductionZones, leaderProductionZone);
                    Player player = new HumanPlayer(playerNickname, popeTrack, dashboard);
                    players.add(player);
                    if (playerNickname.equals(persistenceSerializableGame.getActivePlayerNickname()))
                        activePlayer = player;

                    //if solo game i have to crete also "lorenzoPlayer"
                    if(persistenceSerializableGame.getPlayerNicknames().contains("LorenzoIlMagnifico")){
                        players.add(new LorenzoPlayer(popeTrack,dashboard, true));
                    }
                }
            }
            if(activePlayer == null){
                activePlayer = players.get(0);
            }
            return new Game(players, market, evolutionSection, activePlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public HashMap<String, Game> getPlayerGame() {
        return playerGame;
    }

    public void setPlayerGame(HashMap<String, Game> playerGame) {
        this.playerGame = playerGame;
    }

    /**
     * method that stores the game in the class attribute "gameToBeStored". It is called every time that someone
     * ends the turn
     */
    @Override
    public void run() {
        System.out.println("game to be saved lorenzo position: " + gameToBeSaved.getPlayers().get(0).getPopeTrack().getLorenzoPosition().getIndex());
        PersistenceSerializableGame persistenceSerializableGame = new PersistenceSerializableGame(gameToBeSaved);
        System.out.println("persistance game lorenzo position: " + persistenceSerializableGame.getPopeTrackHashMap().get(persistenceSerializableGame.getActivePlayerNickname()).getLorenzoPosition().getIndex());
        String OS = (System.getProperty("os.name")).toUpperCase();
        String savedGamePath;
        String directoryPath;
        if(OS.contains("MAC") || OS.contains("WIN")){
            savedGamePath = "savedGames";
            directoryPath = savedGamePath;
        }else{
            savedGamePath = "/savedGames";
            directoryPath = "savedGames";
        }

        try {

            String path = "";
            for (Player player : gameToBeSaved.getPlayers()) {
                path = path + player.getNickName();
            }

            String tempPath = System.getProperty("java.io.tmpdir");
            File[] files = new File(tempPath).listFiles();
            String dirPath = tempPath + savedGamePath + "/";



            boolean controllo = false;
            for(File file: files){
                if(file.getAbsolutePath().equals((tempPath+savedGamePath))){
                    controllo = true;
                }

            }

            if(!controllo){

                Path dirPathTemp = Files.createTempDirectory(directoryPath);

                File sourceFile =dirPathTemp.toFile();

                File destFile = new File(tempPath + savedGamePath);

                if (sourceFile.renameTo(destFile)) {
                    System.out.println("File renamed successfully");
                } else {
                    System.out.println("Failed to rename file");
                }

                dirPath = destFile.getAbsolutePath();

            }
            FileOutputStream file = new FileOutputStream(dirPath + "/" + path + ".ser");
            ObjectOutputStream streamer = new ObjectOutputStream(file);

            // write object to file
            streamer.writeObject(persistenceSerializableGame);

            // closing resources
            streamer.close();
            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
