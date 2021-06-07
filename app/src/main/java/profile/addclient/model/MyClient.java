package profile.addclient.model;

public class MyClient {
    private String my_client_id;
    private String my_client_name;
    private String my_client_email;

    public MyClient() {
    }

    public MyClient(String my_client_id, String my_client_name, String my_client_email) {
        this.my_client_id = my_client_id;
        this.my_client_name = my_client_name;
        this.my_client_email = my_client_email;
    }

    public String getMy_client_id() {
        return my_client_id;
    }

    public void setMy_client_id(String my_client_id) {
        this.my_client_id = my_client_id;
    }

    public String getMy_client_name() {
        return my_client_name;
    }

    public void setMy_client_name(String my_client_name) {
        this.my_client_name = my_client_name;
    }

    public String getMy_client_email() {
        return my_client_email;
    }

    public void setMy_client_email(String my_client_email) {
        this.my_client_email = my_client_email;
    }
}
