package ru.nvasilishin.vkfriends.utils;

import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

/**
 * Created by n.vasilishin on 17.02.2016.
 */
public class DialogsLoader extends Loader<VKApiMessage> {
    @Override
    protected Loader<VKApiMessage> prepareRequest(long offset, int count) {
        return null;
    }

    @Override
    protected Loader<VKApiMessage> prepareRequest() {
        return null;
    }

    @Override
    protected Loader<VKApiMessage> prepareRequest(long id) {
        return null;
    }

    @Override
    protected VKList<VKApiMessage> parse(VKResponse response) {
        return null;
    }
}
