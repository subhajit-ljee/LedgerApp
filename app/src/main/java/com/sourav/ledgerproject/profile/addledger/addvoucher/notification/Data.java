package com.sourav.ledgerproject.profile.addledger.addvoucher.notification;

public class Data {

    private String Title;
    private String Message;

    public Data(String title, String message) {
        Title = title;
        Message = message;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
