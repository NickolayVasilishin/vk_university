package ru.nvasilishin.vkfriends.view.dialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiGetMessagesResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import ru.nvasilishin.vkfriends.utils.UserItem;

/**
 * Created by Nick on 28.01.2016.
 */
public class MessagesLoader extends AsyncTask<Bundle, Void, Void>{
    private static final String TAG = "MessagesLoaderTag";
    private VKList<VKApiMessage> messages;
    private static final int MESSAGES_COUNT = 50;
    private final Object lock;
    private volatile boolean isLoading;
    //messages.getHistory
    //https://vk.com/dev/messages.getHistory?params[count]=111&params[user_id]=28511609&params[v]=5.44

    public MessagesLoader() {
        Log.d(TAG, "Was instantiated");
        lock = new Object();
    }

    @Override
    protected Void doInBackground(Bundle... params) {
        long id = params[0].getLong("id");
        long offset = params[0].getLong("offset");
        loadSync(id, offset);
        return null;
    }

    public MessagesLoader load(long id){
        load(id, 0);
        return this;
    }

    public MessagesLoader load(long id, long offset){
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        bundle.putLong("offset", offset);
        execute(bundle);
        return this;
    }

    private void loadSync(long id, long offset){
        Log.d(TAG, "Loading data in background");
        VKRequest request = new VKRequest("messages.getHistory", VKParameters.from("user_id", id, "offset", offset, "count", MESSAGES_COUNT), VKApiGetMessagesResponse.class);
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Log.d(TAG, "Response received.");
                synchronized (lock) {
                    messages = ((VKApiGetMessagesResponse) response.parsedModel).items;
                    isLoading = false;
                    lock.notifyAll();
                }
                Log.d(TAG, "" + messages.getCount());
            }
            @Override
            public void onError(VKError error) {
                Log.e(TAG, "onError: " + error.toString());
                synchronized (lock) {
                    isLoading = false;
                    lock.notifyAll();
                }
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                Log.d(TAG, "attempt failed: " + request.toString());
            }
        });
        Log.d(TAG, "Load completed.");
    }
}
