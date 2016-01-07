package ru.nvasilishin.vkfriends.utils;

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

import ru.nvasilishin.vkfriends.view.FriendListAdapter;

/**
 *  Using to load friends via VkSdk.
 *  Loads friends and fills given RecyclerView.
 *
 */
public class FriendListLoader {
    private static final String TAG = "FriendListLoaderTag";
    private UserItem[] friends;

    private RecyclerView view;
    private Context context;

    /**
     * Не уверен в правильности этого решения.
     * Как-то дождаться получения VkResponse у меня не получилось: метод onComplete
     * вызывается уже из главного треда, других приемлемых вариантов не нашел.
     * Получалось, что приложение падало с NPE, т.к. массив friends еще не успевал инициализироваться.
     * В данном случае самым простым вариантом было бы использовать <code>executeSyncWithListener</code>,
     * но это не очень элегантно.
     * В других работах видел, что загрузка загрузка друзей производится в фрагменте и там же без проблем
     * результат заливается в RecycleView. Тоже не особо нравится этот вариант, т.к. кажется, что
     * это недостаточно гибко.
     *
     * Есть ли какое-нибудь нормальное решение этой проблемы? Хочется асинхронно загружать данные
     * из сети и заливать их во View по готовности. Как нормально дождаться данных?
     *
     */
    public FriendListLoader(RecyclerView view, Context context){
        this.view = view;
        this.context = context;
    }

    public FriendListLoader load(){
        Log.d(TAG, "Building request");
        VKRequest request = new VKRequest("friends.get", VKParameters.from(VKApiConst.SORT, "hints", VKApiConst.FIELDS, "photo_100, online"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Log.d(TAG, "Response received.");
                parseResponse(response);
                fillView(view, context);
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

    private void parseResponse(VKResponse vkResponse){
        try {
            JSONArray response = vkResponse.json.getJSONObject("response").getJSONArray("items");
            UserItem[] friends = new UserItem[response.length()];
            for (int i = 0; i < response.length(); i ++) {
                JSONObject item = response.getJSONObject(i);
                friends[i] = new UserItem(item.getLong("id"), item.getString("photo_100"), item.getString("first_name"), item.getString("last_name"), item.getInt("online") == 1);
            }
            this.friends = friends;
            Log.d(TAG, "Parsed " + friends.length + " users. The last user is " + friends[friends.length-1]);
        } catch (JSONException e) {
            Log.e(TAG, "JSON exception", e);
            e.printStackTrace();
        }
    }

    public UserItem[] getFriends(){
        return friends;
    }

    public FriendListLoader fillView(RecyclerView mRecyclerView, Context context) {
        mRecyclerView.setAdapter(new FriendListAdapter(friends, context));
        return this;
    }
}
