package com.sourav.ledgerproject.profile.addledger.model;

public class DeleteClientModel {

    private String client_id;
    private String client_name;

    public DeleteClientModel(String client_id, String client_name) {
        this.client_id = client_id;
        this.client_name = client_name;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    @Override
    public String toString() {
        return "DeleteVoucherModel{" +
                "client_id='" + client_id + '\'' +
                ", client_name='" + client_name + '\'' +
                '}';
    }
}
