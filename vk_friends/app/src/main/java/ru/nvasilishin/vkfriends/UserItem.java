package ru.nvasilishin.vkfriends;

import android.graphics.Bitmap;

/**
 * Created by n.vasilishin on 22.12.2015.
 */
public class UserItem {

    private long id;
    private Bitmap photoIcon;
    private String firstname;
    private String lastname;
    private boolean isOnline;

    public UserItem(long id, Bitmap photoIcon, String firstname, String lasttname, boolean isOnline){
        this.id = id;
        this.photoIcon = photoIcon;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isOnline = isOnline;
    }

    public UserItem(long id, String photoIcon, String firstname, String lasttname, boolean isOnline){
        this.id = id;
        //this.photoIcon = photoIcon;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isOnline = isOnline;
    }

    public long getId() {
        return id;
    }

    public Bitmap getPhotoIcon() {
        return photoIcon;
    }

    public String getName(){
        return firstname + " " + lastname;
    }

    public String getFirstname() {
        return firstname;
    }


    public String getLastname() {
        return lastname;
    }

    public boolean isOnline() {
        return isOnline;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Friend Name";
    }
}
