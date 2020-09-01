package com.app.jobfizzer.View.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Model.ShowProvidersResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.helpers.OnSwipeListener;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.View.activities.ProviderProfileActivity;
import com.app.jobfizzer.View.activities.ShowProvidersActivity;
import com.app.jobfizzer.R;

import java.util.List;

/**
 * Created by karthik on 11/10/17.
 */
public class ProvidersAdapter extends RecyclerView.Adapter<ProvidersAdapter.MyViewHolder> {
    private Context context;
    private List<ShowProvidersResponseModel.ProviderService.AllProvider> providers;

    private GestureDetectorCompat detector;
    private OnSwipeListener onSwipeListener = new OnSwipeListener() {

        @Override
        public boolean onSwipe(Direction direction) {

            Utils.log("Swipe", direction.toString());
            // Possible implementation
            if (direction == Direction.left || direction == Direction.right) {
                // Do something COOL like animation or whatever you want
                // Refer to your view if needed using a global reference
                return true;
            } else if (direction == Direction.up || direction == Direction.down) {
                // Do something COOL like animation or whatever you want
                // Refer to your view if needed using a global reference
                if (direction == Direction.up) {
                    Utils.log("SWIPE", "UP");
//                    ActivityCompat.finishAfterTransition(ProviderProfileActivity.this);
                }
                return true;
            }

            return super.onSwipe(direction);
        }
    };


    public ProvidersAdapter(Context context, List<ShowProvidersResponseModel.ProviderService.AllProvider> providers) {
        this.context = context;
        this.providers = providers;
        detector = new GestureDetectorCompat(context, onSwipeListener);
    }

    @Override
    public ProvidersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_provider, parent, false);

        return new ProvidersAdapter.MyViewHolder(itemView);
    }

    @SuppressLint({"ClickableViewAccessibility", "DefaultLocale", "SetTextI18n"})

    @Override
    public void onBindViewHolder(final ProvidersAdapter.MyViewHolder holder, int position) {
        final ShowProvidersResponseModel.ProviderService.AllProvider subCategory = providers.get(position);
        holder.providerName.setText(subCategory.getName());
        double val = subCategory.getDistance();
        String disntac = String.format("%.2f", val);
        holder.distance.setText(disntac + " km");
        holder.ratingView.setRating(subCategory.getAvgRating());
        ImageLoader imageLoader = new ImageLoader(context);
        imageLoader.load(providers.get(position).getImage(), holder.providerPic, Utils.getProfilePicture(context));

        holder.providerPic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utils.log("TOUCH", "UP");
                return detector.onTouchEvent(motionEvent);
            }
        });
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, ProviderProfileActivity.class);
                    intent.putExtra("providerDetails", subCategory);
                    Pair<View, String> p1 = Pair.create((View) holder.providerName, "providerName");
                    Pair<View, String> p2 = Pair.create((View) holder.providerPic, "providerPic");
                    Pair<View, String> p3 = Pair.create((View) holder.rootLayout, "parentLayout");
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((ShowProvidersActivity) context, holder.providerPic, "providerPic");
                    context.startActivity(intent, optionsCompat.toBundle());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return providers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView providerName, distance;
        RatingBar ratingView;
        ImageView providerPic;
        LinearLayout rootLayout;
        LinearLayout cardView;

        MyViewHolder(View view) {
            super(view);
            providerName = view.findViewById(R.id.providerName);
            distance = view.findViewById(R.id.distance);
            providerPic = view.findViewById(R.id.providerPic);
            rootLayout = view.findViewById(R.id.rootLayout);
            cardView = view.findViewById(R.id.cardLayout);
            ratingView = view.findViewById(R.id.ratingView);
        }
    }
}
