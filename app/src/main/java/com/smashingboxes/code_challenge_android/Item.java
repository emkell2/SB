package com.smashingboxes.code_challenge_android;

/**
 * Model class representing an Item
 * @author Erin Kelley
 */
public class Item {
    private String id;
    private String upc;
    private String itemDescription;
    private String manufacturer;
    private String brand;

    public Item(String itemDescription) {
        this.itemDescription = itemDescription;

        // Would implement these if they were used, but not necessary
        this.id = "";
        this.upc = "";
        this.manufacturer = "";
        this.brand = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
