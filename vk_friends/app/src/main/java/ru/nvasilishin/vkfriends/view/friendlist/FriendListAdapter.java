package ru.nvasilishin.vkfriends.view.friendlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.nvasilishin.vkfriends.R;
import ru.nvasilishin.vkfriends.utils.UserItem;

/**
 * Created by Николай on 24.12.2015.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
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
    }

    @Override
    public int getItemCount() {
        return mFriends.length;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView photoView;
        public TextView nameView;
        public TextView onlineView;

        public ViewHolder(View view) {
            super(view);

            photoView = (ImageView) view.findViewById(R.id.user_photo);
            nameView = (TextView) view.findViewById(R.id.user_name);
            onlineView = (TextView) view.findViewById(R.id.user_online);

        }
    }
}
