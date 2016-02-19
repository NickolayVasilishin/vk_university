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
    private static final int VISIBLE_TRESHOLD = 30;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Loader<VKApiUser> mLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        Log.d(TAG, "At onCreateView");

        mLoader = new FriendsLoader(getActivity()).load();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.friends_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FriendListAdapter(mLoader.getOrWait(), view.getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLayoutManager.getItemCount() <= (((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition() + VISIBLE_TRESHOLD)) {
                    mLoader.load(mLayoutManager.getItemCount(), FriendsLoader.DEFAULT_MESSAGES_COUNT).to((AppendableAdapter)mAdapter);
                    Log.d(TAG, "Total friends: " + mLayoutManager.getItemCount());
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
