package ru.nvasilishin.vkfriends;

import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by n.vasilishin on 22.12.2015.
 */
public class FriendListLoader {
    private static final String TAG = "FriendListLoaderTag";
    private JSONArray response;

    public UserItem[] getFriends(){
        ArrayList<UserItem> friends = new ArrayList<>();
        VKRequest request = new VKRequest("users.get", VKParameters.from(VKApiConst.USER_IDS, user_ids_for_req, VKApiConst.FIELDS, "photo_100,online"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    FriendListLoader.this.response = response.json.getJSONArray("response");
                } catch (JSONException e) {
                    Log.e(TAG, "Unhandled JSON response. ", e);
                }
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

        for(int i = 0; i < response.length(); i ++){
            try {
                JSONObject item = response.getJSONObject(i);
                UserItem user = new UserItem(item.getLong("id"), item.getString("photo_100"), item.getString("firstname"), item.getString("lastname"), item.getInt("online") == 1 ? true : false)
                friends.add(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return null;
    }
}
