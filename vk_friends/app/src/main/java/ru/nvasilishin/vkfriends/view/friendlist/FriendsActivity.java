package ru.nvasilishin.vkfriends.view.friendlist;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import ru.nvasilishin.vkfriends.R;
import ru.nvasilishin.vkfriends.view.dialog.DialogActivity;

public class FriendsActivity extends ActionBarActivity {
    private static final String TAG = "FriendsActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "At FriendsActivity");
        setContentView(R.layout.activity_friends);

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().add(R.id.friendlist_container, new FriendsFragment()).commit();

    }
}
