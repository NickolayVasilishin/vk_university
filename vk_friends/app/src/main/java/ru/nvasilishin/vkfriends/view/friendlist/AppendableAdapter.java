package ru.nvasilishin.vkfriends.view.friendlist;

import com.vk.sdk.api.model.VKApiModel;
import com.vk.sdk.api.model.VKList;

/**
 * Created by n.vasilishin on 19.02.2016.
 */
public interface AppendableAdapter {
    public AppendableAdapter append(VKList<? extends VKApiModel> list);
    public void notifyAdapter();
}
