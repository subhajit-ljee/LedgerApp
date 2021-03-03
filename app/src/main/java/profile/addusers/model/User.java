package profile.addusers.model;

import java.util.Objects;

public class User {
    private String id;
    private String name;
    private String email;
    private String messeging_token;

    public User() {
    }

    public User(String id, String name, String email, String messeging_token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.messeging_token = messeging_token;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) &&
                Objects.equals(messeging_token, user.messeging_token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, messeging_token);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", messeging_token='" + messeging_token + '\'' +
                '}';
    }
}
