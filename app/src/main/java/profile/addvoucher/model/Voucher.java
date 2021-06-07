package profile.addvoucher.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Voucher implements Parcelable {

    private String id;
    private String name;
    private String user_id;
    private String client_id;
    private String ledger_id;
    private String voucher_number;
    private String type;
    private String amount;
    private String timestamp;
    private boolean added;
    private String notifyfrom;

    public Voucher(){}

    public Voucher(String id, String name, String user_id, String client_id, String ledger_id, String voucher_number, String type, String amount, String timestamp, boolean added, String notifyfrom) {
        this.id = id;
        this.name = name;
        this.user_id = user_id;
        this.client_id = client_id;
        this.ledger_id = ledger_id;
        this.voucher_number = voucher_number;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.added = added;
        this.notifyfrom = notifyfrom;
    }

    protected Voucher(Parcel in) {
        id = in.readString();
        name = in.readString();
        user_id = in.readString();
        client_id = in.readString();
        ledger_id = in.readString();
        voucher_number = in.readString();
        type = in.readString();
        amount = in.readString();
        timestamp = in.readString();
        added = in.readByte() != 0;
    }

    public static final Creator<Voucher> CREATOR = new Creator<Voucher>() {
        @Override
        public Voucher createFromParcel(Parcel in) {
            return new Voucher(in);
        }

        @Override
        public Voucher[] newArray(int size) {
            return new Voucher[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getLedger_id() {
        return ledger_id;
    }

    public void setLedger_id(String ledger_id) {
        this.ledger_id = ledger_id;
    }

    public String getVoucher_number() {
        return voucher_number;
    }

    public void setVoucher_number(String voucher_number) {
        this.voucher_number = voucher_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public String getNotifyfrom() {
        return notifyfrom;
    }

    public void setNotifyfrom(String notifyfrom) {
        this.notifyfrom = notifyfrom;
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", client_id='" + client_id + '\'' +
                ", ledger_id='" + ledger_id + '\'' +
                ", voucher_number='" + voucher_number + '\'' +
                ", type='" + type + '\'' +
                ", amount='" + amount + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", added=" + added +
                ", notifyfrom='" + notifyfrom + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(client_id);
        dest.writeString(ledger_id);
        dest.writeString(voucher_number);
        dest.writeString(type);
        dest.writeString(amount);
        dest.writeString(timestamp);
        dest.writeByte((byte) (added ? 1 : 0));
        dest.writeString(notifyfrom);
    }
}
