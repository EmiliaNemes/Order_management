package org.model;

public class OrderProducts {

    private int ID;
    private int orderID;
    private int productID;
    private int quantity;

    public OrderProducts() {
        this.ID = 0;
        this.orderID = 0;
        this.productID = 0;
        this.quantity = 0;
    }

    public OrderProducts(int ID, int orderID, int productID, int quantity) {
        this.ID = ID;
        this.orderID = orderID;
        this.productID = productID;
        this.quantity = quantity;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
