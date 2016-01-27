package ru.nvasilishin.vkfriends.view.friendlist;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.nvasilishin.vkfriends.utils.FriendListLoader;
import ru.nvasilishin.vkfriends.R;

/**
 * Created by n.vasilishin on 22.12.2015.
 */
public class FriendsFragment extends Fragment {
    private static final String TAG = "FriendsFragmentTag";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FriendListLoader mFriendListLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        Log.d(TAG, "At onCreateView");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.friends_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mFriendListLoader = new FriendListLoader().loadAsync();
        mAdapter = new FriendListAdapter(mFriendListLoader.getFriendsOrWait(), view.getContext());
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
