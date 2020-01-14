package com.app.jobfizzer.View.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.jobfizzer.Model.ChatMessagesResponseModel.ChatListResponseModel;
import com.app.jobfizzer.Model.ChatMessagesResponseModel.Result;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.Events.InternetEvent;
import com.app.jobfizzer.Utilities.Events.MessageEvent;
import com.app.jobfizzer.Utilities.FCM.ConnectivityReceiver;
import com.app.jobfizzer.Utilities.FCM.ServiceClass;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.CustomLibraries.CircleImageView;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.View.adapters.ChatsAdapter;
import com.app.jobfizzer.ViewModel.ChatViewModel;
import com.google.gson.Gson;
import com.app.jobfizzer.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.app.jobfizzer.Utilities.helpers.Utils.emojiChatFilter;


public class ChatActivity extends BaseActivity implements View.OnClickListener {
    public String displayPic;
    public int currentPage = 1;
    public int totalPage = 1;
    ServiceClass.Emitters emitters = new ServiceClass.Emitters(ChatActivity.this);
    ImageLoader imageLoader;
    ChatViewModel chatViewModel;
    private RecyclerView messageItems;
    private List<Result> messageArray = new ArrayList<>();
    private TextView userName, waitingForInternet;
    private CircleImageView userImage;
    private ImageView backIcon;
    private LinearLayout sendLayout;
    private String userID;
    private String providerID;
    private EditText messageInput;
    private AppSettings appSettings = new AppSettings(ChatActivity.this);
    private ChatsAdapter chatsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String TAG = ChatActivity.class.getSimpleName();
    private String booking_id;
    private boolean internetStatus;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
//        String mJsonString = event.getJsonObject().toString();
//        JsonParser parser = new JsonParser();
//        JsonElement mJson = parser.parse(mJsonString);
        Gson gson = new Gson();
        Result object = gson.fromJson(event.getJsonObject().toString(), Result.class);
        Utils.log(TAG,object.getType());
        object.setType("receiver");
        updateMessages(object);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInternetEvent(InternetEvent event) {
        internetStatus = event.isInternetStatus();
        loadValue(internetStatus);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        imageLoader = new ImageLoader(this);
        initViews();
        initListners();
        setValues();
    }

    private void initViews() {
        messageItems = findViewById(R.id.messageItems);
        userName = findViewById(R.id.userName);
        userImage = findViewById(R.id.userImage);
        sendLayout = findViewById(R.id.sendLayout);
        backIcon = findViewById(R.id.backIcon);
        messageInput = findViewById(R.id.messageInput);
        waitingForInternet = findViewById(R.id.waitingForInternet);
        messageInput.setFilters(new InputFilter[]{emojiChatFilter});
        registerReceiver(new ConnectivityReceiver(), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
    }


    private void initListners() {
        backIcon.setOnClickListener(this);
        sendLayout.setOnClickListener(this);

        messageItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (chatsAdapter.getItemCount() > 8) {
                        if (linearLayoutManager.findLastVisibleItemPosition() >= chatsAdapter.getItemCount() - 1) {
                            if (internetStatus) {
                                try {
                                    buildNextDataInputs();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void buildNextDataInputs() throws JSONException {

        if (currentPage < totalPage) {
            int curr = currentPage + 1;
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("senderID", appSettings.getUserId());
            jsonObject.put("receiverID", userID);
            jsonObject.put("page", "" + curr);
            jsonObject.put("senderType", "user");

            InputForAPI inputForAPI = new InputForAPI(ChatActivity.this);
            inputForAPI.setUrl(UrlHelper.CHAT_MESSAGES);
            inputForAPI.setFile(null);
            inputForAPI.setJsonObject(jsonObject);
            inputForAPI.setHeaders(new HashMap<String, String>());
            getNextData(inputForAPI);
        }

    }

    private void loadValue(boolean isConnected) {
        if (isConnected) {
            if (waitingForInternet.getVisibility() == View.VISIBLE) {
                waitingForInternet.setBackgroundColor(ContextCompat.getColor(ChatActivity.this, R.color.fb_connect));
                waitingForInternet.setText(R.string.connecting);
                waitingForInternet.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        waitingForInternet.setVisibility(View.GONE);
                    }
                }, 3000);
            } else {
                waitingForInternet.setVisibility(View.GONE);
            }
        } else {
            waitingForInternet.setBackgroundColor(ContextCompat.getColor(ChatActivity.this, R.color.fb_messenger));
            waitingForInternet.setText(R.string.waiting_for_internet);
            waitingForInternet.setVisibility(View.VISIBLE);
        }
    }

    private void setValues() {
        Intent intent = getIntent();
        try {
            if (intent != null) {
                userID = intent.getStringExtra("userID");
                booking_id = intent.getStringExtra("booking_id");
                displayPic = intent.getStringExtra("userImage");
                userName.setText(intent.getStringExtra("userName"));
                Utils.log(TAG, "booking_id: " + booking_id);

                imageLoader.load(displayPic, userImage, Utils.getProfilePicture(ChatActivity.this));
                providerID = appSettings.getUserId();

                try {
                    buildChatMessageInputs();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildChatMessageInputs() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("senderID", appSettings.getUserId());
        jsonObject.put("receiverID", userID);
        jsonObject.put("page", "1");
        jsonObject.put("senderType", "user");

        InputForAPI inputForAPI = new InputForAPI(ChatActivity.this);
        inputForAPI.setUrl(UrlHelper.CHAT_MESSAGES);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        getData(inputForAPI);
    }

    private void getData(InputForAPI inputForAPI) {


        chatViewModel.getChatLists(inputForAPI).observe(this, new Observer<ChatListResponseModel>() {
            @Override
            public void onChanged(@Nullable ChatListResponseModel chatListResponseModel) {
                if (chatListResponseModel != null) {
                    if (!chatListResponseModel.getError().equalsIgnoreCase("true")) {
                        handleChatMessageResponse(chatListResponseModel);
                    } else {
                        setAdapterValues();
                        Utils.toast(findViewById(android.R.id.content), chatListResponseModel.getMessage());
                    }
                }
            }
        });
    }

    private void handleChatMessageResponse(ChatListResponseModel response) {

        messageArray = chatViewModel.getCurrentValues(response, userID);
        if (response.getResults().size() > 0) {
            booking_id = String.valueOf(response.getResults().get(0).getBookingId());
            if (messageArray.size() > 0) {
                totalPage = response.getPageCount();
                currentPage = Integer.parseInt(response.getCurrentpage());
            } else {
                totalPage = 1;
                currentPage = 1;
            }
        }
        setAdapterValues();
    }


    public void setAdapterValues() {
        chatsAdapter = new ChatsAdapter(messageArray, ChatActivity.this);
        linearLayoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, true);
        messageItems.setLayoutManager(linearLayoutManager);
        messageItems.setAdapter(chatsAdapter);
        chatsAdapter.notifyDataSetChanged();
        linearLayoutManager.scrollToPosition(0);
    }

    private void getNextData(InputForAPI inputForAPI) {

        chatViewModel.getChatLists(inputForAPI).observe(this, new Observer<ChatListResponseModel>() {
            @Override
            public void onChanged(@Nullable ChatListResponseModel chatListResponseModel) {
                if (chatListResponseModel != null) {
                    handleNextDataResponse(chatListResponseModel);
                }
            }
        });

    }

    private void handleNextDataResponse(ChatListResponseModel response) {
        List<Result> newArray;
        newArray = chatViewModel.getNextValues(response, userID);
        totalPage = response.getPageCount();
        currentPage = Integer.parseInt(response.getCurrentpage());
        chatsAdapter.changeValues(newArray);
    }


    @Override
    public void onClick(View view) {
        if (view == backIcon) {
            onBackPressed();

        } else if (view == sendLayout) {
            if (messageInput.getText().toString().trim().length() != 0) {
                JSONObject jsonObject = new JSONObject();

                Result result = new Result();
                result.setBookingId(Integer.valueOf(booking_id));
                result.setSenderID(Integer.valueOf(providerID));
                result.setReceiverID(Integer.valueOf(userID));
                result.setTime("" + System.currentTimeMillis());
                result.setContent(messageInput.getText().toString());
                result.setContentType("text");
                result.setType("sender");
                try {
                    jsonObject.put("booking_id", booking_id);
                    jsonObject.put("sender_id", providerID);
                    jsonObject.put("reciever_id", userID);
                    jsonObject.put("message_id", UUID.randomUUID());
                    jsonObject.put("Time", System.currentTimeMillis());
                    jsonObject.put("content", messageInput.getText().toString());
                    jsonObject.put("content_type", "text");
                    emitters.sendMessage(jsonObject);
                    jsonObject.put("type", "sender");
                    updateMessages(result);

                } catch (Exception e) {
                    e.printStackTrace();

                }
                messageInput.setText("");
                linearLayoutManager.scrollToPosition(0);
            } else {
                sendLayout.startAnimation(AnimationUtils.loadAnimation(ChatActivity.this, R.anim.shake_error));
            }
        }
    }


    public void updateMessages(Result result) {
        if (chatsAdapter.getItemCount()>0) {
            List<Result> finalArray = chatViewModel.addValuesinStart(chatsAdapter.getItems(), result);
            chatsAdapter.swapItems(finalArray);
        } else {
            messageArray.add(result);
            setAdapterValues();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}