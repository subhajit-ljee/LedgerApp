package com.sourav.ledgerproject.profile.addledger.model;

public class Voucher {

    private String id;
    private String account_name;
    private String account_type;
    private String account_address;
    private String account_country;
    private String account_state;
    private String account_pincode;
    private String opening_balance;

    public Voucher(String id, String account_name, String account_type, String account_address, String account_country, String account_state, String account_pincode, String opening_balance) {
        this.id = id;
        this.account_name = account_name;
        this.account_type = account_type;
        this.account_address = account_address;
        this.account_country = account_country;
        this.account_state = account_state;
        this.account_pincode = account_pincode;
        this.opening_balance = opening_balance;
    }

    public String getId() {
        return id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public String getAccount_type() {
        return account_type;
    }

    public String getAccount_address() {
        return account_address;
    }

    public String getAccount_country() {
        return account_country;
    }

    public String getAccount_state() {
        return account_state;
    }

    public String getAccount_pincode() {
        return account_pincode;
    }

    public String getOpening_balance() {
        return opening_balance;
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "id='" + id + '\'' +
                ", account_name='" + account_name + '\'' +
                ", account_type='" + account_type + '\'' +
                ", account_address='" + account_address + '\'' +
                ", account_country='" + account_country + '\'' +
                ", account_state='" + account_state + '\'' +
                ", account_pincode='" + account_pincode + '\'' +
                ", opening_balance='" + opening_balance + '\'' +
                '}';
    }
}
