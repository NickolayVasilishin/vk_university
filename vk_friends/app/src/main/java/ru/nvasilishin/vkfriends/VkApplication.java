package ru.nvasilishin.vkfriends;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

/**
 * Created by n.vasilishin on 16.12.2015.
 */
public class VkApplication extends Application {

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                forward();
                Toast.makeText(getApplicationContext(), "AccessToken is Invalid", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "At Application onCreate()", Toast.LENGTH_SHORT).show();
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
        VKSdk.logout();
        Toast.makeText(this, "Logged: " + VKSdk.isLoggedIn(), Toast.LENGTH_SHORT).show();
        forward();
    }

    private void forward(){
        Class activityClass = VKSdk.isLoggedIn() ? FriendsActivity.class : AuthActivity.class;
        Intent intent = new Intent(this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
    }
}
