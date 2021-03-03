package profile.addledger.model;


public class BankDetails {

    private String pan_or_it_no;
    private String bank_name;
    private String bank_ifsc;
    private String account_number;
    private String branch_name;

    public BankDetails(){}

    public BankDetails(String pan_or_it_no, String bank_name, String bank_ifsc, String account_number, String branch_name) {
        this.pan_or_it_no = pan_or_it_no;
        this.bank_name = bank_name;
        this.bank_ifsc = bank_ifsc;
        this.account_number = account_number;
        this.branch_name = branch_name;
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

    public void setPan_or_it_no(String pan_or_it_no) {
        this.pan_or_it_no = pan_or_it_no;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public void setBank_ifsc(String bank_ifsc) {
        this.bank_ifsc = bank_ifsc;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    @Override
    public String toString() {
        return "BankDetails{" +
                ", pan_or_it_no='" + pan_or_it_no + '\'' +
                ", bank_name='" + bank_name + '\'' +
                ", bank_ifsc='" + bank_ifsc + '\'' +
                ", account_number='" + account_number + '\'' +
                ", branch_name='" + branch_name + '\'' +
                '}';
    }
}
