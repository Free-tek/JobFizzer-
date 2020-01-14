package com.app.jobfizzer.View.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.jobfizzer.Model.ChatMessagesResponseModel.ChatMessageApiModel;
import com.app.jobfizzer.Model.ChatMessagesResponseModel.Msglist;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.Events.MessageEvent;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.View.activities.MainActivity;
import com.app.jobfizzer.View.adapters.ChatRoomAdapter;
import com.app.jobfizzer.ViewModel.ChatViewModel;
import com.app.jobfizzer.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.app.jobfizzer.Utilities.helpers.Utils.emojiChatFilter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView chatRooms;
    List<Msglist> chatRoomList = new ArrayList<>();
    EditText searchBox;
    TextView messagesHeading;
    SwipeRefreshLayout swipeView;
    AppSettings appSettings;
    LinearLayout searchLayout;
    RelativeLayout empty_layout;
    LinearLayout chatLayout, signin_layout;
    ImageView backButton;
    MainActivity activity;
    private ChatRoomAdapter chatRoomAdapter;
    private String TAG = ChatFragment.class.getSimpleName();
    private ChatViewModel chatViewModel;
    private FrameLayout rootView;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String param1, String param2) {

        return new ChatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        appSettings = new AppSettings(activity);
        initViews(view);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(false);
            }
        });
        return view;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        try {
            getChatLists();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getVAlues();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Utils.hideKeyboard(activity);


    }

    public void getVAlues() {
        try {
            getChatLists();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getChatLists() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("providerid", appSettings.getUserId());

        InputForAPI inputForAPI = new InputForAPI(getActivity());
        inputForAPI.setUrl(UrlHelper.CHAT_LISTS);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(new HashMap<String, String>());

        getChatData(inputForAPI);
    }

    private void getChatData(InputForAPI inputForAPI) {
        chatViewModel.getChatMessage(inputForAPI).observe(this, new Observer<ChatMessageApiModel>() {
            @Override
            public void onChanged(@Nullable ChatMessageApiModel chatMessageApiModel) {
                if (chatMessageApiModel != null) {
                    if (!chatMessageApiModel.getError().equalsIgnoreCase("true")) {
                        handleChatSuccessResponse(chatMessageApiModel);
                    } else {
                        Utils.toast(rootView, chatMessageApiModel.getErrorMessage());
                    }
                }
            }
        });


    }

    private void handleChatSuccessResponse(ChatMessageApiModel response) {
        chatRoomList = response.getMsglist();
        chatRoomAdapter = new ChatRoomAdapter(chatRoomList, activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        chatRooms.setLayoutManager(linearLayoutManager);
        chatRooms.setAdapter(chatRoomAdapter);
        chatRoomAdapter.notifyDataSetChanged();


        if (!appSettings.getIsLogged().equalsIgnoreCase("false")) {
            if (chatRoomList.size() > 0) {
                empty_layout.setVisibility(View.GONE);
                chatLayout.setVisibility(View.VISIBLE);
            } else {
                empty_layout.setVisibility(View.VISIBLE);
                chatLayout.setVisibility(View.GONE);

            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initViews(View view) {

        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        messagesHeading = view.findViewById(R.id.messagesHeading);
        rootView = view.findViewById(R.id.parentLayoutChat);
        swipeView = view.findViewById(R.id.swipeView);
        chatRooms = view.findViewById(R.id.chatRooms);
        searchBox = view.findViewById(R.id.searchBox);
        backButton = view.findViewById(R.id.backButton);
        empty_layout = view.findViewById(R.id.empty_layout);

        chatLayout = view.findViewById(R.id.chatLayout);
        signin_layout = view.findViewById(R.id.signin_layout);
        searchBox.setFilters(new InputFilter[]{emojiChatFilter});


        if (!appSettings.getIsLogged().equalsIgnoreCase("false")) {
            chatLayout.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
            signin_layout.setVisibility(View.GONE);

        } else {
            chatLayout.setVisibility(View.GONE);
            empty_layout.setVisibility(View.GONE);
            signin_layout.setVisibility(View.VISIBLE);
        }


        searchLayout = view.findViewById(R.id.searchLayout);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                String newText = searchBox.getText().toString();

                try {
                    chatRoomAdapter.getFilter().filter(newText);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

}