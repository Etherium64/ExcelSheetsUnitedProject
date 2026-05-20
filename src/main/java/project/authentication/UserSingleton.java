package project.authentication;

/**
 *  User Singleton class
 *  Stores the Username and User_ID for access across other controller in a UserSingleton Class
 *  Simulates an "Active" User across all the stages
 */
public final class UserSingleton {
    private int user_id;
    private String username;
    private final static UserSingleton INSTANCE = new UserSingleton();

    private UserSingleton() {};

    public static UserSingleton getInstance() { return INSTANCE;}

    public void setup(int user_id, String username)
    {
        this.user_id = user_id;
        this.username = username;
    }

    public int getUser_id() {return user_id;}

    public String getUsername() {return username;}

}
