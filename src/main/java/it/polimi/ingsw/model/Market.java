package it.polimi.ingsw.model;

public class Market {

    private Resource[][] marketBoard;
    private Resource externalResource;

    /**
     * initialize a marker with random resource positions and random external resource according to the rules
     * the number of type of resources is defined, only the position is random
     */
    public Market(){

    }

    /**
     * method that really populate the board when the object is istantiated
     */
    private void randomPopulateBoard(){

    }

    /**
     *
     * @param position is the index of the row/column chosen to play the move in the market
     * @param isRow if true the player have chosen a row, if false a column
     */
    public void updateBoard(int position, boolean isRow){

    }

    /**
     * methos that return the resource at the given position in the market
     * @param row is the number of the chosen row (0 is the first)
     * @param column is the number of the chosen column (0 is the first)
     * @return the resource at that position in the market
     */
    public Resource getPosition(int row, int column){
        //fake object to avoid errors before actually implement the method
        Resource r = new Resource(ResourceType.COIN);
        return r;
    }
}
