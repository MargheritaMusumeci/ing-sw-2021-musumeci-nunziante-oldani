package it.polimi.ingsw.model;

public class Resource {

    private ResourceType type;

    public Resource(ResourceType type){
        this.type = type;
    }

    /**
     * This method returns the type of the resource
     * @return type
     */
    public ResourceType getType() {
        return type;
    }
}
