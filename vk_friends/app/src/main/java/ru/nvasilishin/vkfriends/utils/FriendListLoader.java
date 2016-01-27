package ru.nvasilishin.vkfriends.utils;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LruCache;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.NoSuchElementException;

/**
 *  Using for asynchronous loading friends via VkSdk and AsyncTask.
 *
 */
public class FriendListLoader extends AsyncTask<LruCache,Void ,Void>{
    private static final String TAG = "FriendListLoaderTag";
    //TODO Replace with cache?
    private UserItem[] mFriends;
    //Using to indicate if data is loading. Otherwise loader state must be checked somehow.
    private volatile boolean isLoading;
    private Object mLock;

    /*
     * TODO
     * 1) Cache...
     */
    public FriendListLoader(){
        mLock = new Object();
        mFriends = new UserItem[0];
        isLoading = false;
    }

    @Override
    protected Void doInBackground(LruCache... params) {
        loadSync();
        //params[0].
        return null;
    }

    /**
     * Requests FriendList via VKSdk asynchronously. Executes request in background thread and
     * stores result at <code>mFriends</code> variable. Use with <code>getOrWait</code>.
     * @return
     */
    public FriendListLoader load() {
        Log.d(TAG, "Starting async loading.");
        isLoading = true;
        execute();
        return this;
    }

    /**
     * private method based on <code>executeSyncWithListener</code>. This allows to control
     * loading state and avoid NPE when accessing to <code>mFriends</code> before response received.
     */
    private void loadSync(){
        Log.d(TAG, "Loading data in background");
        VKRequest request = new VKRequest("friends.get", VKParameters.from(VKApiConst.SORT, "hints", VKApiConst.FIELDS, "photo_100, online"));
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Log.d(TAG, "Response received.");
                UserItem[] friends = parseResponse(response);
                synchronized (mLock) {
                    mFriends = friends;
                    isLoading = false;
                    mLock.notifyAll();
                }
            }
            @Override
            public void onError(VKError error) {
                synchronized (mLock) {
                    isLoading = false;
                    mLock.notifyAll();
                }
                Log.e(TAG, "onError: " + error.toString());
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                Log.d(TAG, "attempt failed: " + request.toString());
            }
        });
        Log.d(TAG, "Load completed.");
    }

    @Nullable
    private UserItem[] parseResponse(VKResponse vkResponse){
        try {
            JSONArray response = vkResponse.json.getJSONObject("response").getJSONArray("items");
            UserItem[] friends = new UserItem[response.length()];
            for (int i = 0; i < response.length(); i ++) {
                JSONObject item = response.getJSONObject(i);
                friends[i] = new UserItem(item.getLong("id"), item.getString("photo_100"), item.getString("first_name"), item.getString("last_name"), item.getInt("online") == 1);
            }
            Log.d(TAG, "Parsed " + friends.length + " users. The last user is " + friends[friends.length-1]);
            return friends;
        } catch (JSONException e) {
            Log.e(TAG, "JSON exception", e);
            e.printStackTrace();
        }
        //TODO
        return null;
    }

    /**
     * Return friends or wait when request will be finished.
     * @return mFriends. May be null (after bad parsing or error) or empty.
     */
    public UserItem[] getOrWait(){
        Log.d(TAG, "Getting friends");
        /*
        * TODO check on null -> illegal state
        * think about other states
        * where to handle errors?
        */
        synchronized (mLock){
            Log.d(TAG, "Data is" + (isLoading ? "loading" : "not loading"));
            if(isLoading)
                try {
                    mLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
        return mFriends;
    }

}