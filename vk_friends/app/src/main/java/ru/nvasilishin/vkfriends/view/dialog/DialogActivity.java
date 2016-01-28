package ru.nvasilishin.vkfriends.view.dialog;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import ru.nvasilishin.vkfriends.R;

/**
 * Created by n.vasilishin on 18.01.2016.
 */
public class DialogActivity extends ActionBarActivity{
    private static final String TAG = "DialogActivityTag";

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.d(TAG, "Starting DialogFragment");
        DialogFragment fragment = new DialogFragment();
        fragment.setArguments(savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.dialog_container, fragment)
                .commit();
    }
}
