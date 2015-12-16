package ru.nvasilishin.vkfriends;

import android.app.Activity;
import android.os.Bundle;

import com.vk.sdk.VKSdk;

/**
 * Created by n.vasilishin on 16.12.2015.
 */
public class AuthActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKSdk.login(this, ACTIVITY_SERVICE);
        //TODO Ways to avoid foreground service activity
    }
}
