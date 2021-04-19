package it.polimi.ingsw.controller;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.board.Stock;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DoActionPlayer {

    private Game modelGame;

    public DoActionPlayer(Game modelGame) {
        this.modelGame = modelGame;
    }

    /**
     * update market and dashboard of the active player
     *
     * @param position is the number of row or column that player wish to buy
     * @param isRow    true if player chose a row, false otherwise
     * @throws ExcessOfPositionException
     * @throws OutOfBandException
     * @throws ResourceAlreadyPresentException
     * @throws NotEnoughSpaceException
     */
    public void buyFromMarket(int position, boolean isRow, Stock stock) throws ExcessOfPositionException, OutOfBandException, ResourceAlreadyPresentException, NotEnoughSpaceException {

        //devo controllare che posso effettivamente svolgere la mossa?
        //if(  modelGame.getActivePlayer().getActionState()==false){}

        //mossa effettuata
        modelGame.getActivePlayer().setActionState(true);

        //prendo le risorse dal mercato e aggiorno qust'ultimo
        Resource[] resources = modelGame.getMarket().updateBoard(position, isRow);
        List<Resource> resourceList= Arrays.asList(resources);

        modelGame.getActivePlayer().getDashboard().getStock();

        //se pesco un punto fede incremento il popeTrack
        if(resourceList.contains(Resource.FAITH)){
            modelGame.getActivePlayer().getPopeTrack().updateGamerPosition(1);
            resourceList.remove(Resource.FAITH);
        }
        //prendo dal client le informazioni riguardanti il tipo di risorsa e la futura locazione della risorsa

        int stockBox;
        int numberOfResourses;
        Resource typeOfResource;
        boolean stopStoreResources = false;

        while ((resourceList.size() > 0) && (!stopStoreResources)) {
            stockBox = 1; //sarà il risultato di un metodo che chiede al client in che box inserire le nuove biglie
            numberOfResourses = 1; //sarà il risultato di un metodo che chiede al client quante biglie inserire
            typeOfResource = Resource.COIN; //sarà il risultato di un metodo che chiede al client che tipo di biglie inserire

            //try -- catch
            stock.addResources(stockBox, numberOfResourses, typeOfResource);
            while(numberOfResourses>0){
                int index =0;
                while(index<resourceList.size()){
                    if(resourceList.get(index).equals(typeOfResource)){
                        resourceList.remove(index);
                        break;
                    }
                    index++;
                }
                numberOfResourses--;
            }
        }
        discardResource(resourceList.size());
    }

    /**
     * Set 'active' a specified leaderCard. In case of STOCKPLUS-leaderCard this method create new space in stock
     *
     * @param position of the array of leaderCard
     * @throws OutOfBandException
     * @throws LeaderCardAlreadyUsedException
     */
    public void activeLeaderCard(int position, Game modelGame) throws OutOfBandException, LeaderCardAlreadyUsedException {
        modelGame.getActivePlayer().activeLeaderCard(position);
        //se è un potere di produzione aggiuntivo come lo tratto? creo un'altra produzionZonePlus ?
    }

    /**
     * method used for move resources from a box to an other
     */
    public void manageStock(int originBox, int finalBox) throws OutOfBandException, NotEnoughSpaceException {

        modelGame.getActivePlayer().getDashboard().getStock().moveResources(originBox, finalBox);

    }

    public void discardResource(int resourcesDiscarded) throws ExcessOfPositionException {
        for (Player player : modelGame.getPlayers()) {
            if (player.equals(modelGame.getActivePlayer())) {
                player.getPopeTrack().updateGamerPosition(resourcesDiscarded);
            }
        }
    }
}
