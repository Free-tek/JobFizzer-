package com.app.jobfizzer.View.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Model.ChatMessagesResponseModel.Result;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.R;

import java.util.List;

import static com.app.jobfizzer.Utilities.helpers.Utils.convertDate;


/*
 * Created by yuvaraj on 30/11/17.
 */

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MyViewHolder> {
    private List<Result> jsonArray;
    private Context context;
    private String TAG = ChatsAdapter.class.getSimpleName();
    private boolean animationsLocked = false;
    private int lastAnimatedPosition = -1;
    private boolean delayEnterAnimation = true;

    public ChatsAdapter(List<Result> jsonArray, Context context) {
        this.jsonArray = jsonArray;
        this.context = context;
        Utils.dismiss();
    }


    public List<Result> getItems() {
        return this.jsonArray;
    }

    public void swapItems(List<Result> items) {
        this.jsonArray = items;
        notifyDataSetChanged();
    }

    public void changeValues(List<Result> newjsonArray) {
        jsonArray.addAll(newjsonArray);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_items, parent, false);

        return new MyViewHolder(view);
    }

    public void refresh() {
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);

        Result result = jsonArray.get(position);

        Utils.log("dfbfbfb", result.getType());
        Utils.log("dfbfbfbffb", result.getContent());


            if (result.getType().equalsIgnoreCase("sender")) {
                holder.receiverLay.setVisibility(View.GONE);
                holder.senderLay.setVisibility(View.VISIBLE);
                holder.stimeStamp.setText(convertDate(result.getTime()));
                holder.smessage.setText(result.getContent());

            } else {
                holder.senderLay.setVisibility(View.GONE);
                holder.receiverLay.setVisibility(View.VISIBLE);
                holder.timeStamp.setText(convertDate(result.getTime()));
                holder.message.setText(result.getContent());
            }


        if (position == jsonArray.size() - 1) {
            holder.progressBar.setVisibility(View.GONE);
        } else {

            holder.progressBar.setVisibility(View.GONE);
        }

    }

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;


        if (position > lastAnimatedPosition) {
            //lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }

    @Override
    public int getItemCount() {
        if (jsonArray != null) {
            return jsonArray.size();
        } else {
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView message, timeStamp;
        private TextView smessage, stimeStamp;
        private View senderLay;
        private View receiverLay;
        private ProgressBar progressBar;

        MyViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            message = itemView.findViewById(R.id.message);
            smessage = itemView.findViewById(R.id.smessage);
            timeStamp = itemView.findViewById(R.id.timeStamp);
            stimeStamp = itemView.findViewById(R.id.stimeStamp);
            senderLay = itemView.findViewById(R.id.senderLay);
            receiverLay = itemView.findViewById(R.id.receiverLay);

        }
    }
}