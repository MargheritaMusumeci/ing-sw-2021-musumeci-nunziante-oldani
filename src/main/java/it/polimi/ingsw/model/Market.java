package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class Market {

    private Resource[][] marketBoard;
    private Resource externalResource;

    /**
     * initialize a marker with random resource positions and random external resource according to the rules
     * the number of type of resources is defined, only the position is random
     */
    public Market(){
        marketBoard = new Resource[3][4];
        randomPopulateBoard();
    }

    /**
     * method that really populate the board when the object is istantiated
     */
    private void randomPopulateBoard(){
        ArrayList<Resource> listOfElement = new ArrayList<Resource>();
        Random r = new Random();
        int numChosen;

        //2 coins, 2 shilds, 2 servants, 2 rocks, 1 faith, 4 nothing --> tot 13
        listOfElement.add(Resource.COIN);
        listOfElement.add(Resource.COIN);
        listOfElement.add(Resource.ROCK);
        listOfElement.add(Resource.ROCK);
        listOfElement.add(Resource.SERVANT);
        listOfElement.add(Resource.SERVANT);
        listOfElement.add(Resource.SHIELD);
        listOfElement.add(Resource.SHIELD);
        listOfElement.add(Resource.FAITH);
        listOfElement.add(Resource.NOTHING);
        listOfElement.add(Resource.NOTHING);
        listOfElement.add(Resource.NOTHING);
        listOfElement.add(Resource.NOTHING);

        numChosen = r.nextInt(listOfElement.size());
        externalResource = listOfElement.get(numChosen);
        listOfElement.remove(numChosen);

        for (int i=0; i<marketBoard.length; i++){
            for (int j=0; j<marketBoard[i].length;j++){
                if(listOfElement.size() != 0){
                    numChosen = r.nextInt(listOfElement.size());
                }else{
                    numChosen = 0;
                }

                marketBoard[i][j] = listOfElement.get(numChosen);
                listOfElement.remove(numChosen);

            }
        }

    }

    /**
     * method that update resources in the market when someone buy at the market
     * @param position is the index of the row/column chosen to play the move in the market
     * @param isRow if true the player have chosen a row, if false a column
     * @return the array of resources bought at the market
     */
    public Resource[] updateBoard(int position, boolean isRow){

        Resource tempRes;
        Resource[] bought = null;

        if(isRow){
            bought = marketBoard[position].clone();
            tempRes = marketBoard[position][0];
            for (int i=0; i< marketBoard[position].length - 1; i++){
                marketBoard[position][i] = marketBoard[position][i+1];
            }
            marketBoard[position][3] = externalResource;
            externalResource = tempRes;
        }else{
            bought = new Resource[3];
            tempRes = marketBoard[0][position];
            for (int i=0; i< marketBoard.length - 1; i++){
                bought[i] = marketBoard[i][position];
                marketBoard[i][position] = marketBoard[i+1][position];
            }
            marketBoard[2][position] = externalResource;
            externalResource = tempRes;
        }

        return bought;
    }

    /**
     * method that return the resource at the given position in the market
     * @param row is the number of the chosen row (0 is the first)
     * @param column is the number of the chosen column (0 is the first)
     * @return the resource at that position in the market
     */
    public Resource getPosition(int row, int column){
        return marketBoard[row][column];
    }

    /**
     * method that returns a copy of the market board
     * @return a copy of the market board
     */
    public Resource[][] getMarketBoard(){
        return marketBoard.clone();
    }
}
