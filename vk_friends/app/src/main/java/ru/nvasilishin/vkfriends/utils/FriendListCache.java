package ru.nvasilishin.vkfriends.utils;

import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Nick on 27.01.2016.
 */
public class FriendListCache {
    private ConcurrentHashMap<Long, UserItem> cache;
    private final int MAX_CACHE_SIZE = 110;
    private final long TIMEOUT = 10*60*1000;
    private Date lastUpdate;
    private int currentCacheSize;

    public UserItem fetch(Long id){
        return cache.get(id);
    }

    public UserItem[] fetchAll(){
        UserItem[] users = new UserItem[cache.size()];
        Enumeration<UserItem> elements = cache.elements();
        int i = 0;
        while (elements.hasMoreElements()) {
            users[i++] = elements.nextElement();
        }
        return users;
    }

    public void update(){}
}
