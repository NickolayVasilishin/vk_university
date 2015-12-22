package ru.nvasilishin.vkfriends;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class FriendsActivity extends ActionBarActivity {
    private static final String TAG = "FriendsActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().add(R.id.friendlist_container, new FriendsFragment()).commit();

    }
}
