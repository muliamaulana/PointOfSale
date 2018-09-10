package com.muliamaulana.pointofsale.database;

/**
 * Created by muliamaulana on 9/9/2018.
 */
public class ItemModel {

    private int id;
    private String name;
    private int price;
    private byte[] image;

    public ItemModel(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
