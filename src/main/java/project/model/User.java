package project.model;

public class User {
    private int user_id;
    private String username;
    private String password;
    private byte[] salt;
    private Boolean registered;

    public User(int user_id, String username, String password, byte[] salt, Boolean registered) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.registered = registered;
    }

    public User(String username, String password, byte[] salt, Boolean registered) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.registered = registered;
    }

    public User()
    {
        this.username="";
        this.password="";
        this.salt = null;
        this.registered = false;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(byte[] salt) {this.salt = salt;}

    public void setRegistered(Boolean registered) { this.registered = registered;}

    public int getUser_id() {return user_id;}

    public String getUsername() {return username;}

    public String getPassword() {return password;}

    public byte[] getSalt() { return salt;}

    public Boolean getRegistered() {return registered;}

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password=" + password + '\'' +
                ", salt=" + salt + '\'' +
                ", registered=" + registered +
                '}';
    }
}



