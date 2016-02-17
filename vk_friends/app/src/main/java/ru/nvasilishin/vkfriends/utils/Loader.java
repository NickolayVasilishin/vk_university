package ru.nvasilishin.vkfriends.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.Identifiable;
import com.vk.sdk.api.model.VKApiModel;
import com.vk.sdk.api.model.VKList;

/**
 * Created by Nick on 15.02.2016.
 */
public abstract class Loader<Subject extends VKApiModel & Identifiable> extends AsyncTask<Void, Void, Void> {

    private final static int MAX_WAIT_TIME = 100 * 1000;

    protected volatile VKRequest mRequest;
    protected volatile boolean mLoading = false;
    protected Object mLock = new Object();
    protected VKList<Subject> mResponse;
    @NonNull
    protected Context mContext;

    public final Loader<Subject> load() {
        Log.d(tag(), "Starting loading");
        prepareRequest();
        mLoading = true;
        execute();
        return this;
    }


    public final Loader<Subject> load(long id) {
        prepareRequest(id);
        mLoading = true;
        execute();
        return this;
    }


    public final Loader<Subject> load(long offset, int count) {
        prepareRequest(offset, count);
        mLoading = true;
        execute();
        return this;
    }


    @Override
    protected final Void doInBackground(Void... params) {
        loadSync();
        return null;
    }

    protected final String tag() {
        return this.getClass().getSimpleName() + "Tag";
    }

    protected final void loadSync(){
        final Boolean result = new Boolean(false);
        mRequest.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                synchronized (mLock) {
                    mResponse = parse(response);
                    mLoading = false;
                    mLock.notifyAll();
                }
            }

            @Override
            public void onError(VKError error) {
                notifyOnError(error);
                synchronized (mLock) {
                    mLoading = false;
                    mLock.notifyAll();
                }
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                Log.d(tag(), "Loading attempt " + attemptNumber);
            }
        });
    }

    /**
     * Use this method as return in get* methods.
     * This class has to just download, not store data.
     *
     * @return list of loaded items
     */
    private VKList<Subject> andClean() {
        VKList<Subject> response = mResponse;
        mResponse = new VKList<>();
        return response;
    }

    /**
     * Notify user if loading failed.
     *
     * @param error
     */
    protected void notifyOnError(VKError error) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "Loading failed.", Toast.LENGTH_SHORT).show();
            }
        });
        Log.e(tag(), error.errorMessage);
    }

    /**
     *
     * @return requested items or null if data wasn't received yet.
     */
    public final VKList<Subject> getOrNull() {
        return andClean();
    }

    /**
     *
     * @return requested items or block until it will be loaded.
     *          null in case of error.
     */
    public final VKList<Subject> getOrWait() {
        synchronized (mLock) {
            Log.d(tag(), "Data is " + (mLoading ? "loading" : "not loading"));
            if (!mResponse.isEmpty() && !mLoading) {
                return andClean();
            }
            else {
                try {
                    Log.d(tag(), "Waiting for data.");
                    mLock.wait(MAX_WAIT_TIME);
                } catch (InterruptedException e) {}
                Log.d(tag(), "Returning data: " + mResponse.size());
                return andClean();
            }
        }
    }

    protected abstract Loader<Subject> prepareRequest(long offset, int count);
    protected abstract Loader<Subject> prepareRequest();
    protected abstract Loader<Subject> prepareRequest(long id);
    protected abstract VKList<Subject> parse(VKResponse response);


}
