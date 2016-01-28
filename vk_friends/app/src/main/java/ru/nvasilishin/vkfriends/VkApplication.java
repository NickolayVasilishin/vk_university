package ru.nvasilishin.vkfriends;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
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
                Log.e(TAG, "AccessToken is Invalid");
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "At onCreate()");
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
//        VKSdk.logout();


        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d(TAG, "Activity " + activity.getLocalClassName() + " created.");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(TAG, "Activity " + activity.getLocalClassName() + " started.");
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }




}
