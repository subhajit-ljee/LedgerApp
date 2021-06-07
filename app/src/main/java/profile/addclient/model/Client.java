package profile.addclient.model;

import java.util.Objects;

public class Client {

    private String id;
    private String user_id;
    private String client_name;
    private String client_email;
    private String messeging_token;

    public Client(){}

    public Client(String id, String user_id, String client_name, String client_email, String messeging_token) {
        this.id = id;
        this.user_id = user_id;
        this.client_name = client_name;
        this.client_email = client_email;
        this.messeging_token = messeging_token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_email() {
        return client_email;
    }

    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }

    public String getMesseging_token() {
        return messeging_token;
    }

    public void setMesseging_token(String messeging_token) {
        this.messeging_token = messeging_token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) &&
                Objects.equals(user_id, client.user_id) &&
                Objects.equals(client_name, client.client_name) &&
                Objects.equals(client_email, client.client_email) &&
                Objects.equals(messeging_token, client.messeging_token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user_id, client_name, client_email, messeging_token);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", client_name='" + client_name + '\'' +
                ", client_email='" + client_email + '\'' +
                ", messeging_token='" + messeging_token + '\'' +
                '}';
    }
}
