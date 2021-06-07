package profile.addvoucher.addoutstandings.model;

public class Balance {

    private String id;
    private String userid;
    private String clientid;
    private String ledgerid;
    private String bill_balance;

    public Balance() {
    }

    public Balance(String id, String userid, String clientid, String ledgerid, String bill_balance) {
        this.id = id;
        this.userid = userid;
        this.clientid = clientid;
        this.ledgerid = ledgerid;
        this.bill_balance = bill_balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getLedgerid() {
        return ledgerid;
    }

    public void setLedgerid(String ledgerid) {
        this.ledgerid = ledgerid;
    }

    public String getBill_balance() {
        return bill_balance;
    }

    public void setBill_balance(String bill_balance) {
        this.bill_balance = bill_balance;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", clientid='" + clientid + '\'' +
                ", ledgerid='" + ledgerid + '\'' +
                ", bill_balance='" + bill_balance + '\'' +
                '}';
    }
}
