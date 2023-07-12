package com.example.akmarketplace;

public class NotificationAK {
    private String buyerEmail;
    private String sellerEmail;
    private Long itemId;

    private String buyerName;
    private String itemName;

    public NotificationAK() {

    }

    public NotificationAK(String buyerEmail, String sellerEmail, String buyerName, String itemName, Long itemId) {
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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
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
