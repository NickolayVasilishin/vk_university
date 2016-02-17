package ru.nvasilishin.vkfriends.view.friendlist;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vk.sdk.api.model.VKApiUser;

import ru.nvasilishin.vkfriends.R;
import ru.nvasilishin.vkfriends.utils.FriendsLoader;
import ru.nvasilishin.vkfriends.utils.Loader;

/**
 * Created by n.vasilishin on 22.12.2015.
 */
public class FriendsFragment extends Fragment {
    private static final String TAG = "FriendsFragmentTag";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Loader<VKApiUser> mFriendListLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        Log.d(TAG, "At onCreateView");

        mFriendListLoader = new FriendsLoader(getActivity()).load();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.friends_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FriendListAdapter(mFriendListLoader.getOrWait(), view.getContext());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
