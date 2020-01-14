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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.jobfizzer.Model.ViewBookingsResponseModel.AllBooking;
import com.app.jobfizzer.Model.ViewBookingsResponseModel.BookingResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.Events.Status;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.View.adapters.BookingsAdapter;
import com.app.jobfizzer.ViewModel.BookingsViewModel;
import com.app.jobfizzer.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class BookingsFragment extends Fragment {
    private static String TAG = BookingsFragment.class.getSimpleName();
    public List<AllBooking> bookingsArray = new ArrayList<>();
    public RelativeLayout empty_layout;
    public SwipeRefreshLayout swipeView;
    public RecyclerView bookingsList;
    RelativeLayout guest_login;
    AppSettings appSettings;
    View view;
    BookingsViewModel bookingsViewModel;

    public BookingsFragment() {

    }

    public static BookingsFragment newInstance(String param1, String param2) {

        return new BookingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bookings, container, false);
        bookingsViewModel = ViewModelProviders.of(this).get(BookingsViewModel.class);
        initViews(view);

        return view;
    }

    public void getData() {
        InputForAPI inputForAPI = new InputForAPI(getActivity());
        inputForAPI.setUrl(UrlHelper.VIEW_BOOKINGS);
        inputForAPI.setFile(null);
        inputForAPI.setHeaders(ApiCall.getHeaders(getActivity()));
        getValues(inputForAPI);

    }

    private void handleSuccessResponse(BookingResponseModel response) {
        if (swipeView.isRefreshing()) {
            swipeView.setRefreshing(false);
        }
        bookingsArray = response.getAllBookings();
        if (bookingsArray != null && bookingsArray.size() > 0) {
            empty_layout.setVisibility(View.GONE);
            bookingsList.setVisibility(View.VISIBLE);
            BookingsAdapter adapter = new BookingsAdapter(getActivity(), bookingsArray, view);
            bookingsList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            empty_layout.setVisibility(View.VISIBLE);
            bookingsList.setVisibility(View.GONE);

        }
    }

    private void getValues(InputForAPI inputForAPI) {

        bookingsViewModel.getBookingList(inputForAPI).observe(this,
                new Observer<BookingResponseModel>() {
                    @Override
                    public void onChanged(@Nullable BookingResponseModel bookingResponseModel) {
                        handleSuccessResponse(bookingResponseModel);
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStatus(Status event) {
        getData();
    }


    private void initViews(View view) {
        appSettings = new AppSettings(getActivity());
        bookingsList = view.findViewById(R.id.bookingsList);
        empty_layout = view.findViewById(R.id.empty_layout);
        guest_login = view.findViewById(R.id.guest_login);
        swipeView = view.findViewById(R.id.swipeView);
        bookingsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (appSettings.getUserType().equalsIgnoreCase("guest")) {
            guest_login.setVisibility(View.VISIBLE);
            swipeView.setVisibility(View.GONE);
            empty_layout.setVisibility(View.GONE);
        } else {
            swipeView.setVisibility(View.VISIBLE);
            guest_login.setVisibility(View.GONE);
            getData();
        }
    }
}
