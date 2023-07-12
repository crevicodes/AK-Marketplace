package com.example.akmarketplace;

public class AccountUser {
    private String fullname;
    private String email;
    private String phone;

    public AccountUser(String email, String fullname, String phone) {
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
    }

    public AccountUser() {

    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

//for getting "users" collection from firestore database