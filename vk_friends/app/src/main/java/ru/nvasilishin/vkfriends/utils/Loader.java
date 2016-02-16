package ru.nvasilishin.vkfriends.utils;

import android.os.AsyncTask;

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

    protected volatile VKRequest mRequest;
    protected volatile boolean mLoading = false;
    protected Object mLock = new Object();
    protected VKList<Subject> mResponse;

    public final Loader<Subject> load() {
        prepareRequest();
        execute();
        return this;
    }


    public final Loader<Subject> load(long id) {
        prepareRequest(id);
        execute();
        return this;
    }


    public final Loader<Subject> load(long offset, int count) {
        prepareRequest(offset, count);
        execute();
        return this;
    }


    @Override
    protected final Void doInBackground(Void... params) {
        loadSync();
        return null;
    }

    protected final void loadSync(){
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
                synchronized (mLock) {
                    mLoading = false;
                    mLock.notifyAll();
                }
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            }
        });
    }

    /**
     * Use this method after you've got contents of the response.
     */
    private void clean() {
        mResponse = null;
    }


    /**
     *
     * @return requested items or null if data wasn't received yet.
     */
    public final VKList<Subject> getOrNull() {
        VKList<Subject> response = mResponse;
        clean();
        return response;
    }

    /**
     *
     * @return requested items or block until it will be loaded.
     *          null in case of error.
     */
    public final VKList<Subject> getOrWait() {
        synchronized (mLock) {
            if (mResponse != null && !mLoading) {
                VKList<Subject> response = mResponse;
                clean();
                return response;
            }
            else {
                try {
                    mLock.wait();
                } catch (InterruptedException e) {}
                VKList<Subject> response = mResponse;
                clean();
                return response;
            }
        }
    }

    protected abstract Loader<Subject> prepareRequest(long offset, int count);
    protected abstract Loader<Subject> prepareRequest();
    protected abstract Loader<Subject> prepareRequest(long id);
    protected abstract VKList<Subject> parse(VKResponse response);


}
