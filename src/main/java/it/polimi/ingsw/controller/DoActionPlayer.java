package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.board.Stock;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.Player;
import java.util.Arrays;
import java.util.List;

public class DoActionPlayer extends DoAction {


    public DoActionPlayer(Game modelGame) {
       this.modelGame = modelGame;
    }

    /**
     * This method manages the purchase of resources from market, updates the market board and the player's stock
     *
     * @param position indicates the row or column to be purchased
     * @param isRow true if player chose a row, false otherwise
     * @param personalStock activePlayer's stock
     * @throws ExcessOfPositionException
     * @throws OutOfBandException
     * @throws ResourceAlreadyPresentException
     * @throws NotEnoughSpaceException
     */
    public void buyFromMarket(int position, boolean isRow, Stock personalStock){
        //devo controllare che posso effettivamente svolgere la mossa?
        //if( modelGame.getActivePlayer().getActionState()==false){}

        //mossa effettuata
        modelGame.getActivePlayer().setActionState(true);

        //prendo le risorse dal mercato e aggiorno qust'ultimo
        Resource[] resources = null;
        try {
            resources = modelGame.getMarket().updateBoard(position, isRow);
        } catch (ExcessOfPositionException e) {
            e.getLocalizedMessage();
        }

        //converto le risorse ottenute in arrayList per comodità della gestione
        List<Resource> resourceList= Arrays.asList(resources);

        //se ho ottenuto un punto fede allora incremento il popeTrack --> sempre 1 posizione
        if(resourceList.contains(Resource.FAITH)){
            try {
                modelGame.getActivePlayer().getPopeTrack().updateGamerPosition(1);
            } catch (ExcessOfPositionException e) {
                e.getLocalizedMessage();
            }
            resourceList.remove(Resource.FAITH);
        }

        //prendo dal client le informazioni riguardanti il tipo di risorsa, la futura locazione della risorsa e se le biglie rimaste le vuole scartare
        int stockBox;
        int numberOfResourses;
        Resource typeOfResource;
        boolean stopStoreResources = false;

        while ((resourceList.size() > 0) && (!stopStoreResources)) {
            stockBox = 1; //sarà il risultato di un metodo che chiede al client in che box inserire le nuove biglie
            numberOfResourses = 1; //sarà il risultato di un metodo che chiede al client quante biglie inserire
            typeOfResource = Resource.COIN; //sarà il risultato di un metodo che chiede al client che tipo di biglie inserire

            try {
                personalStock.addResources(stockBox, numberOfResourses, typeOfResource);
            } catch (NotEnoughSpaceException | ResourceAlreadyPresentException | OutOfBandException e) {
                e.getLocalizedMessage();
            }

            while(numberOfResourses>0){
                resourceList.remove(typeOfResource);
                numberOfResourses--;
            }
            stopStoreResources = false; //sarà il risultato di un metodo che chiede al client se le biglie restanti le vuole scartare
        }

        //Increase popeTracks of players of as many positions as the number of resources discarded by activePlayer
        if(resourceList.size()>0) {
            for (Player player : modelGame.getPlayers()) {
                if (player.equals(modelGame.getActivePlayer())) {
                    moveCross(resourceList.size());
                }
            }
        }
    }

    /**
     * Set 'active' a specified leaderCard. In case of STOCKPLUS-leaderCard this method create new space in stock
     *
     * @param position index of the array of leaderCard
     * @throws OutOfBandException
     * @throws LeaderCardAlreadyUsedException
     */
    public void activeLeaderCard(int position){
        try {
            modelGame.getActivePlayer().activeLeaderCard(position);
        } catch (OutOfBandException | LeaderCardAlreadyUsedException e) {
           e.getLocalizedMessage();
        }

        //se è un potere di produzione aggiuntivo come lo tratto? creo un'altra produzionZonePlus ?
    }

    /**
     *  Controller method used for move resources from a box to an other
     */
    public void manageStock(int originBox, int finalBox) throws OutOfBandException, NotEnoughSpaceException {

        modelGame.getActivePlayer().getDashboard().getStock().moveResources(originBox, finalBox);

    }

    @Override
    public void buyEvolutionCard(int row, int col) {
        //compro una carta e la assegno alla zona di produzione
    }

    @Override
    public void moveCross(int positions) {
        try {
            modelGame.getActivePlayer().getPopeTrack().updateGamerPosition(positions);
        } catch (ExcessOfPositionException e) {
            e.getLocalizedMessage();
        }
    }
}
