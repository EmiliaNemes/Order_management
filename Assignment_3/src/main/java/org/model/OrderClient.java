package org.model;

public class OrderClient {

    private int ID;
    private int clientID;
    private int totalPrice;

    public OrderClient() {
        this.ID = 0;
        this.clientID = 0;
        this.totalPrice = 0;
    }

    public OrderClient(int ID, int clientID, int totalPrice) {
        this.ID = ID;
        this.clientID = clientID;
        this.totalPrice = totalPrice;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
