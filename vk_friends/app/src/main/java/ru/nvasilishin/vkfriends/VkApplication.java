package ru.nvasilishin.vkfriends;

import android.app.Application;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

/**
 * Created by n.vasilishin on 16.12.2015.
 */
public class VkApplication extends Application {

    public static String TAG = "ApplicationTag";

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                //redirect to AuthActivity?
                Log.d(TAG, "AccessToken is Invalid");
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "At Application onCreate()");
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
        VKSdk.logout();
    }



}
