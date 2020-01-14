package com.app.jobfizzer.View.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.app.jobfizzer.Utilities.ConnectionUtils;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.Constants.Constants;
import com.app.jobfizzer.Utilities.helpers.CustomLibraries.CircleIndicator;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.R;
import com.app.jobfizzer.View.fragments.PageFragment;


import static com.app.jobfizzer.Utilities.Constants.Constants.FINE_LOCATION_PERMISSIONS;

/**
 * Created by karthik on 01/10/17.
 */

public class OnboardActivity extends BaseActivity {
    String[] titles, subtitles;
    int[] backgrounds;
    AppSettings appSettings = new AppSettings(OnboardActivity.this);
    private Button signIn, signUp, skipLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_onboard);
        initViewsValues();
        initListeners();
        checkForpermissions();
    }

    private void checkForpermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSIONS);
            }
        }
    }

    private void initListeners() {

        skipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionUtils.isNetworkConnected(OnboardActivity.this)) {
                    moveMainActivity();
                } else {
                    Utils.toast(findViewById(android.R.id.content), getResources().getString(R.string.no_internet_connection));
                }
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signIn = new Intent(OnboardActivity.this, SignInActivity.class);
                startActivity(signIn);
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(OnboardActivity.this, SignUpActivity.class);
                startActivity(signUp);
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
            }
        });
    }

    private void moveMainActivity() {
        appSettings.setUserType("guest");
        Intent intent = new Intent(OnboardActivity.this, MainActivity.class);
        intent.putExtra(Constants.LOGIN_TYPE, Constants.HOME_TAB);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }

    private void initViewsValues() {
        titles = new String[]{getResources().getString(R.string.jobfizzer), getResources().getString(R.string.photographer), getResources().getString(R.string.interior_design), getResources().getString(R.string.beautician)};
        subtitles = new String[]{getResources().getString(R.string.text_one), getResources().getString(R.string.text_two), getResources().getString(R.string.text_three), getResources().getString(R.string.text_four)};
        backgrounds = new int[]{R.drawable.onboard_1, R.drawable.onboard_2, R.drawable.onboard_3, R.drawable.onboard_4};
        ViewPager pager = findViewById(R.id.onBoardPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setOffscreenPageLimit(1);
        pager.setAdapter(adapter);
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        signIn = findViewById(R.id.signIn);
        signUp = findViewById(R.id.signUp);
        skipLogin = findViewById(R.id.skipLogin);


    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(titles[position], subtitles[position], backgrounds[position], position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_PERMISSIONS) {
            if (grantResults.length > 0) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
        }
    }
}
