package profile.addledger.uivalidation;

import android.util.Log;

import profile.addledger.model.Ledger;

public class LedgerValidation {

    private static final String TAG = "LedgerValidation";
    private Ledger ledger;

    public LedgerValidation(Ledger ledger){
        this.ledger = ledger;
        Log.d(TAG, "LedgerValidation: Ledger " + ledger);
    }

    public String isIdValid(){
        String err = "";
        if(ledger.getId().isEmpty()){
            err = "id not found";
        }else if(ledger.getClient_id().isEmpty()){
            err = "client id not found";
        }

        return err;
    }

    public String isNameValid(){
        String nameError = "";
        if(ledger.getAccount_name().isEmpty()){
            nameError = "You must give a name !";
        }else if(!ledger.getAccount_name().matches("[a-zA-Z]{3,}+[\\s]*[a-zA-Z]+")){
            nameError = "You must specify a valid name";
        }

        return nameError;
    }

    public String isTypeValid(){
        String typeError = "";
        if(ledger.getAccount_type().isEmpty() || ledger.getAccount_type() == null){
            typeError = "please choose a type";
        }else if(!(ledger.getAccount_type().equals("Debit") || ledger.getAccount_type().equals("Credit"))){
            typeError = "Type is invalid";
        }

        return typeError;
    }

    public String isAddressValid(){
        String addressError = "";
        if(ledger.getAccount_address().isEmpty() || ledger.getAccount_address() == null){
            addressError = "You must give a address !";
        }

        return addressError;
    }

    public String isCountryValid(){
        String countryError = "";
        if(ledger.getAccount_country().isEmpty() || ledger.getAccount_country() == null){
            countryError = "You must specify a country !";
        }

        return countryError;
    }

    public String isStateValid(){
        String stateError = "";
        if(ledger.getAccount_state().isEmpty() || ledger.getAccount_state() == null){
            stateError = "You must specify a state !";
        }

        return stateError;
    }

    public String isPincodeValid(){
        String pincodeError = "";
        if(ledger.getAccount_pincode().isEmpty() || ledger.getAccount_pincode() == null){
            pincodeError = "You must specify a state !";
        }

        return pincodeError;
    }

    public String isOpeningValid(){
        String openingError = "";
        String opening_balance;
        opening_balance = ledger.getOpening_balance();
        if(opening_balance.isEmpty()){
            openingError = "You must specify an amount !";
        }

        if(opening_balance.matches("[0]+[0-9]+")){
            openingError = "you can't put leading zeroes";
        }else if(opening_balance.matches("[a-zA-Z]+")){
            openingError = "you can't put letters or words in opening balance";
        }

        return openingError;
    }
}
