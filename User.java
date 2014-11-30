/**
 * Created by James on 11/30/2014.
 */
public class User {
    public String name;
    public String key;
    public boolean authenticated;

    public User(String name, String key)
    {
        this.name = name;
        this.key = key;
        this.authenticated = false;
    }
}
