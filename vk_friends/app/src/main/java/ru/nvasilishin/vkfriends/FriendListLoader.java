package ru.nvasilishin.vkfriends;

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
    private JSONArray response;
    UserItem[] friends;

    public FriendListLoader load(){
        VKRequest request = new VKRequest("users.get", VKParameters.from(VKApiConst.SORT, "hints", VKApiConst.FIELDS, "photo_100, online"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                    Log.d(TAG, FriendListLoader.this.response.toString());
                    parseResponse(response);
            }
            @Override
            public void onError(VKError error) {
                //Do error stuff
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                //I don't really believe in progress
            }
        });
        return this;
    }

    private void parseResponse(VKResponse VKResponse){
        try {
            JSONArray response = VKResponse.json.getJSONObject("response").getJSONArray("items");
            UserItem[] friends = new UserItem[response.length()];
            for(int i = 0; i < response.length(); i ++) {
                JSONObject item = response.getJSONObject(i);
                UserItem user = new UserItem(item.getLong("id"), item.getString("photo_100"), item.getString("firstname"), item.getString("lastname"), item.getInt("online") == 1);
                friends[i] = user;
            }
            this.friends = friends;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public UserItem[] getFriends(){
        return friends;
    }
}
