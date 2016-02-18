package ru.nvasilishin.vkfriends.utils;

import android.content.Context;
import android.util.Log;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKUsersArray;

/**
 * Created by Nick on 16.02.2016.
 */
public class FriendsLoader extends Loader<VKApiUser> {
    private static String METHOD_NAME = "friends.get";
    private static String REQUEST_FIELDS = "photo_100, online";
    private static String REQUEST_SORT = "hints";

    public FriendsLoader(Context context) {
       super(context);
    }

    @Override
    protected Loader<VKApiUser> prepareRequest(long offset, int count) {
        mRequest = new VKRequest(METHOD_NAME, VKParameters.from(VKApiConst.SORT, REQUEST_SORT, VKApiConst.FIELDS, REQUEST_FIELDS, VKApiConst.COUNT, count, VKApiConst.OFFSET, offset), VKUsersArray.class);
        return this;
    }

    @Override
    protected Loader<VKApiUser> prepareRequest() {
        mRequest = new VKRequest(METHOD_NAME, VKParameters.from(VKApiConst.SORT, REQUEST_SORT, VKApiConst.FIELDS, REQUEST_FIELDS), VKUsersArray.class);
        return this;
    }

    @Override
    protected Loader<VKApiUser> prepareRequest(long id) {
        mRequest = new VKRequest("users.get", VKParameters.from(VKApiConst.FIELDS, REQUEST_FIELDS, VKApiConst.USER_ID, id), VKUsersArray.class);
        return this;
    }

    @Override
    protected VKList<VKApiUser> parse(VKResponse response) {
        VKList<VKApiUser> users;
        Log.d(tag(), "Parsing response " + response.json.toString().substring(0, 50) + "...");
        if(response.parsedModel instanceof VKUsersArray) {
            users = new VKList<>(response.json, VKApiUser.class);
        } else {
            Log.d(tag(), "Parsing failed. Returning empty list.");
            users = new VKList<>();
        }
        return users;
    }
}
