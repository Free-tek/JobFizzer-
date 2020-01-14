package com.app.jobfizzer.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.app.jobfizzer.R;

public class Animationhelper {



    public void animateUp(LinearLayout commentSection, final LinearLayout topLayout, Context context) {
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) topLayout.getLayoutParams();
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slidefrombottomm);
        commentSection.setVisibility(View.VISIBLE);
        commentSection.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slidefrombottom));

        topLayout.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                topLayout.setLayoutParams(params);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    public void collapse(final View v,Context context)
    {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / context.getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void expand(final View v,Context context) {
        v.measure(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RecyclerView.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (targetHeight / context.getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void fadeAnimation(View view) {
        TranslateAnimation textAnimation = new TranslateAnimation(0, 0, 100, 0);
        textAnimation.setDuration(1000);
        textAnimation.setFillAfter(true);
        float fromAlpha = (float) 0.2;
        float toAlpha = (float) 1.0;
        AlphaAnimation fadeInAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        fadeInAnimation.setDuration(1000);
        AnimationSet s = new AnimationSet(false);
        s.addAnimation(textAnimation);
        s.addAnimation(fadeInAnimation);
        view.setAnimation(s);
        view.setVisibility(View.VISIBLE);
    }
}
