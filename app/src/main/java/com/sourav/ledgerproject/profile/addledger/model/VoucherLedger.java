package com.sourav.ledgerproject.profile.addledger.model;

public class VoucherLedger {

    private String creation_time;
    private String voucher_mode;
    private String amount;

    public VoucherLedger(String creation_time, String voucher_mode, String amount) {
        this.creation_time = creation_time;
        this.voucher_mode = voucher_mode;
        this.amount = amount;
    }

    public String getCreation_time() {
        return creation_time;
    }

    public String getVoucher_mode() {
        return voucher_mode;
    }

    public String getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "VoucherLedger{" +
                "creation_time='" + creation_time + '\'' +
                ", voucher_mode='" + voucher_mode + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
