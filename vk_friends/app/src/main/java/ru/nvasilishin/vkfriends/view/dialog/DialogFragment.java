package ru.nvasilishin.vkfriends.view.dialog;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.nvasilishin.vkfriends.R;
import ru.nvasilishin.vkfriends.utils.UserItem;

/**
 * Created by n.vasilishin on 18.01.2016.
 */
public class DialogFragment extends Fragment {
    private static final int VISIBLE_TRESHOLD = 10;
    private static final String TAG = "DialogFragmentTag";
    private long mId;
    private boolean loading = false;
    private UserItem mCollocutor;
    private MessagesLoader mLoader;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Bundle id is " + getArguments().getLong("id"));
        mId = getArguments().getLong("id");

        //Replace with cache
        mCollocutor = new UserItem(mId, "", "", "", true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("" + mId);

        mLoader = new MessagesLoader().load(mId);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.dialog_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MessagesAdapter(mLoader.getMessagesOrWait(), view.getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!loading && mLayoutManager.getItemCount() <= (((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition() + VISIBLE_TRESHOLD)) {
                        mLoader.load(mId, mLayoutManager.getItemCount());
                    mAdapter.notifyDataSetChanged();
                    loading = true;
                }

            }
        });
        return view;
    }

}
