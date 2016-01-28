package ru.nvasilishin.vkfriends.view.dialog;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.nvasilishin.vkfriends.R;
import ru.nvasilishin.vkfriends.utils.UserItem;

/**
 * Created by n.vasilishin on 18.01.2016.
 */
public class DialogFragment extends Fragment{
    private long mId;
    private UserItem mCollocutor;
    private MessagesLoader mLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getArguments().getLong("id");

        //Replace with cache
        mCollocutor = new UserItem(mId, "", "", "", true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment, container, false);
        getActivity().getActionBar().setTitle("" + mId);

        mLoader = new MessagesLoader().load(mId);
        return view;
    }


}
