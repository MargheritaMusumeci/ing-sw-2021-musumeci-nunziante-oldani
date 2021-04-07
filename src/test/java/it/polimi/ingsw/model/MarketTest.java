package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class MarketTest {

    @Test
    public void getInstanceOfMarketTest() {
        Market m1 = null;
        Market m2 = null;
        m1 = Market.getInstanceOfMarket();
        m2 = Market.getInstanceOfMarket();
        assertNotNull(m1);
        assertEquals(m1, m2);

    }

    @Test
    public void updateBoardTest() throws ExcessOfPositionException {
        Market m = Market.getInstanceOfMarket();
        Resource[][] r = m.getMarketBoard();
        Resource[][] rnew = null;
        Resource er = m.getExternalResource();
        Resource temp;
        Resource[] returnedResouces;




        //testing row
        for(int i=0; i<r.length; i++){

            System.out.println("riga: " + i +"\n\n");
            System.out.println("Stampo la board del market");
            for (int k=0; k<3; k++){
                for (int j=0; j<4; j++){
                    System.out.print(r[k][j] + "\t");
                }
                System.out.println();
            }

            System.out.println("externale resource is: " + er +"\n\n");
            returnedResouces = m.updateBoard(i, true);
            rnew = m.getMarketBoard();

            //check if the other cells aren't changed
            for (int k=0; k<r.length; k++){
                for (int j=0; j<r[k].length; j++){
                    if(k!=i) {
                        assertEquals(rnew[k][j], r[k][j]);
                    }
                }
            }


            System.out.println();
            System.out.println();
            //check if the returned array is the old one
            Resource[] row = new Resource[r[i].length];
            for (int j=0; j<row.length; j++){
                row[j] = r[i][j];
            }
            //stampo le risorse utili e controllo che siano uguali con l'assert
            for (int k=0; k<returnedResouces.length; k++){
                assertEquals(returnedResouces[k], row[k]);
                System.out.print(returnedResouces[k] + " ");
                System.out.print(row[k] + "\t");
            }
            System.out.println();
            System.out.println();

           //check if the substitution has occured
            r = m.getMarketBoard();
            temp = returnedResouces[0];
            for(int j= 0; j<returnedResouces.length-1; j++){
                returnedResouces[j] = returnedResouces[j+1];
            }
            returnedResouces[returnedResouces.length-1] = er;
            er = temp;
            System.out.println("Stampo la board del market");
            for (int k=0; k<3; k++){
                for (int j=0; j<4; j++){
                    System.out.print(r[k][j] + "\t");
                }
                System.out.println();
            }

            System.out.println("externale resource is: " + er +"\n\n");
            System.out.println();

            for (int j=0; j<row.length; j++){
                row[j] = r[i][j];
            }
            for (int k=0; k<returnedResouces.length; k++){
                assertEquals(returnedResouces[k], row[k]);
                System.out.print(returnedResouces[k] + " ");
                System.out.print(row[k] + "\t");
            }
            assertEquals(er, m.getExternalResource());
            System.out.println();
            System.out.println();

        }

        //testing cols
        for (int i=0; i<r[0].length; i++){
            System.out.println("colonna: " + i +"\n\n");
            System.out.println("Stampo la board del market");
            for (int k=0; k<3; k++){
                for (int j=0; j<4; j++){
                    System.out.print(r[k][j] + "\t");
                }
                System.out.println();
            }

            System.out.println("externale resource is: " + er +"\n\n");
            returnedResouces = m.updateBoard(i, false);
            rnew = m.getMarketBoard();
            //check if the other cells aren't changed
            for (int k=0; k<r.length; k++){
                for (int j=0; j<r[k].length; j++){
                    if(j!=i) {
                        assertEquals(rnew[k][j], r[k][j]);
                    }
                }
            }


            //check if the returned array is the old on
            Resource[] col = new Resource[r.length];
            for (int j=0; j<col.length; j++){
                col[j] = r[j][i];
            }

            //stampo le risorse utili e controllo che siano uguali con l'assert
            for (int k=0; k<returnedResouces.length; k++){
                assertEquals(returnedResouces[k], col[k]);
                System.out.print(returnedResouces[k] + " ");
                System.out.print(col[k] + "\t");
            }
            System.out.println();
            System.out.println();


            //check if the substitution has occured
            r = m.getMarketBoard();
            temp = returnedResouces[0];
            for(int j= 0; j<returnedResouces.length-1; j++){
                returnedResouces[j] = returnedResouces[j+1];
            }
            returnedResouces[returnedResouces.length-1] = er;
            er = temp;
            System.out.println("Stampo la board del market");
            for (int k=0; k<3; k++){
                for (int j=0; j<4; j++){
                    System.out.print(r[k][j] + "\t");
                }
                System.out.println();
            }

            System.out.println("externale resource is: " + er +"\n\n");
            System.out.println();

            for (int j=0; j<col.length; j++){
                col[j] = r[j][i];
            }
            for (int k=0; k<returnedResouces.length; k++){
                assertEquals(returnedResouces[k], col[k]);
                System.out.print(returnedResouces[k] + " ");
                System.out.print(col[k] + "\t");
            }
            assertEquals(er, m.getExternalResource());
            System.out.println();
            System.out.println();
        }

        try {
            m.updateBoard(4, true);
        } catch (ExcessOfPositionException e) {
            System.out.println(e.getMessage());

        }

        try {
            m.updateBoard(4, false);
        } catch (ExcessOfPositionException e) {
            System.out.println(e.getMessage());
        }



    }

    @Test
    public void getPositionTest() throws ExcessOfPositionException {

        Market m = Market.getInstanceOfMarket();

        Resource pos;

        //check that every position is not null
        for (int i=0; i<m.getMarketBoard().length; i++){
            for (int j=0; j<m.getMarketBoard()[i].length; j++){
                pos = m.getPosition(i,j);
                assertNotNull(pos);
            }
        }

        //check that position won't change after a market upadate

        pos = m.getPosition(0,0);
        m.updateBoard(0, true);
        assertNotEquals(pos, m.getPosition(0,0));

        try{
            pos = m.getPosition(-1, 3);

        }catch (ExcessOfPositionException e){
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void getMarketBoardTest() throws ExcessOfPositionException {
        //check that the returned value doesn't chenage
        Resource[][] board1, board2;
        Market m = Market.getInstanceOfMarket();

        board1 = m.getMarketBoard();
        board2 = m.getMarketBoard();

        //this has to be true
        assertNotEquals(board2, board1);
        for (int i=0; i<board1.length; i++){
            for (int j=0; j<board2.length; j++){
                assertEquals(board1[i][j], board2[i][j]);
            }
        }

        m.updateBoard(1,true);

        //this still has to be true after the changes
        assertNotEquals(board2, board1);
        for (int i=0; i<board1.length; i++){
            for (int j=0; j<board2.length; j++){
                assertEquals(board1[i][j], board2[i][j]);
            }
        }

    }
}