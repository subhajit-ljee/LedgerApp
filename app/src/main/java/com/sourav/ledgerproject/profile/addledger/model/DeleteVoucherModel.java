package com.sourav.ledgerproject.profile.addledger.model;

public class DeleteVoucherModel {

    private String voucher_name;
    private String voucher_date;
    private String voucher_type;
    private String voucher_amount;

    public DeleteVoucherModel(String voucher_name, String voucher_date, String voucher_type, String voucher_amount) {
        this.voucher_name = voucher_name;
        this.voucher_date = voucher_date;
        this.voucher_type = voucher_type;
        this.voucher_amount = voucher_amount;
    }

    public String getVoucher_name() {
        return voucher_name;
    }

    public void setVoucher_name(String voucher_name) {
        this.voucher_name = voucher_name;
    }

    public String getVoucher_date() {
        return voucher_date;
    }

    public void setVoucher_date(String voucher_date) {
        this.voucher_date = voucher_date;
    }

    public String getVoucher_type() {
        return voucher_type;
    }

    public void setVoucher_type(String voucher_type) {
        this.voucher_type = voucher_type;
    }

    public String getVoucher_amount() {
        return voucher_amount;
    }

    public void setVoucher_amount(String voucher_amount) {
        this.voucher_amount = voucher_amount;
    }

    @Override
    public String toString() {
        return "DeleteVoucherModel{" +
                "voucher_name='" + voucher_name + '\'' +
                ", voucher_date='" + voucher_date + '\'' +
                ", voucher_type='" + voucher_type + '\'' +
                ", voucher_amount='" + voucher_amount + '\'' +
                '}';
    }
}
