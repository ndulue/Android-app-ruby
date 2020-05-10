package app.calcounterapp.com.ruby.Model;

import java.util.List;

public class UserModel {
    private String message;
    private Boolean success;
    private List<User> result;



    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<User> getResult() {
        return result;
    }

    public void setResult(List<User> result) {
        this.result = result;
    }


}
