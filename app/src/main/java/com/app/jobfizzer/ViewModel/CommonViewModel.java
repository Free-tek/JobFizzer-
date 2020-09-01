package com.app.jobfizzer.ViewModel;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Environment;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.AllPageResponseModel;
import com.app.jobfizzer.Model.CommonResponseModel;
import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Model.ForgotPasswordResponseModel;
import com.app.jobfizzer.Model.ImageUploadSuccessResponse;
import com.app.jobfizzer.Model.PayStachAccessURLResponse;
import com.app.jobfizzer.Repository.CommonRepository;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.R;

import java.io.File;
import java.security.MessageDigest;
import java.util.Random;

public class CommonViewModel extends AndroidViewModel {

    CommonRepository commonRepository;

    public CommonViewModel(@NonNull Application application) {
        super(application);
        commonRepository = new CommonRepository();
    }

    public LiveData<AllPageResponseModel> getAllPageResponse(InputForAPI inputs) {
        return commonRepository.getAllPageResponse(inputs);
    }

    public LiveData<ImageUploadSuccessResponse> uploadImage(InputForAPI inputs) {
        return commonRepository.uploadImage(inputs);
    }

    public LiveData<ForgotPasswordResponseModel> requestOtp(InputForAPI inputs) {
        return commonRepository.requestOtp(inputs);
    }


    public LiveData<FlagResponseModel> flagUpdate(InputForAPI inputs) {
        return commonRepository.flagUpdate(inputs);
    }

    public String getCombinedStrings(String... strings) {
        StringBuilder finalString = new StringBuilder();
        for (String string : strings) {
            finalString.append(string);
        }
        return finalString.toString();
    }


    public void generateKeyHash() {
        try {
            PackageInfo info = getApplication().getBaseContext().getPackageManager().getPackageInfo(
                    getApplication().getBaseContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Utils.log("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public File getFile()
    {
        File filepath = Environment.getExternalStorageDirectory();
        final File zoeFolder = new File(filepath.getAbsolutePath(),
                getApplication().getBaseContext().getString(R.string.app_name)).getAbsoluteFile();
        if (!zoeFolder.exists()) {
            zoeFolder.mkdir();
        }
        File newFolder = new File(zoeFolder,
                getApplication().getBaseContext().getString(R.string.app_name) + "_Image").getAbsoluteFile();
        if (!newFolder.exists()) {
            newFolder.mkdir();
        }

        Random r = new Random();
        int Low = 1000;
        int High = 10000000;
        int randomImageNo = r.nextInt(High - Low) + Low;
        String camera_captureFile = String.valueOf("PROFILE_IMG_" + randomImageNo);
        final File file = new File(newFolder, camera_captureFile + ".jpg");
        return file;
    }


    public MutableLiveData<PayStachAccessURLResponse> webPayment(InputForAPI inputForAPI) {
        return commonRepository.makePayment(inputForAPI);
    }

    public MutableLiveData<CommonResponseModel> getPaymentStatus(InputForAPI book_id) {
        return commonRepository.getPaymentStatus(book_id);
    }
}
