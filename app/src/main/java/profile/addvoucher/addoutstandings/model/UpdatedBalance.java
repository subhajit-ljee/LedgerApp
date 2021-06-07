package profile.addvoucher.addoutstandings.model;

public class UpdatedBalance {

    private String id;
    private String ledger_id;
    private String client_id;
    private String outstanding_balance;
    private String final_balance;

    public UpdatedBalance() {
    }

    public UpdatedBalance(String id, String ledger_id, String client_id, String outstanding_balance, String final_balance) {
        this.id = id;
        this.ledger_id = ledger_id;
        this.client_id = client_id;
        this.outstanding_balance = outstanding_balance;
        this.final_balance = final_balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLedger_id() {
        return ledger_id;
    }

    public void setLedger_id(String ledger_id) {
        this.ledger_id = ledger_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getOutstanding_balance() {
        return outstanding_balance;
    }

    public void setOutstanding_balance(String outstanding_balance) {
        this.outstanding_balance = outstanding_balance;
    }

    public String getFinal_balance() {
        return final_balance;
    }

    public void setFinal_balance(String final_balance) {
        this.final_balance = final_balance;
    }

    @Override
    public String toString() {
        return "UpdatedBalance{" +
                "id='" + id + '\'' +
                ", ledger_id='" + ledger_id + '\'' +
                ", client_id='" + client_id + '\'' +
                ", outstanding_balance='" + outstanding_balance + '\'' +
                ", final_balance='" + final_balance + '\'' +
                '}';
    }
}
