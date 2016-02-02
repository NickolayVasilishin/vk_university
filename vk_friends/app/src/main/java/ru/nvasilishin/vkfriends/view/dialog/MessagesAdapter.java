package ru.nvasilishin.vkfriends.view.dialog;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import ru.nvasilishin.vkfriends.R;

/**
 * Created by n.vasilishin on 02.02.2016.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    Context mContext;
    VKList<VKApiMessage> mMessages;

    MessagesAdapter(VKList<VKApiMessage> messages, Context context){
        mContext = context;
        mMessages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(mMessages.get(position).body);
//        Log.d("MATag", holder.itemView.getParent().toString());
    }

    @Override
    public int getItemCount() {
        return mMessages.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        @Nullable
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.message_text);
            image = (ImageView) itemView.findViewById(R.id.message_image);
        }
    }
}
