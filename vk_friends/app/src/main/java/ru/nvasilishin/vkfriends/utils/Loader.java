package ru.nvasilishin.vkfriends.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.Identifiable;
import com.vk.sdk.api.model.VKApiModel;
import com.vk.sdk.api.model.VKList;

import ru.nvasilishin.vkfriends.view.friendlist.AppendableAdapter;

/**
 * Created by Nick on 15.02.2016.
 */
//TODO AsyncTask can be executed only once. Alternatives??????????????
    //Using
public abstract class Loader<Subject extends VKApiModel & Identifiable> {

    private final static int MAX_WAIT_TIME = 100 * 1000;

    protected volatile VKRequest mRequest;
    protected volatile boolean mLoading;
    protected Object mLock;
    protected VKList<Subject> mResponse;
    protected Context mContext;
    private AppendableAdapter mReceiver;

    protected Loader() {
        mLoading = false;
        mLock = new Object();
        mResponse = new VKList<>();
    }

    protected Loader(Context context) {
        this();
        mContext = context;
    }

    public final Loader<Subject> load() {
        Log.d(tag(), "Starting loading");
        prepareRequest();
        //TODO add state checking
        mLoading = true;
        execute();
        return this;
    }

    private void execute() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                loadSync();
                return null;
            }
        }.execute();
    }


    public final Loader<Subject> load(long id) {
        prepareRequest(id);
        mLoading = true;
        execute();
        return this;
    }


    public final Loader<Subject> load(long offset, int count) {
//        if (mRequest == null || !mRequest.getMethodParameters().containsKey("offset") || !mRequest.getMethodParameters().containsKey("count")) {
            prepareRequest(offset, count);
//            Log.d(tag(), "Creating new " + mRequest.getPreparedParameters().toString());
//        }
//        else {
//            Log.d(tag(), "Removed: " + mRequest.getMethodParameters().remove("offset"));
//            mRequest.addExtraParameter("offset", offset);
//            mRequest.addExtraParameter("count", count);
//            Log.d(tag(), "Using old with " + offset + " " + count + ": "  + mRequest.getPreparedParameters().toString());
//        }
        Log.d(tag(), "Creating new " + mRequest.getPreparedParameters().toString());
        mLoading = true;
        execute();
        return this;
    }

    protected final String tag() {
        return this.getClass().getSimpleName() + "Tag";
    }

    public void to(AppendableAdapter adapter) {
        mReceiver = adapter;
    }

    protected final void loadSync(){
        mRequest.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                synchronized (mLock) {
                    if(mReceiver != null) {
                        mReceiver.append(parse(response));
                        mReceiver.notifyAdapter();
                        mReceiver = null;
                    }
                    else
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

    public boolean isLoading() {
        return mLoading;
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
            if (!mLoading && !mResponse.isEmpty()) {
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
