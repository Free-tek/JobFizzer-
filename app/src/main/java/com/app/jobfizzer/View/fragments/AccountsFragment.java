package com.app.jobfizzer.View.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Model.UserDetails;
import com.app.jobfizzer.Model.ViewProfileResponseModel;
import com.app.jobfizzer.Utilities.Animationhelper;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.ConnectionUtils;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.View.activities.AboutUs;
import com.app.jobfizzer.View.activities.AddressActivity;
import com.app.jobfizzer.View.activities.ChangePasswordActivity;
import com.app.jobfizzer.View.activities.EditProfileActivity;
import com.app.jobfizzer.View.activities.MainActivity;
import com.app.jobfizzer.View.activities.SignInActivity;
import com.app.jobfizzer.View.adapters.ThemeAdapter;
import com.app.jobfizzer.ViewModel.AccountViewModel;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.app.jobfizzer.R;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AccountsFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public TextView userName, userMobile;
    public ImageView profilePic;
    LinearLayout logOut;
    AppSettings appSettings;
    TextView logOutText;
    LinearLayout changeProfile;
    LinearLayout viewEditAddress;
    LinearLayout changePassword, guestLogin;
    GoogleApiClient mGoogleApiClient;
    MainActivity activity;
    AccountViewModel accountViewModel;
    Animationhelper animationhelper;
    private ImageView changeThemIcon;
    private RecyclerView changeThemeAdapter;
    private boolean themeOpen = false;
    private UserDetails userDetails;
    private LinearLayout changeTheme;
    private View changePasswordView;

    public AccountsFragment() {
        // Required empty public constructor
    }

    public static AccountsFragment newInstance() {
        return new AccountsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity = (MainActivity) getContext();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);
        FacebookSdk.sdkInitialize(getApplicationContext());
        animationhelper = new Animationhelper();
        initViews(view);
        initListeners();
        setLayoutChanges();
        getProfileValues();
        initAdapter();
        return view;
    }

    public void moveChangeProfile() {
        Intent intent = new Intent(activity, EditProfileActivity.class);
        intent.putExtra("provider_details", userDetails);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }

    private void moveChangePassword() {
        Intent intent = new Intent(activity, ChangePasswordActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }

    private void moveAddress() {
        Intent intent = new Intent(activity, AddressActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

    }


    private void initListeners() {

        changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionUtils.isNetworkConnected(activity)) {
                    handleChangeTheme();
                } else {
                    Utils.toast(activity.findViewById(android.R.id.content), getString(R.string.no_internet_connection));
                }
            }
        });


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ConnectionUtils.isNetworkConnected(activity)) {
                    moveChangePassword();
                } else {
                    Utils.toast(activity.findViewById(android.R.id.content), getString(R.string.no_internet_connection));
                }
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionUtils.isNetworkConnected(activity)) {
                    handleLogout();
                } else {
                    Utils.toast(activity.findViewById(android.R.id.content), getString(R.string.no_internet_connection));
                }
            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionUtils.isNetworkConnected(activity)) {
                    moveChangeProfile();
                } else {
                    Utils.toast(activity.findViewById(android.R.id.content), getString(R.string.no_internet_connection));
                }
            }
        });

        viewEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionUtils.isNetworkConnected(activity)) {
                    moveAddress();
                } else {
                    Utils.toast(activity.findViewById(android.R.id.content), getString(R.string.no_internet_connection));
                }
            }
        });

    }

    private void handleLogout() {
        if (!appSettings.getUserType().equalsIgnoreCase("guest")) {
            logoutFromApp();
        } else {
            moveSignInActivity();
        }
    }

    private void handleChangeTheme() {
        if (!themeOpen) {
            expand(changeThemeAdapter);
            themeOpen = true;
        } else {
            collapse(changeThemeAdapter);
            themeOpen = false;
        }
    }

    private void initAdapter() {

        List<Integer> allColors = Utils.getAllMaterialColors(activity);
        int numberOfColumns = 6;
        changeThemeAdapter.setLayoutManager(new GridLayoutManager(activity, numberOfColumns));
        ThemeAdapter adapter = new ThemeAdapter(activity, allColors);
        changeThemeAdapter.setAdapter(adapter);
    }

    private void getProfileValues() {

        InputForAPI inputForAPI = new InputForAPI(getActivity());
        inputForAPI.setUrl(UrlHelper.VIEW_PROFILE);
        inputForAPI.setFile(null);
        inputForAPI.setHeaders(ApiCall.getHeaders(getActivity()));
        getProfileData(inputForAPI);

    }

    private void getProfileData(InputForAPI inputForAPI) {

        accountViewModel.getViewProfile(inputForAPI).observe(this,
                new Observer<ViewProfileResponseModel>() {
                    @Override
                    public void onChanged(@Nullable ViewProfileResponseModel viewProfileResponseModel) {
                        if (viewProfileResponseModel != null) {
                            handlProfileDataResponse(viewProfileResponseModel);
                        }
                    }
                });
    }

    private void handlProfileDataResponse(ViewProfileResponseModel response) {
        userDetails = response.getUserDetails();
        setData();
    }

    public void expand(final View v) {
        changeThemIcon.setRotation(90);
        animationhelper.expand(v, activity);

    }

    public void collapse(final View v) {
        changeThemIcon.setRotation(0);
        animationhelper.collapse(v, activity);

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void setData() {
        if (appSettings.getUserType().equalsIgnoreCase("guest")) {
            userName.setText(R.string.hello);
            userMobile.setText(R.string.guest);
        } else {
            if (userDetails != null) {
                userName.setText(getFormattedName(userDetails.getFirstName(), userDetails.getLastName()));
                if (String.valueOf(userDetails.getMobile()).equalsIgnoreCase("")) {
                    userMobile.setText(String.valueOf(userDetails.getMobile()));
                } else {
                    userMobile.setText(" ");
                }
                ImageLoader imageLoader = new ImageLoader(getActivity());
                imageLoader.load(userDetails.getImage(), profilePic, Utils.getProfilePicture(getActivity()));
            }
        }

    }

    private String getFormattedName(String firstName, String lastName) {
        return accountViewModel.getFormattedName(firstName, lastName);
    }

    private void initViews(View view) {

        userName = view.findViewById(R.id.userName);
        changeProfile = view.findViewById(R.id.changeProfile);
        logOutText = view.findViewById(R.id.logOutText);
        userMobile = view.findViewById(R.id.userMobile);
        logOut = view.findViewById(R.id.logOut);
        profilePic = view.findViewById(R.id.account_profile_pic);
        guestLogin = view.findViewById(R.id.guestLogin);
        viewEditAddress = view.findViewById(R.id.viewEditAddress);
        changePasswordView = (View) view.findViewById(R.id.changePasswordView);
        changePassword = view.findViewById(R.id.changePassword);
        changeTheme = view.findViewById(R.id.changeTheme);
        changeThemIcon = view.findViewById(R.id.changeThemIcon);
        changeThemeAdapter = view.findViewById(R.id.changeThemeAdapter);
        appSettings = new AppSettings(activity);
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        Utils.setIconColour(activity, profilePic.getDrawable());
        initGeneral(view);
        initGoogle();

    }

    private void setLayoutChanges() {
        if (appSettings.getIsSocialLogin().equalsIgnoreCase("true")) {
            changePassword.setVisibility(View.GONE);
            changePasswordView.setVisibility(View.GONE);

        } else {
            changePassword.setVisibility(View.VISIBLE);
            changePasswordView.setVisibility(View.VISIBLE);
        }

        if (appSettings.getUserType().equalsIgnoreCase("guest")) {
            guestLogin.setVisibility(View.GONE);
            logOutText.setText(getResources().getString(R.string.sign_in));
        } else {
            guestLogin.setVisibility(View.VISIBLE);
            logOutText.setText(getResources().getString(R.string.logout));
        }
    }

    private void logout() {
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (mGoogleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        moveSignInActivity();
    }

    private void logoutFromApp() {
        InputForAPI inputForAPI = new InputForAPI(getActivity());
        inputForAPI.setUrl(UrlHelper.LOG_OUT);
        inputForAPI.setFile(null);
        inputForAPI.setHeaders(ApiCall.getHeaders(getActivity()));
        hitLogout(inputForAPI);
    }

    private void hitLogout(InputForAPI inputForAPI) {
        accountViewModel.hitLogout(inputForAPI);
        logout();
    }

    private void initGoogle() {

        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

    }

    private void initGeneral(View view) {

        final LinearLayout aboutUs;
        aboutUs = view.findViewById(R.id.aboutUs);
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ConnectionUtils.isNetworkConnected(activity)) {
                    Intent intent = new Intent(activity, AboutUs.class);
                    startActivity(intent);
                    activity.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
                } else {
                    Utils.toast(activity.findViewById(android.R.id.content), getString(R.string.no_internet_connection));
                }
            }
        });

    }

    private void moveSignInActivity() {
        appSettings.setIsLogged("false");
        Intent intent = new Intent(activity, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
