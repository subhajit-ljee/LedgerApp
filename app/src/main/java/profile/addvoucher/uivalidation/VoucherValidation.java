package profile.addvoucher.uivalidation;

import profile.addvoucher.model.Voucher;

public class VoucherValidation {

    private static final String TAG = "VoucherValidation";
    private Voucher voucher;
    public VoucherValidation(Voucher voucher){
        this.voucher = voucher;
    }

    public String isVoucherTypeValid(){
        String err = "";
        if(voucher.getLedger_id().isEmpty() || voucher.getClient_id().isEmpty() || voucher.getId().isEmpty()){
            err = "Voucher is invalid";
        }

        if(voucher.getType().isEmpty() || !(voucher.getType().equals("Payment") || voucher.getType().equals("Receipt"))){
            err = "Please enter a valid type";
        }

        return err;
    }

    public String isAmountValid(){
        String openingError = "";
        String amount;
        amount = voucher.getAmount();
        if(amount.isEmpty()){
            openingError = "Please enter an amount !";
        }

        if(amount.matches("[0]+[0-9]+")){
            openingError = "you can't put leading zeroes";
        }else if(amount.matches("[a-zA-Z]+")){
            openingError = "you can't put letters or words in voucher amount !";
        }

        return openingError;
    }
}
