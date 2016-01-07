package ru.nvasilishin.vkfriends;

import android.content.Context;
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

/**
 * Created by n.vasilishin on 22.12.2015.
 */
public class FriendListLoader {
    private static final String TAG = "FriendListLoaderTag";
    UserItem[] friends;

    public FriendListLoader load(){
        Log.d(TAG, "Building request");
        VKRequest request = new VKRequest("friends.get", VKParameters.from(VKApiConst.SORT, "hints", VKApiConst.FIELDS, "photo_100, online"));
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Log.d(TAG, response.toString());
                parseResponse(response);
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

    //THIS
    public FriendListLoader loadToAdapter(RecyclerView.Adapter adapter, Context context){
        load();
        adapter = new FriendListAdapter(getFriends(), context);
        return this;
    }

    private void parseResponse(VKResponse VKResponse){
        try {
            JSONArray response = VKResponse.json.getJSONObject("response").getJSONArray("items");
            UserItem[] friends = new UserItem[response.length()];
            for (int i = 0; i < response.length(); i ++) {
                JSONObject item = response.getJSONObject(i);
                UserItem user = new UserItem(item.getLong("id"), item.getString("photo_100"), item.getString("first_name"), item.getString("last_name"), item.getInt("online") == 1);
                friends[i] = user;
                Log.d(TAG, "Parsed user: " + user);
            }
            this.friends = friends;
        } catch (JSONException e) {
            Log.e(TAG, "JSON exception", e);
            e.printStackTrace();
        }
    }

    public UserItem[] getFriends(){
        return friends;
    }
}
