package com.app.jobfizzer.View.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app.jobfizzer.Utilities.Animationhelper;
import com.app.jobfizzer.R;

/*
 * Created by karthik on 01/10/17.
 */
public class PageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_onboard_pager, container, false);

        TextView firstPageTitle = v.findViewById(R.id.firstPageTitle);
        LinearLayout firstPageView = v.findViewById(R.id.firstPageView);
        TextView firstPageSubTitle = v.findViewById(R.id.firstPageSubTitle);
        TextView otherPageSubtitle = v.findViewById(R.id.otherPageSubtitle);
        TextView otherPageTitle = v.findViewById(R.id.otherPageTitle);
        ImageView background = v.findViewById(R.id.backgroundImage);

        if (getArguments().getInt("position") == 0) {
            firstPageTitle.setText(getArguments().getString("title"));
            firstPageSubTitle.setText(getArguments().getString("subtitle"));
            otherPageSubtitle.setVisibility(View.INVISIBLE);
            otherPageTitle.setVisibility(View.INVISIBLE);
            firstPageSubTitle.setVisibility(View.VISIBLE);
            firstPageTitle.setVisibility(View.VISIBLE);
            firstPageView.setVisibility(View.VISIBLE);
        } else {
            otherPageSubtitle.setText(getArguments().getString("subtitle"));
            otherPageTitle.setText(getArguments().getString("title"));
            otherPageSubtitle.setVisibility(View.VISIBLE);
            otherPageTitle.setVisibility(View.VISIBLE);
            firstPageSubTitle.setVisibility(View.INVISIBLE);
            firstPageTitle.setVisibility(View.INVISIBLE);
            firstPageView.setVisibility(View.INVISIBLE);

            Animationhelper animationhelper=new Animationhelper();
            animationhelper.fadeAnimation(otherPageSubtitle);
        }
        background.setImageResource(getArguments().getInt("background"));

        TranslateAnimation imageAnimation =
                new TranslateAnimation(100, -100, 0, 0);
        imageAnimation.setDuration(10000);
        imageAnimation.setFillAfter(true);
        imageAnimation.setRepeatCount(-1);
        imageAnimation.setRepeatMode(Animation.REVERSE);
        background.setAnimation(imageAnimation);
        background.setVisibility(View.VISIBLE);


        return v;
    }

    public static PageFragment newInstance(String title, String subtitle, int background, int position) {

        PageFragment f = new PageFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        b.putString("subtitle", subtitle);
        b.putInt("background", background);
        b.putInt("position", position);
        f.setArguments(b);

        return f;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

}
