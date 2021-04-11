package org.model;

public class Product {

    private int ID;
    private String name;
    private int quantity;
    private float price;

    public Product() {
        this.ID = 0;
        this.name = null;
        this.quantity = 0;
        this.price = 0;
    }

    public Product(int ID, String name, int quantity, float price) {
        this.ID = ID;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
