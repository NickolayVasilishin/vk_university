package ru.nvasilishin.vkfriends.view.dialog;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import ru.nvasilishin.vkfriends.R;

/**
 * Created by n.vasilishin on 18.01.2016.
 */
public class DialogActivity extends ActionBarActivity{
    private static final String TAG = "DialogActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        Log.d(TAG, "Starting DialogFragment. Bundle is " + getIntent().getExtras());
        DialogFragment fragment = new DialogFragment();
        fragment.setArguments(getIntent().getExtras());
        getFragmentManager()
                .beginTransaction()
                .add(R.id.dialog_container, fragment)
                .commit();
    }
}
