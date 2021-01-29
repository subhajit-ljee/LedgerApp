package com.sourav.ledgerproject.profile.model;

public class AccountHolder {

    private String name;
    private String address;
    private String country;
    private String pincode;
    private String state;
    private String type;

    private String client_id;
    private String opening_balance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getOpening_balance() {
        return opening_balance;
    }

    public void setOpening_balance(String opening_balance) {
        this.opening_balance = opening_balance;
    }


    @Override
    public String toString() {
        return "AccountHolder{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", pincode='" + pincode + '\'' +
                ", state='" + state + '\'' +
                ", type='" + type + '\'' +
                ", client_id='" + client_id + '\'' +
                ", opening_balance='" + opening_balance + '\'' +
                '}';
    }
}
