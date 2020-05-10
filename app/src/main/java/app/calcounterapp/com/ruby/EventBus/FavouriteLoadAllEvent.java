package app.calcounterapp.com.ruby.EventBus;

import java.util.List;

import app.calcounterapp.com.ruby.Model.Favourite;

public class FavouriteLoadAllEvent {
    private boolean success;
    private List<Favourite> favouriteList;
    private String message;

    public FavouriteLoadAllEvent(boolean success, List<Favourite> favouriteList) {
        this.success = success;
        this.favouriteList = favouriteList;
    }

    public FavouriteLoadAllEvent(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Favourite> getFavouriteList() {
        return favouriteList;
    }

    public void setFavouriteList(List<Favourite> favouriteList) {
        this.favouriteList = favouriteList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
