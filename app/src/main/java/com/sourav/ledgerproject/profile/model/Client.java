package com.sourav.ledgerproject.profile.model;

public class Client {

    private String id;
    private String client_name;
    private String client_email;
    private String client_id;

    public Client(String id, String client_name, String client_email, String client_id) {
        this.id = id;
        this.client_name = client_name;
        this.client_email = client_email;
        this.client_id = client_id;
    }

    public String getId() {
        return id;
    }

    public String getClient_name() {
        return client_name;
    }

    public String getClient_email() {
        return client_email;
    }

    public String getClient_id() {
        return client_id;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", client_name='" + client_name + '\'' +
                ", client_email='" + client_email + '\'' +
                ", client_id='" + client_id + '\'' +
                '}';
    }
}
