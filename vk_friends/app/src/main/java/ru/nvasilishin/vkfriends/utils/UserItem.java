package ru.nvasilishin.vkfriends.utils;

/**
 * Created by n.vasilishin on 22.12.2015.
 */
public class UserItem {

    private final long mId;
    private String mPhotoIcon;
    private String mFirstname;
    private String mLastname;
    private boolean mOnline;

    public UserItem(long id, String photoIcon, String firstname, String lastname, boolean isOnline){
        mId = id;
        mPhotoIcon = photoIcon;
        mFirstname = firstname;
        mLastname = lastname;
        mOnline = isOnline;
    }

    public long getId() {
        return mId;
    }

    public String getPhotoIcon() {
        return mPhotoIcon;
    }

    public String getName(){
        return getmFirstname() + " " + getLastname();
    }

    public String getmFirstname() {
        return mFirstname;
    }


    public String getLastname() {
        return mLastname;
    }

    public boolean isOnline() {
        return mOnline;
    }

    @Override
    public int hashCode() {
        return (int) (mId ^ (mId >>> 32));
    }

    @Override
    public String toString() {
        return "" + getId() + " " + getName() + " " + isOnline();
    }
}
