package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Stock {
    private ArrayList<Resource[]> boxes;

    public Stock(){
        boxes = new ArrayList<Resource[]>();
        Resource[] box0 = new Resource[1];
        Resource[] box1 = new Resource[2];
        Resource[] box2 = new Resource[2];
        boxes.add(box0);
        boxes.add(box1);
        boxes.add(box2);
    }

    public void addResources(int originBox , int numberResources){//non serve anche il tipo di risorsa?

    }

    public int getQuantities(int originBox){
        return 0;
    }

    public void useResources(int originBox , int numberResources){

    }

    public void moveResources(int originBox , int destinationBox){

    }
}
