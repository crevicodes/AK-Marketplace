package com.example.akmarketplace;

import java.util.ArrayList;

public class Item { //item class for retrieving from database

    private long time_added_millis;
    private String title;
    private String description;
    private String image;
    private String sold;
    private String sellerName;
    private String sellerEmail;
    private ArrayList<String> buyerEmails;
    private String sellerPhone;
    private double locationLat;
    private double locationLng;
    private double price;


    public Item(long time_added_millis, String title, String description, double price, String sellerName, String sellerEmail, String sellerPhone, String image, double locationLat, double locationLng, ArrayList<String> buyerEmails, String sold) {

        this.time_added_millis = time_added_millis;
        this.title = title;
        this.description = description;
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
        this.sellerPhone = sellerPhone;
        this.image = image;
        this.locationLat = locationLat;
        this.locationLng = locationLng;
        this.price = price;
        this.buyerEmails = buyerEmails;
        this.sold = sold;

    }

    public Item() {

    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public long getTime_added_millis() {
        return time_added_millis;
    }

    public void setTime_added_millis(long time_added_millis) {
        this.time_added_millis = time_added_millis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }
    
    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(double locationLng) {
        this.locationLng = locationLng;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setBuyerEmails(ArrayList<String> buyerEmails) {
        this.buyerEmails = buyerEmails;
    }

    public ArrayList<String> getBuyerEmails() {
        return this.buyerEmails;
    }
}
