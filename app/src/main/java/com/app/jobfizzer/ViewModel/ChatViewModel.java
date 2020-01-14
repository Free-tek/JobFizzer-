package com.app.jobfizzer.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzer.Model.ChatMessagesResponseModel.ChatListResponseModel;
import com.app.jobfizzer.Model.ChatMessagesResponseModel.ChatMessageApiModel;
import com.app.jobfizzer.Model.ChatMessagesResponseModel.Result;
import com.app.jobfizzer.Repository.ChatRepository;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    ChatRepository chatRepository;
    public ChatViewModel(@NonNull Application application) {
        super(application);
        chatRepository=new ChatRepository();
    }


    public LiveData<ChatListResponseModel> getChatLists(InputForAPI inputs) {
        return  chatRepository.getChatLists(inputs);
    }

    public LiveData<ChatMessageApiModel> getChatMessage(InputForAPI inputs) {
        return  chatRepository.getChatMessage(inputs);
    }

    public List<Result> getNextValues(ChatListResponseModel response,String userID) {
        List<Result> results = response.getResults();
        List<Result> newArray=new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            Result jsonObjects = results.get(i);
            if (jsonObjects.getSenderID().toString().equalsIgnoreCase(userID)) {
                jsonObjects.setType("receiver");
            } else {
                jsonObjects.setType("sender");
            }
            newArray.add(jsonObjects);
        }

        newArray = reverse(newArray);
        return results;
    }

    public List<Result> reverse(List<Result> jsonArray) {
        List<Result> finalList = new ArrayList<>(jsonArray);
        Collections.reverse(finalList);
        return finalList;
    }

    public List<Result> getCurrentValues(ChatListResponseModel response, String userID) {
        List<Result> messageArray = new ArrayList<>();

        List<Result> results = response.getResults();
        if (results.size() != 0) {

            for (int i = 0; i < results.size(); i++) {
                Result jsonObjects = results.get(i);
                if (jsonObjects.getSenderID().toString().equalsIgnoreCase(userID)) {

                    jsonObjects.setType("receiver");
                } else {
                    jsonObjects.setType("sender");

                }
                messageArray.add(jsonObjects);
            }
        }
        if (messageArray.size()>0)
        {
            messageArray = reverse(messageArray);
        }

        return messageArray;
    }

    public List<Result> addValuesinStart(List<Result> items, Result result) {
        List<Result> finalArray = new ArrayList<>();
        finalArray.add(0, result);
        for (int i = 0; i < items.size(); i++) {
            finalArray.add(i + 1, items.get(i));
        }
        return finalArray;
    }
}
