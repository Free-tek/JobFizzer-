package com.app.jobfizzer.View.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Model.ChatMessagesResponseModel.Msglist;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.CustomLibraries.CircleImageView;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.View.activities.ChatActivity;
import com.app.jobfizzer.R;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by user on 22-11-2017.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MyViewHolder> implements Filterable {
    private List<Msglist> mainMsgList;
    private List<Msglist> defMsgList;
    private Activity context;
    private AppSettings appSettings;
    private String TAG = ChatRoomAdapter.class.getSimpleName();
    private CustomFilter customFilter;

    public ChatRoomAdapter(List<Msglist> msgLists, Activity context) {
        this.context = context;
        this.mainMsgList = msgLists;
        this.defMsgList = msgLists;
        customFilter = new CustomFilter(ChatRoomAdapter.this, msgLists);
        appSettings = new AppSettings(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_rooms, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Msglist msgList;
        msgList = mainMsgList.get(holder.getAdapterPosition());

        holder.userName.setText(msgList.getName());

        ImageLoader imageLoader = new ImageLoader(context);
        imageLoader.load(msgList.getProfilePic(), holder.userImage, Utils.getProfilePicture(context));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userImage", mainMsgList.get(holder.getAdapterPosition()).getProfilePic());
                intent.putExtra("userName", mainMsgList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("userID", "" + mainMsgList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("booking_id", "");

                context.startActivity(intent);
                context.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

            }
        });
    }

    private class CustomFilter extends Filter {
        private ChatRoomAdapter mAdapter;
        private List<Msglist> resultsList;

        private CustomFilter(ChatRoomAdapter mAdapter, List<Msglist> resultList) {
            super();
            this.mAdapter = mAdapter;
            this.resultsList = resultList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String charString = charSequence.toString();
            List<Msglist> resultList;

            if (charString.isEmpty()) {
                resultList = defMsgList;

            } else {
                List<Msglist> filterArray = new ArrayList<>();

                for (int i = 0; i < resultsList.size(); i++) {
                    if (resultsList.get(i).getName().toLowerCase()
                            .contains(charString.toLowerCase())) {

                        filterArray.add(resultsList.get(i));
                    }
                }
                resultList = filterArray;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = resultList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            mainMsgList = (List<Msglist>) filterResults.values;
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return customFilter;
    }

    @Override
    public int getItemCount() {
        return mainMsgList != null &&  mainMsgList.size() > 0 ? mainMsgList.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName, lastMessage;
        ImageView lastMessageIcon;

        MyViewHolder(View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            lastMessageIcon = itemView.findViewById(R.id.lastMessageIcon);

            userName = itemView.findViewById(R.id.userName);
            lastMessage = itemView.findViewById(R.id.lastMessage);

        }
    }

}