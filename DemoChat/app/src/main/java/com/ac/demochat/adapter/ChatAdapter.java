package com.ac.demochat.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ac.demochat.R;
import com.ac.demochat.entity.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Chat> mDataSet;
    private String mId;

    private static final int CHAT_RIGHT = 1;
    private static final int CHAT_LEFT = 2;

    public ChatAdapter(List<Chat> mDataSet, String mId) {
        this.mDataSet = mDataSet;
        this.mId = mId;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        if (viewType == CHAT_RIGHT) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_right, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_left, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        Chat chat = mDataSet.get(position);
        holder.txtEmail.setText(chat.getName());
        holder.txtMessage.setText(chat.getMessage());
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataSet.get(position).getUid().equals(mId))
            return CHAT_RIGHT;

        return CHAT_LEFT;
    }



    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtMessage;
        protected TextView txtEmail;
        public ViewHolder(View itemView) {
            super(itemView);
            txtMessage =  itemView.findViewById(R.id.txtMessage);
            txtEmail = itemView.findViewById(R.id.txtEmail);
        }
    }
}
