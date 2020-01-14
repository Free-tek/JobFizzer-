package com.app.jobfizzer.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.ChatMessagesResponseModel.ChatListResponseModel;
import com.app.jobfizzer.Model.ChatMessagesResponseModel.ChatMessageApiModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ChatRepository {

    public LiveData<ChatListResponseModel> getChatLists(InputForAPI inputForAPI) {
        final MutableLiveData<ChatListResponseModel> allPageResponseModelMutableLiveData = new MutableLiveData<>();
        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ChatListResponseModel chatListResponseModel = gson.fromJson(response.toString(), ChatListResponseModel.class);
                allPageResponseModelMutableLiveData.setValue(chatListResponseModel);

            }

            @Override
            public void setResponseError(String error) {
                ChatListResponseModel chatListResponseModel = new ChatListResponseModel();
                chatListResponseModel.setError("true");
                chatListResponseModel.setMessage(error);
                allPageResponseModelMutableLiveData.setValue(chatListResponseModel);

            }
        });
        return allPageResponseModelMutableLiveData;
    }

    public LiveData<ChatMessageApiModel> getChatMessage(InputForAPI inputForAPI) {
        final MutableLiveData<ChatMessageApiModel> mutableLiveData = new MutableLiveData<>();
        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ChatMessageApiModel chatMessageApiModel = gson.fromJson(response.toString(), ChatMessageApiModel.class);
                mutableLiveData.setValue(chatMessageApiModel);

            }

            @Override
            public void setResponseError(String error) {
                ChatMessageApiModel chatMessageApiModel = new ChatMessageApiModel();
                chatMessageApiModel.setError("true");
                chatMessageApiModel.setErrorMessage(error);
                mutableLiveData.setValue(chatMessageApiModel);

            }
        });
        return mutableLiveData;
    }

}
