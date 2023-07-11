package com.example.akmarketplace;

public class Notification {
    private String buyerEmail;
    private String sellerEmail;

    public Notification () {

    }

    public Notification (String buyerEmail, String sellerEmail) {
        this.buyerEmail = buyerEmail;
        this.sellerEmail = sellerEmail;
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
}
