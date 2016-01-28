package ru.nvasilishin.vkfriends.view.friendlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ru.nvasilishin.vkfriends.R;
import ru.nvasilishin.vkfriends.utils.UserItem;
import ru.nvasilishin.vkfriends.view.dialog.DialogActivity;

/**
 * Created by Николай on 24.12.2015.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    private static final String TAG = "FriendListAdapterTag";
    private Context mContext;
    private UserItem[] mFriends;

    public FriendListAdapter(UserItem[] friends, Context context) {
        mFriends = friends;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(mContext).load(mFriends[position].getPhotoIcon()).resize(64, 64).centerCrop().into(holder.photoView);
        holder.nameView.setText(mFriends[position].getName());
        holder.onlineView.setText(mFriends[position].isOnline() ? R.string.user_online : R.string.user_offline);
        holder.id = mFriends[position].getId();
    }

    @Override
    public int getItemCount() {
        return mFriends.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView photoView;
        public TextView nameView;
        public TextView onlineView;
        public long id;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            photoView = (ImageView) view.findViewById(R.id.user_photo);
            nameView = (TextView) view.findViewById(R.id.user_name);
            onlineView = (TextView) view.findViewById(R.id.user_online);

        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "Starting new dialog activity with id" + id);
            Bundle bundle = new Bundle();
            bundle.putLong("id", id);

            //TODO Choose proper intent flag
            Intent intent = new Intent(v.getContext(), DialogActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);
            Log.d(TAG, "Is it started?");
        }
    }
}
