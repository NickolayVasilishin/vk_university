package ru.nvasilishin.vkfriends.view.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import ru.nvasilishin.vkfriends.view.friendlist.FriendsActivity;

/**
 * Created by n.vasilishin on 16.12.2015.
 */
public class AuthActivity extends Activity {

    public static String TAG = "AuthActivityTag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!VKSdk.isLoggedIn()) {
            Log.d(TAG, "Isn't logged in. Starting VKSdk.login().");
            VKSdk.login(this, VKScope.MESSAGES, VKScope.FRIENDS);
        } else {
            Log.d(TAG, "Logged in. Forwarding to Friends Activity.");
            forward();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Log.d(TAG, "Success result, starting Friends Activity");
                forward();
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), "You must log in to proceed.", Toast.LENGTH_SHORT).show();
                AuthActivity.this.finish();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void forward(){
        startActivity(new Intent(this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

}
