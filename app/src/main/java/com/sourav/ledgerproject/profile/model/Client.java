package com.sourav.ledgerproject.profile.model;

public class Client {

    private String user_id;
    private String client_name;
    private String client_email;
    private String client_id;

    public Client(){}

    public Client(String user_id, String client_name, String client_email, String client_id) {
        this.user_id = user_id;
        this.client_name = client_name;
        this.client_email = client_email;
        this.client_id = client_id;
    }

    public String getUser_id() {
        return user_id;
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
                "user_id='" + user_id + '\'' +
                ", client_name='" + client_name + '\'' +
                ", client_email='" + client_email + '\'' +
                ", client_id='" + client_id + '\'' +
                '}';
    }
}
