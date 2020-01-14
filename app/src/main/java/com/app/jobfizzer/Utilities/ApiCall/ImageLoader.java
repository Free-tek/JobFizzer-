package com.app.jobfizzer.Utilities.ApiCall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ImageLoader {
    public Context context;
    public ImageLoader(Context contextvalue) {
        context = contextvalue;
    }

    public void load(String url, ImageView imageView,Drawable drawable) {
        Glide.with(context).load(url).apply(new RequestOptions().placeholder(drawable).error(drawable)).into(imageView);
    }

    public void loadWithCallBack(String url, ImageView imageView, Drawable drawable, final ImageLoaded imageLoaded) {
        Glide.with(context).load(url).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        imageLoaded.onImageLoaded();
                        return false;
                    }
                }).apply(new RequestOptions().placeholder(drawable).error(drawable))
                .into(imageView);
    }


    public void loadWithBitmap(String url, final ImageLoadedBitmap imageLoaded) {

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(3)
                .cornerRadiusDp(40)
                .oval(false)
                .build();
        Picasso.with(context).load(url).transform(transformation).resize(140, 140).into(new com.squareup.picasso.Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageLoaded.onImageLoaded(bitmap);

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

    }


    public interface ImageLoaded
    {
        public void onImageLoaded();
    }


    public interface ImageLoadedBitmap
    {
        public void onImageLoaded(Bitmap bitmap);
    }



}
