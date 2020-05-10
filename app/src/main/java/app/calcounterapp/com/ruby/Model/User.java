package app.calcounterapp.com.ruby.Model;

public class User {
    private String apiKey;
    private String name;
    private String email;
    private String password;

    public User(String apiKey, String name, String email, String password) {
        this.apiKey = apiKey;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getApiKey(){
        return apiKey;
    }
    public void setApiKey(String apiKey){
        this.apiKey = apiKey;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
