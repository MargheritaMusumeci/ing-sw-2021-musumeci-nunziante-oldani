package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;
import it.polimi.ingsw.model.popeTrack.PopeCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoActionPlayer extends DoAction {

    TurnHandlerMultiPlayer turnHandler;
    public DoActionPlayer(Game modelGame, TurnHandlerMultiPlayer turnHandler) {
       this.modelGame = modelGame;
       this.turnHandler=turnHandler;
    }

    /**
     * This method manages the purchase of resources from market, updates the market board and the player's stock
     * @param position indicates the row or column to be purchased
     * @param isRow true if player chose a row, false otherwise
     */
    public void buyFromMarket(int position, boolean isRow) {

        //Purchase resources from market and updates the market board
        Resource[] resources = null;
        try {
            resources = modelGame.getMarket().updateBoard(position, isRow);
        } catch (ExcessOfPositionException e) {
            e.getLocalizedMessage();
        }

        //check if leader card is in use and refactor array to arraylist
        ArrayList<Resource> resourceList = modifyResources(resources);

        //save resources in model section
        HumanPlayer humanPlayer = (HumanPlayer) modelGame.getActivePlayer();
        humanPlayer.setResources(resourceList);

        //Increase PopeTrackPosition if player got a faith ball
        if (resourceList.contains(Resource.FAITH)) {
            try {
                modelGame.getActivePlayer().getPopeTrack().updateGamerPosition(1);
            } catch (ExcessOfPositionException e) {
                e.getLocalizedMessage();
            }
            resourceList.remove(Resource.FAITH);
        }

        //Ask client which resources he want to discard
        ArrayList<Resource> saveResources = humanPlayer.getResources();

        //until the player chooses a correct number of resources to insert
        while(modelGame.getActivePlayer().getDashboard().getStock().manageStock(saveResources)){

            //notify error
            //reestablish original resources
            humanPlayer.setResources(resourceList);

            //ask again
        }
        ArrayList<Resource> discardResource = resourceList;

        for (Resource saveResource : saveResources) {
            discardResource.remove(saveResource);
        }

        /*
        ArrayList<Resource> discardResource = null;
        //MESSAGE discardResource = throwAway(resourceList); !!!!! toDo

        for (Resource deleteResource : discardResource) {
            resourceList.remove(deleteResource);
        }

        while (modelGame.getActivePlayer().getDashboard().getStock().manageStock(resourceList)) {
            for (Resource deleteResource : discardResource) {
                resourceList.add(deleteResource);
            }
            //Ask client which resources he want to discard

            discardResource = null;
            //MESSAGE discardResource = throwAway(resourceList); !!!!! toDo

            for (Resource deleteResource : discardResource) {
                resourceList.remove(deleteResource);
            }
        }

        /*
        STOCK AUTOGESTITO
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
         */

        //Increase popeTracks of players of as many positions as the number of resources discarded by activePlayer
        List<Player> players = new ArrayList<>();
        if(discardResource.size()>0) {
            for (Player player : modelGame.getPlayers()) {
                if (!player.equals(modelGame.getActivePlayer())) {
                    players.add(player);
                }
            }
            moveCross(discardResource.size(), (ArrayList<Player>) players);
        }

        //mossa effettuata
        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.BUY_FROM_MARKET);
    }

    /**
     * Set 'active' a specified leaderCard. In case of STOCKPLUS-leaderCard this method create new space in stock
     *
     * @param position index of the array of leaderCard
     */
    public void activeLeaderCard(int position){
        try {
            ((HumanPlayer) modelGame.getActivePlayer()).activeLeaderCard(position);
        } catch (OutOfBandException | LeaderCardAlreadyUsedException e) {
           e.getLocalizedMessage();
        }

        //se è un potere di produzione aggiuntivo come lo tratto? creo un'altra produzionZonePlus ?
    }

    @Override
    public void buyEvolutionCard(int row, int col) {
        //compro una carta e la assegno alla zona di produzione
    }

    @Override
    public void moveCross(int positions, ArrayList<Player> players) {

        //Increment Pope Track
        for (Player player : players) {

            if (player instanceof LorenzoPlayer) {
                try {
                    player.getPopeTrack().checkLorenzoPosition(positions);
                } catch (OutOfBandException e) {
                    e.getLocalizedMessage();
                }
            } else {
                try {
                    player.getPopeTrack().updateGamerPosition(positions);
                } catch (ExcessOfPositionException e) {
                    e.getLocalizedMessage();
                }
            }
        }

        //Check Pope section
        for (Player player : players) {

            if (!(player instanceof LorenzoPlayer)) {

                /**
                 * se sono in una posizione di incontro papale
                 * se nessuno era già arrivato la
                 * se non avevo scartato quella carta papale --> quite impossible se nessuno vi era già arrivato
                 */
                if (player.getPopeTrack().getGamerPosition().getPopePosition() &&
                        (turnHandler.getLastSection() < player.getPopeTrack().getGamerPosition().getNumPopeSection()) &&
                        !player.getPopeTrack().getPopeCard().get(player.getPopeTrack().getGamerPosition().getNumPopeSection()).isDiscard()) {

                    PopeCard popeCard = player.getPopeTrack().getPopeCard().get(player.getPopeTrack().getGamerPosition().getNumPopeSection() - 1);
                    popeCard.setIsUsed();
                    turnHandler.setLastSection(player.getPopeTrack().getGamerPosition().getNumPopeSection());

                    // per tutti gli altri player controllo se sono nella stessa sezione papale, in caso affermativo attivo anche la loro carta, in caso negativo la scarto
                    for (Player player2 : players) {
                        if (player2 != player &&
                                player2.getPopeTrack().getGamerPosition().getPopeSection() &&
                                player2.getPopeTrack().getGamerPosition().getNumPopeSection() == turnHandler.getLastSection() &&
                                !player.getPopeTrack().getPopeCard().get(player.getPopeTrack().getGamerPosition().getNumPopeSection()).isDiscard()) {
                                player2.getPopeTrack().getPopeCard().get(player2.getPopeTrack().getGamerPosition().getNumPopeSection() - 1).setIsUsed();
                        } else {
                            player2.getPopeTrack().getPopeCard().get(turnHandler.getLastSection()).setIsDiscard();
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * Method used for replace white balls with colored ones if a leader card NOMOREWHITE is in use.
     * Method used also for transform the array of resources in an arrayList.
     * @param resources resources obtained by market
     * @return arrayList of modified resources
     */
    private ArrayList<Resource> modifyResources(Resource[] resources){

        //if leader card with power: NOMOREWHITE is active and in use, modify resource list obtained from market
        for(int i = 0; i<modelGame.getActivePlayer().getDashboard().getLeaderCards().size();i++){

            if(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(i).isUsed() &&
                    modelGame.getActivePlayer().getDashboard().getLeaderCards().get(i).getAbilityType() == LeaderAbility.NOMOREWHITE){

                Resource replaceResource = Resource.NOTHING;
                if(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(i).getAbilityResource().get(Resource.COIN)!=0) replaceResource = Resource.COIN;
                if(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(i).getAbilityResource().get(Resource.ROCK)!=0) replaceResource = Resource.ROCK;
                if(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(i).getAbilityResource().get(Resource.SERVANT)!=0) replaceResource = Resource.SERVANT;
                if(modelGame.getActivePlayer().getDashboard().getLeaderCards().get(i).getAbilityResource().get(Resource.SHIELD)!=0) replaceResource = Resource.SHIELD;

                for(int j=0; j<resources.length;j++){
                    if(resources[j]==Resource.NOTHING){
                        resources[j]=replaceResource;
                    }
                }
            }
        }
        List<Resource> resourceList = Arrays.asList(resources);
        return (ArrayList<Resource>) resourceList;
    }
}
