package profile.addclient.model;

import java.util.Objects;

public class Client {

    private String id;
    private String client_name;
    private String client_email;
    private String messeging_token;

    public Client(){}

    public Client(String id, String client_name, String client_email, String messeging_token) {
        this.id = id;
        this.client_name = client_name;
        this.client_email = client_email;
        this.messeging_token = messeging_token;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
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
                Objects.equals(client_name, client.client_name) &&
                Objects.equals(client_email, client.client_email) &&
                Objects.equals(messeging_token, client.messeging_token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client_name, client_email, messeging_token);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", client_name='" + client_name + '\'' +
                ", client_email='" + client_email + '\'' +
                ", messaging_token='" + messeging_token + '\'' +
                '}';
    }
}
