package org.model;

public class Client {

    private int ID;
    private String name;
    private String address;

    public Client() {
        this.ID = 0;
        this.name = null;
        this.address = null;
    }

    public Client(int ID, String name, String address) {
        this.ID = ID;
        this.name = name;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
