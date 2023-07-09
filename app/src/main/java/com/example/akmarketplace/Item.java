package com.example.akmarketplace;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.type.LatLng;

public class Item {

    private long time_added_millis;
    private String title;
    private String description;
    private String image;
    private boolean isSold;
    private String sellerName;
    private String sellerEmail;
    private String buyerName;
    private String buyerEmail;
    private String sellerPhone;
    private String buyerPhone;
    private double locationLat;
    private double locationLng;
    private double price;


    public Item(long time_added_millis, String title, String description, double price, String sellerName, String sellerEmail, String sellerPhone, String image, double locationLat, double locationLng) {

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

        this.buyerName = "";
        this.buyerPhone = "";
        this.buyerEmail = "";
        this.isSold = false;
    }

    //public int compare(Item rhs) {
        //if (this.getTime_added_millis() > rhs.getTime_added_millis()) return ()
        //else if (this.getTime_added_millis() < rhs.getTime_added_millis()) return
        //else return 0;
        //return this.getTime_added_millis().compareTo(rhs.getTime_added_millis());
    //}

    public Item() {

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

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
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
}
