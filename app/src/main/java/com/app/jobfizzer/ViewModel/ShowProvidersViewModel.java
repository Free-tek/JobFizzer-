package com.app.jobfizzer.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.app.jobfizzer.Model.ShowProvidersResponseModel;
import com.app.jobfizzer.Repository.ShowProvidersRepository;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.R;

public class ShowProvidersViewModel extends AndroidViewModel {
    private ShowProvidersRepository showProvidersRepository;
    public ShowProvidersViewModel(@NonNull Application application) {
        super(application);
        showProvidersRepository=new ShowProvidersRepository();
    }

    public LiveData<ShowProvidersResponseModel> getShowProviders(InputForAPI inputForAPI) {
        return showProvidersRepository.getShowProviders(inputForAPI);
    }


    public Bitmap getBitmap(Context context) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.circle_violet);
        drawable.setColorFilter(new PorterDuffColorFilter(Utils.getPrimaryCOlor(context),
                PorterDuff.Mode.SRC_IN));
        try {
            Bitmap bitmap;
            bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }


}
