package com.app.jobfizzer.View.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.jobfizzer.Model.CategoryResponseModel.Banner_images;
import com.app.jobfizzer.Model.CategoryResponseModel.CategoryResponse;
import com.app.jobfizzer.Model.CategoryResponseModel.List_category;
import com.app.jobfizzer.Model.FavouriteBannerImages;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.helpers.Interfaces.onDeleteClicked;
import com.app.jobfizzer.Utilities.helpers.Interfaces.onFavourite;
import com.app.jobfizzer.Utilities.helpers.SpacesItemDecoration;
import com.app.jobfizzer.View.activities.MainActivity;
import com.app.jobfizzer.View.adapters.FavouriteSliderAdapter;
import com.app.jobfizzer.View.adapters.HomePageCategoryAdapter;
import com.app.jobfizzer.View.adapters.SliderAdapter;
import com.app.jobfizzer.ViewModel.HomePageViewModel;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.app.jobfizzer.R;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView categoriesView;
    RecyclerViewPager viewpager;
    ViewPager dviewpager;
    MainActivity activity;
    SwipeRefreshLayout refreshLayout;
    HomePageViewModel homePageViewModel;
    private List<FavouriteBannerImages> faveList = new ArrayList<>();
    private String TAG = HomeFragment.class.getSimpleName();
    private List<Banner_images> bannerArray = new ArrayList<>();
    private PageIndicatorView circleIndicator;
    private LinearLayoutManager layout;
    private HomePageCategoryAdapter adapter;
    List<List_category> list_categories = new ArrayList<>();
    List<List_category> finalListCategories;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {

        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity = (MainActivity) getContext();
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        getData();
        getFavorite();
    }

    public void setFavourite(List<FavouriteBannerImages> faveList) {
        if (faveList.size() == 0) {
            if (bannerArray != null) {
                if (bannerArray.size() != 0) {
                    viewpager.setAdapter(new SliderAdapter(activity, bannerArray, false));
                } else {
                    loadBannerArray();
                }
            } else {
                loadBannerArray();
            }
        } else {
            viewpager.setAdapter(new FavouriteSliderAdapter(activity, faveList, true));
        }
    }

    private void getFavorite() {
        homePageViewModel.getBanner().observe(this, new Observer<List<FavouriteBannerImages>>() {
            @Override
            public void onChanged(@Nullable List<FavouriteBannerImages> favouriteBannerImages) {
                faveList = favouriteBannerImages;
                setFavourite(faveList);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        faveList = new ArrayList<>();
        initViews(view);
        return view;

    }

    private void loadBannerArray() {

        InputForAPI inputForAPI = new InputForAPI(getActivity());
        inputForAPI.setUrl(UrlHelper.HOME_DASH_BOARD_NEW);
        inputForAPI.setFile(null);
        inputForAPI.setHeaders(new HashMap<String, String>());
        getBannerArray(inputForAPI);
    }

    private void getBannerArray(InputForAPI inputForAPI) {

        homePageViewModel.getListCategories(inputForAPI).observe(this, new Observer<CategoryResponse>() {
            @Override
            public void onChanged(@Nullable CategoryResponse categoryResponse) {
                try {
                    refreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (categoryResponse != null) {
                    handleBannerResponse(categoryResponse);
                }
            }
        });

    }

    private void handleBannerResponse(CategoryResponse response) {
        if (response.getBanner_images() != null) {
            bannerArray = response.getBanner_images();
            viewpager.setAdapter(new SliderAdapter(activity, bannerArray, false));
        } else {
        }

    }

    private void getData() {

        bannerArray = new ArrayList<>();
        InputForAPI inputForAPI = new InputForAPI(getActivity());
        inputForAPI.setUrl(UrlHelper.HOME_DASH_BOARD_NEW);
        inputForAPI.setFile(null);
        inputForAPI.setHeaders(new HashMap<String, String>());

        getValues(inputForAPI);

    }

    private void getValues(InputForAPI inputForAPI) {

        homePageViewModel.getListCategories(inputForAPI).observe(this, new Observer<CategoryResponse>() {
            @Override
            public void onChanged(@Nullable CategoryResponse categoryResponse) {
                try {
                    refreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handleCategoryResponse(categoryResponse);
            }
        });
    }

    private void handleCategoryResponse(CategoryResponse response) {
        setAdapterValues(response);
    }

    private void setAdapterValues(final CategoryResponse response) {
        if (response.getList_category() != null) {
            finalListCategories = new ArrayList<>();
            list_categories = response.getList_category();
            int totalSize = list_categories.size();

            for (int i = 0; i < totalSize; i++) {
                final List_category list_category = list_categories.get(i);
                list_category.setIsLiked(homePageViewModel.getLikedStatus("" + list_category.getId(), true));
                finalListCategories.add(list_category);
            }
            setCategoryAdapter(finalListCategories);

            getFavorite();
        } else {
        }
    }

    private void getIsLikedStatus(int adapterPosition) {
        final List_category list_category = finalListCategories.get(adapterPosition);
        list_category.setIsLiked(homePageViewModel.getLikedStatus("" + list_category.getId(), true));
        finalListCategories.set(adapterPosition, list_category);

    }

    private void setCategoryAdapter(List<List_category> finalListCategory) {
        if (adapter == null) {
            categoriesView.addItemDecoration(new SpacesItemDecoration(14));
        }
        int NUM_COLUMNS = 3;
        categoriesView.setLayoutManager(new GridLayoutManager(activity, NUM_COLUMNS));
        adapter = new HomePageCategoryAdapter(activity, finalListCategory, homePageViewModel);
        categoriesView.setAdapter(adapter);
        adapter.onDeleteClicked(new onDeleteClicked() {
            @Override
            public void deleted(String id, final int adapterPosition) {
                homePageViewModel.deleteFav(id, true).observe(HomeFragment.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean success) {
                        if (success != null) {
                            if (success) {
                                getIsLikedStatus(adapterPosition);
                                adapter.notifyDataSetChanged();
                                getFavorite();
                            }
                        }
                    }
                });
            }
        });

        adapter.onFavourite(new onFavourite() {
            @Override
            public void liked(List_category listCategory, final int adapterPosition) {
                homePageViewModel.setLiked(listCategory).observe(HomeFragment.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean success) {
                        if (success != null) {
                            if (success) {
                                getIsLikedStatus(adapterPosition);
                                adapter.notifyDataSetChanged();
                                getFavorite();
                            }
                        }
                    }
                });
            }
        });
    }

    private void initViews(View view) {
        categoriesView = view.findViewById(R.id.categoriesView);
        viewpager = view.findViewById(R.id.viewpager);
        dviewpager = view.findViewById(R.id.dviewpager);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        circleIndicator = view.findViewById(R.id.circleIndicator);
        circleIndicator.setDynamicCount(true);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        homePageViewModel = ViewModelProviders.of(this).get(HomePageViewModel.class);
        layout = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        viewpager.setLayoutManager(layout);

        viewpager.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int scrollState) {
                if (scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                    int c = layout.findFirstCompletelyVisibleItemPosition();
                    dviewpager.setCurrentItem(c);
                    circleIndicator.setSelection(c);
                }
            }

            @Override
            public void onScrolled(@NonNull final RecyclerView recyclerView, int i, int i2) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int childCount = viewpager.getChildCount();
                        int width = viewpager.getChildAt(0).getWidth();
                        int padding = (viewpager.getWidth() - width) / 2;
                        for (int j = 0; j < childCount; j++) {
                            View v = recyclerView.getChildAt(j);
                            float rate = 0;
                            if (v.getLeft() <= padding) {
                                if (v.getLeft() >= padding - v.getWidth()) {
                                    rate = (padding - v.getLeft()) * 1f / v.getWidth();
                                } else {
                                    rate = 1;
                                }
                                v.setScaleY(1 - rate * 0.1f);
                                v.setScaleX(1 - rate * 0.1f);
                            } else {
                                if (v.getLeft() <= recyclerView.getWidth() - padding) {
                                    rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                                }
                                v.setScaleY(0.9f + rate * 0.1f);
                                v.setScaleX(0.9f + rate * 0.1f);
                            }
                        }
                    }
                });


            }
        });

        viewpager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (viewpager.getChildCount() < 3) {
                    if (viewpager.getChildAt(1) != null) {
                        if (viewpager.getCurrentPosition() == 0) {
                            View v1 = viewpager.getChildAt(1);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        } else {
                            View v1 = viewpager.getChildAt(0);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        }
                    }
                } else {
                    if (viewpager.getChildAt(0) != null) {
                        View v0 = viewpager.getChildAt(0);
                        v0.setScaleY(0.9f);
                        v0.setScaleX(0.9f);
                    }
                    if (viewpager.getChildAt(2) != null) {
                        View v2 = viewpager.getChildAt(2);
                        v2.setScaleY(0.9f);
                        v2.setScaleX(0.9f);
                    }
                }

            }
        });

    }


}
