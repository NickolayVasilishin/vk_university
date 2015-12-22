package ru.nvasilishin.vkfriends;


import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * Created by n.vasilishin on 22.12.2015.
 */
public class FriendsFragment extends ListFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<FriendItem> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new FriendItem[] {new FriendItem(), new FriendItem()});
        setListAdapter(adapter);
    }
}
