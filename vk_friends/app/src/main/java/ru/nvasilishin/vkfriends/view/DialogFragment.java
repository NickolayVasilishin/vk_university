package ru.nvasilishin.vkfriends.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.nvasilishin.vkfriends.R;

/**
 * Created by n.vasilishin on 18.01.2016.
 */
public class DialogFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        return view;
    }
}
