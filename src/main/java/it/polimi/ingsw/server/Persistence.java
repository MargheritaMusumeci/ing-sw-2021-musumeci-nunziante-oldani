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

// se il server cade invio un game aborted ?

public class Persistence implements Runnable{
    Server server;
    HashMap<String, Game> playerGame;

    private Game gameToBeSaved;

    public Persistence(Server server){
        this.server = server;
        playerGame = new HashMap<>();
    }


    public PersistenceSerializableGame readGame(String path) {

        PersistenceSerializableGame game = null;

        try {
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream streamer = new ObjectInputStream(file);

            game = (PersistenceSerializableGame) streamer.readObject();
            streamer.close();

            System.out.println("Done");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return game;
    }

    public void saveGame(Game game) {
        gameToBeSaved = game;
        new Thread(this).start();
    }


    public void deleteGame(String absolutePath) {
        File file = new File(absolutePath);
        file.delete();
    }


    public void initializeGame() {

        try {

            String tempPath = System.getProperty("java.io.tmpdir");
            File[] files = new File(tempPath).listFiles();

            //check if there is a directory eith the saved games
            String dirPath = null;
            for(File file: files){
                System.out.println(file.getAbsolutePath());
                System.out.println(tempPath+"savedGame");
                if(file.getAbsolutePath().contains((tempPath+"savedGames"))){
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

    private Game recreateGameFromPersistence(PersistenceSerializableGame persistenceSerializableGame) {

        try {
            Player activePlayer = null;
            Market market = new Market();
            market.setMarketBoard(persistenceSerializableGame.getMarket().getMarket(), persistenceSerializableGame.getMarket().getExternalResource());

            EvolutionSection evolutionSection = new EvolutionSection();
            evolutionSection.setEvolutionSection(persistenceSerializableGame.getEvolutionSection());

            //devo creare i player
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
                    popeTrack.setTrack();

                    ArrayList<LeaderCard> leaderCards = persistenceSerializableGame.getLeaderCards().get(playerNickname);

                    Dashboard dashboard = new Dashboard(playerNickname, inkwell, popeTrack, leaderCards, stock, lockBox, normalProductionZones, leaderProductionZone);
                    Player player = new HumanPlayer(playerNickname, popeTrack, dashboard);
                    players.add(player);
                    if (playerNickname.equals(persistenceSerializableGame.getActivePlayerNickname()))
                        activePlayer = player;

                    //se è un solo game ricostruisco anche lorenzo
                    if(persistenceSerializableGame.getPlayerNicknames().contains("LorenzoIlMagnifico")){
                        players.add(new LorenzoPlayer(popeTrack,dashboard));
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

    @Override
    public void run() {
        PersistenceSerializableGame persistenceSerializableGame = new PersistenceSerializableGame(gameToBeSaved);
        try {

            String path = "";
            for (Player player : gameToBeSaved.getPlayers()) {
                path = path + player.getNickName();
            }

            String tempPath = System.getProperty("java.io.tmpdir");
            File[] files = new File(tempPath).listFiles();
            String dirPath = tempPath + "savedGames/";



            boolean controllo = false;
            for(File file: files){
                System.out.println(file.getAbsolutePath());
                System.out.println(tempPath+"savedGame");
                if(file.getAbsolutePath().equals((tempPath+"savedGames"))){
                    controllo = true;
                }

            }

            if(!controllo){

                Path dirPathTemp = Files.createTempDirectory("savedGames");

                File sourceFile =dirPathTemp.toFile();
                File destFile = new File(tempPath + "savedGames");

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
            System.out.println("Done");

            // closing resources
            streamer.close();
            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
