package ru.nvasilishin.vkfriends.utils;

import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

/**
 * Created by n.vasilishin on 17.02.2016.
 */
public class MusicLoader extends Loader<VKApiAudio> {
    @Override
    protected Loader<VKApiAudio> prepareRequest(long offset, int count) {
        return null;
    }

    @Override
    protected Loader<VKApiAudio> prepareRequest() {
        return null;
    }

    @Override
    protected Loader<VKApiAudio> prepareRequest(long id) {
        return null;
    }

    @Override
    protected VKList<VKApiAudio> parse(VKResponse response) {
        return null;
    }
}
