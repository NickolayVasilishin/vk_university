package ru.nvasilishin.vkfriends.utils;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.nvasilishin.vkfriends.view.FriendListAdapter;

/**
 *  Using to load friends via VkSdk.
 *  Loads friends and fills given RecyclerView.
 *
 */
public class FriendListLoader implements Runnable{
    private static final String TAG = "FriendListLoaderTag";
    private UserItem[] mFriends;
    private volatile boolean isLoading = false;

    private RecyclerView mView;
    private Context mContext;

    /*
     * TODO
     * 1) Replace executeWithListener with executeSyncWithListener
     * 2) Move execution to background
     * 3) Write method getOrWait to synchronize execution and avoid NPE at mFriends variable
     * 4) Remove ugly fillView method
     * 5) Cache...
     */
    public FriendListLoader(RecyclerView view, Context context){
        mView = view;
        mContext = context;
    }


    public void loadAsync() {
        Log.d(TAG, "Starting async loading.");
        isLoading = true;
        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {

                new VKRequest("friends.get", VKParameters.from(VKApiConst.SORT, "hints", VKApiConst.FIELDS, "photo_100, online")).
                        executeSyncWithListener(new VKRequest.VKRequestListener() {
                            @Override
                            public void onComplete(VKResponse response) {
                                synchronized (mFriends) {
                                    parseResponse(response);
                                    isLoading = false;
                                    notifyAll();
                                }
                            }

                            @Override
                            public void onError(VKError error) {
                                synchronized (mFriends) {
                                    isLoading = false;
                                }
                                Log.e(TAG, "onError: " + error.toString());
                            }
                        });
            }
        });

    }

    @Deprecated
    public FriendListLoader load(){
        Log.d(TAG, "Building request");
        VKRequest request = new VKRequest("friends.get", VKParameters.from(VKApiConst.SORT, "hints", VKApiConst.FIELDS, "photo_100, online"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Log.d(TAG, "Response received.");
                parseResponse(response);
                fillView(mView, mContext);
            }
            @Override
            public void onError(VKError error) {
                Log.e(TAG, "onError: " + error.toString());
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                Log.d(TAG, "attempt failed: " + request.toString());
            }
        });
        Log.d(TAG, "Load completed.");
        return this;
    }

    private UserItem[] parseResponse(VKResponse vkResponse){
        try {
            JSONArray response = vkResponse.json.getJSONObject("response").getJSONArray("items");
            UserItem[] friends = new UserItem[response.length()];
            for (int i = 0; i < response.length(); i ++) {
                JSONObject item = response.getJSONObject(i);
                friends[i] = new UserItem(item.getLong("id"), item.getString("photo_100"), item.getString("first_name"), item.getString("last_name"), item.getInt("online") == 1);
            }
            mFriends = friends;
            Log.d(TAG, "Parsed " + friends.length + " users. The last user is " + friends[friends.length-1]);

        } catch (JSONException e) {
            Log.e(TAG, "JSON exception", e);
            e.printStackTrace();
        }
        //
        return mFriends;
    }

    public UserItem[] getFriendsOrWait(){
        synchronized (mFriends) {
            if (mFriends == null && isLoading)
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            if (mFriends == null && !isLoading)
                throw new IllegalStateException("Friends have not been loaded yet. Call load before getting friends");
            if
        }
        return mFriends;
    }

    public FriendListLoader fillView(RecyclerView recyclerView, Context context) {
        recyclerView.setAdapter(new FriendListAdapter(mFriends, context));
        return this;
    }

    @Override
    public void run() {

    }
}
