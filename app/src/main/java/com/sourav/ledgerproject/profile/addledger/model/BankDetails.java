package com.sourav.ledgerproject.profile.addledger.model;


public class BankDetails {

    private String id;
    private String pan_or_it_no;
    private String bank_name;
    private String bank_ifsc;
    private String account_number;
    private String branch_name;

    public BankDetails(String id, String pan_or_it_no, String bank_name, String bank_ifsc, String account_number, String branch_name) {
        this.id = id;
        this.pan_or_it_no = pan_or_it_no;
        this.bank_name = bank_name;
        this.bank_ifsc = bank_ifsc;
        this.account_number = account_number;
        this.branch_name = branch_name;
    }

    public String getId() {
        return id;
    }

    public String getPan_or_it_no() {
        return pan_or_it_no;
    }

    public String getBank_name() {
        return bank_name;
    }

    public String getBank_ifsc() {
        return bank_ifsc;
    }

    public String getAccount_number() {
        return account_number;
    }

    public String getBranch_name() {
        return branch_name;
    }

    @Override
    public String toString() {
        return "BankDetails{" +
                "id='" + id + '\'' +
                ", pan_or_it_no='" + pan_or_it_no + '\'' +
                ", bank_name='" + bank_name + '\'' +
                ", bank_ifsc='" + bank_ifsc + '\'' +
                ", account_number='" + account_number + '\'' +
                ", branch_name='" + branch_name + '\'' +
                '}';
    }
}
