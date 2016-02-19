package ru.nvasilishin.vkfriends.view.dialog;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKList;

import ru.nvasilishin.vkfriends.R;
import ru.nvasilishin.vkfriends.view.friendlist.AppendableAdapter;

/**
 * Created by n.vasilishin on 02.02.2016.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> implements AppendableAdapter{
    private static final String TAG = "MessagesAdapterTag";
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
        VKApiMessage message = mMessages.get(position);
        holder.text.setText(message.body);
        if (!message.attachments.isEmpty())
            Log.d(TAG, "Attachments: " + message.attachments.toAttachmentsString());
        if(message.attachments.toAttachmentsString().contains(VKAttachments.TYPE_PHOTO))
            Picasso.with(mContext).load(((VKApiPhoto)message.attachments.get(0)).photo_604).resize(400, 200).centerCrop().into(holder.image);

//        Log.d("MATag", holder.itemView.getParent().toString());
    }

    @Override
    public int getItemCount() {
        return mMessages.getCount();
    }

    @Override
    public AppendableAdapter append(VKList list) {
        mMessages.addAll(list);
        return this;
    }

    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
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
