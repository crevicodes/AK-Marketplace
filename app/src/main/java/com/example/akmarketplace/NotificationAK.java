package com.example.akmarketplace;

public class NotificationAK { //notification class for retrieving from database
    private String buyerEmail;
    private String sellerEmail;
    private String itemId;

    private String buyerName;
    private String itemName;

    public NotificationAK() {

    }

    public NotificationAK(String buyerEmail, String sellerEmail, String buyerName, String itemName, String itemId) {
        this.buyerEmail = buyerEmail;
        this.sellerEmail = sellerEmail;
        this.itemId = itemId;
        this.buyerName = buyerName;
        this.itemName = itemName;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
