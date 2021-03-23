package profile.addvoucher.notification.model;

public class Data {

    private String title;
    private String message;
    private String vid;
    private String clientid;
    private String ledgerid;
    private String ledgername;
    private String opening_balance;
    private String type;
    private String notifyfrom;
    private String activity;

    public Data(String title, String message,String vid, String clientid, String ledgerid, String ledgername, String opening_balance, String type, String notifyfrom, String activity) {
        this.title = title;
        this.message = message;
        this.vid = vid;
        this.clientid = clientid;
        this.ledgerid = ledgerid;
        this.ledgername = ledgername;
        this.opening_balance = opening_balance;
        this.type = type;
        this.notifyfrom = notifyfrom;
        this.activity = activity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
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

    public String getLedgername() {
        return ledgername;
    }

    public void setLedgername(String ledgername) {
        this.ledgername = ledgername;
    }

    public String getOpening_balance() {
        return opening_balance;
    }

    public void setOpening_balance(String opening_balance) {
        this.opening_balance = opening_balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotifyfrom() {
        return notifyfrom;
    }

    public void setNotifyfrom(String notifyfrom) {
        this.notifyfrom = notifyfrom;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
