package com.example.akmarketplace;

import android.graphics.Bitmap;

public class Item {

    private int time_added_millis;
    private String title;
    private String description;
    private Bitmap image;
    private boolean isSold;
    private String sellerName;
    private String buyerName;
    private String sellerPhone;

    public Item(String title, String description, String sellerName, String sellerPhone, Bitmap image) {

        this.title = title;
        this.description = description;
        this.sellerName = sellerName;
        this.sellerPhone = sellerPhone;
        this.image = image;

        this.buyerName = "";
        this.isSold = false;
    }


}
