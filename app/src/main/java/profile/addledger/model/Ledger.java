package profile.addledger.model;

public class Ledger {

    private String id;
    private String client_id;
    private String account_name;
    private String account_type;
    private String account_address;
    private String account_country;
    private String account_state;
    private String account_pincode;
    private String opening_balance;
    private String timestamp;

    private BankDetails bankDetails;

    public Ledger(){}

    public Ledger(String id, String client_id, String account_name, String account_type, String account_address, String account_country, String account_state, String account_pincode, String opening_balance, String timestamp, BankDetails bankDetails) {
        this.id = id;
        this.client_id = client_id;
        this.account_name = account_name;
        this.account_type = account_type;
        this.account_address = account_address;
        this.account_country = account_country;
        this.account_state = account_state;
        this.account_pincode = account_pincode;
        this.opening_balance = opening_balance;
        this.timestamp = timestamp;
        this.bankDetails = bankDetails;
    }

    public String getId(){ return id; }

    public String getClient_id() {
        return client_id;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public void setAccount_address(String account_address) {
        this.account_address = account_address;
    }

    public void setAccount_country(String account_country) {
        this.account_country = account_country;
    }

    public void setAccount_state(String account_state) {
        this.account_state = account_state;
    }

    public void setAccount_pincode(String account_pincode) {
        this.account_pincode = account_pincode;
    }

    public void setOpening_balance(String opening_balance) {
        this.opening_balance = opening_balance;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public BankDetails getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(BankDetails bankDetails) {
        this.bankDetails = bankDetails;
    }

    @Override
    public String toString() {
        return "Ledger{" +
                "id='" + id + '\'' +
                "client_id='" + client_id + '\'' +
                ", account_name='" + account_name + '\'' +
                ", account_type='" + account_type + '\'' +
                ", account_address='" + account_address + '\'' +
                ", account_country='" + account_country + '\'' +
                ", account_state='" + account_state + '\'' +
                ", account_pincode='" + account_pincode + '\'' +
                ", opening_balance='" + opening_balance + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", bankDetails=" + bankDetails +
                '}';
    }
}
