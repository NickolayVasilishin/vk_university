package ru.nvasilishin.vkfriends.utils;

import android.content.Context;

import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiGetMessagesResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

/**
 * Created by n.vasilishin on 17.02.2016.
 */
public class DialogLoader extends Loader<VKApiMessage> {
    private long mResponderId;
    public static final int DEFAULT_MESSAGES_COUNT = 50;

    public  DialogLoader(long responderId, Context context) {
        super(context);
        mResponderId = responderId;
    }

    @Override
    protected Loader<VKApiMessage> prepareRequest(long offset, int count) {
        mRequest =  new VKRequest("messages.getHistory", VKParameters.from("user_id", mResponderId, "offset", offset, "count", count), VKApiGetMessagesResponse.class);
        return this;
    }

    @Override
    protected Loader<VKApiMessage> prepareRequest() {
        mRequest =  new VKRequest("messages.getHistory", VKParameters.from("user_id", mResponderId, "offset", 0, "count", DEFAULT_MESSAGES_COUNT), VKApiGetMessagesResponse.class);
        return this;
    }

    @Override
    protected Loader<VKApiMessage> prepareRequest(long id) {
        return null;
    }

    @Override
    protected VKList<VKApiMessage> parse(VKResponse response) {
        return ((VKApiGetMessagesResponse) response.parsedModel).items;
    }
}
