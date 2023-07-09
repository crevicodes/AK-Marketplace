package com.example.akmarketplace;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.type.LatLng;

public class Item {

    private long time_added_millis;
    private String title;
    private String description;
    private Uri image;
    private boolean isSold;
    private String sellerName;
    private String sellerEmail;
    private String buyerName;
    private String buyerEmail;
    private String sellerPhone;
    private String buyerPhone;
    private LatLng location;


    public Item(long time_added_millis, String title, String description, String sellerName, String sellerEmail, String sellerPhone, Uri image, LatLng location) {

        this.time_added_millis = time_added_millis;
        this.title = title;
        this.description = description;
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
        this.sellerPhone = sellerPhone;
        this.image = image;
        this.location = location;

        this.buyerName = "";
        this.buyerPhone = "";
        this.buyerEmail = "";
        this.isSold = false;
    }

    public Item() {

    }


}
