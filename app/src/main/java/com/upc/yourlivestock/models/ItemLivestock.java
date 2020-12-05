package com.upc.yourlivestock.models;

public class ItemLivestock {

    private String itemPhoto;
    private String itemRace;
    private String itemGender;
    private String itemColor;
    private String itemYears;
    private String itemOwner;
    private String itemContact;
    private String itemMaps;

    public ItemLivestock(String itemPhoto, String itemRace, String itemGender, String itemColor, String itemYears, String itemOwner, String itemContact, String itemMaps) {
        this.itemPhoto = itemPhoto;
        this.itemRace = itemRace;
        this.itemGender = itemGender;
        this.itemColor = itemColor;
        this.itemYears = itemYears;
        this.itemOwner = itemOwner;
        this.itemContact = itemContact;
        this.itemMaps = itemMaps;
    }

    public String getItemPhoto() {
        return itemPhoto;
    }

    public String getItemRace() {
        return itemRace;
    }

    public String getItemGender() {
        return itemGender;
    }

    public String getItemColor() {
        return itemColor;
    }

    public String getItemYears() {
        return itemYears;
    }

    public String getItemOwner() {
        return itemOwner;
    }

    public String getItemContact() {
        return itemContact;
    }

    public String getItemMaps() {
        return itemMaps;
    }
}
