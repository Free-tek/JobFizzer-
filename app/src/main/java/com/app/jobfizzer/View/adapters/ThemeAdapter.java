package com.app.jobfizzer.View.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Utilities.Constants.Constants;
import com.app.jobfizzer.R;
import com.app.jobfizzer.View.activities.MainActivity;
import com.app.jobfizzer.Utilities.helpers.SharedHelper;

import java.util.List;

import com.app.jobfizzer.Utilities.helpers.CustomLibraries.CircleImageView;

/*
 * Created by Poyyamozhi on 26-Apr-18.
 */

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {

    Context context;
    private List<Integer> mData;
    private LayoutInflater mInflater;

    public ThemeAdapter(Context context, List<Integer> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.theme_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.myTextView.setImageDrawable(new ColorDrawable(mData.get(position)));
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView myTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.info_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                    builder.setMessage(R.string.want_change_theme)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int i = getAdapterPosition() + 1;
                                    SharedHelper.putKey(context, "theme_value", "" + i);
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtra(Constants.LOGIN_TYPE, Constants.ACCOUNTS_TAB);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }

}