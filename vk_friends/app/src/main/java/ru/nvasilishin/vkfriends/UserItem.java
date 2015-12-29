package ru.nvasilishin.vkfriends;

/**
 * Created by n.vasilishin on 22.12.2015.
 */
public class UserItem {

    private final long id;
    private String photoIcon;
    private String firstname;
    private String lastname;
    private boolean isOnline;

    public UserItem(long id, String photoIcon, String firstname, String lastname, boolean isOnline){
        this.id = id;
        this.photoIcon = photoIcon;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isOnline = isOnline;
    }

    public long getId() {
        return id;
    }

    public String getPhotoIcon() {
        return photoIcon;
    }

    public String getName(){
        return getFirstname() + " " + getLastname();
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
        return "" + getId() + " " + getName() + " " + isOnline();
    }
}
