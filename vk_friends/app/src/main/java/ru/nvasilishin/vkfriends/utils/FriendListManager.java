package ru.nvasilishin.vkfriends.utils;

import android.util.LruCache;

/**
 * Created by Nick on 27.01.2016.
 */
public class FriendListManager {
    private final int MAX_CACHE_COUNT = 150;
    private static FriendListManager INSTANCE = null;
    private LruCache<Long, UserItem> mCache;
    private FriendListLoader mLoader;

    private FriendListManager() {
        mCache = new LruCache<>(MAX_CACHE_COUNT);
        mLoader = new FriendListLoader();
    }

    synchronized public static FriendListManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new FriendListManager();
        return INSTANCE;
    }

    public FriendListManager initialize(){
        return this;
    }

    private void fillCache(){
        mLoader.load();
    }

}
