package com.app.jobfizzer.View.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.app.jobfizzer.Model.SubCategoryResponseModel.List_subcategory;
import com.app.jobfizzer.Model.SubCategoryResponseModel.SubCategoryResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.Interfaces.onDeleteClicked;
import com.app.jobfizzer.Utilities.helpers.Interfaces.onSubFavourite;
import com.app.jobfizzer.Utilities.helpers.SpacesItemDecoration;
import com.app.jobfizzer.View.adapters.SubCategoriesAdapter;
import com.app.jobfizzer.ViewModel.HomePageViewModel;
import com.app.jobfizzer.ViewModel.SubCategoryViewModel;
import com.app.jobfizzer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubCategoriesActivity extends BaseActivity {

    private static final int NUM_COLUMNS = 2;
    List<List_subcategory> subcategoriesArray = new ArrayList<>();
    String id;
    SubCategoryViewModel subCategoryViewModel;
    private RecyclerView subCategories;
    HomePageViewModel homePageViewModel;
    SubCategoriesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categories);
        subCategoryViewModel = ViewModelProviders.of(this).get(SubCategoryViewModel.class);
        initViews();
        getIntentValues();
        try {
            getSubCategory();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        homePageViewModel = ViewModelProviders.of(this).get(HomePageViewModel.class);
        subCategories = findViewById(R.id.subCategories);
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getIntentValues() {
        id = getIntent().getStringExtra("subCategoryId");
    }


    private void getSubCategory() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        AppSettings appSettings = new AppSettings(SubCategoriesActivity.this);
        appSettings.setCategoryId(id);
        InputForAPI inputForAPI = new InputForAPI(SubCategoriesActivity.this);
        inputForAPI.setUrl(UrlHelper.LIST_SUB_CATEGORY);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(SubCategoriesActivity.this));
        getSubCategoryValue(inputForAPI);


    }

    private void getSubCategoryValue(InputForAPI inputForAPI) {
        subCategoryViewModel.getListSubCategories(inputForAPI).observe(this, new Observer<SubCategoryResponseModel>() {
            @Override
            public void onChanged(@Nullable SubCategoryResponseModel subCategoryResponseModel) {
                if (subCategoryResponseModel != null) {
                    handleSuccessResponse(subCategoryResponseModel);
                }
            }
        });
    }

    private void handleSuccessResponse(SubCategoryResponseModel response) {
        if (response.getList_subcategory() != null) {
            subcategoriesArray = response.getList_subcategory();

            for (int i = 0; i < subcategoriesArray.size(); i++) {
                final List_subcategory list_category = subcategoriesArray.get(i);
                list_category.setIsLiked(homePageViewModel.getLikedStatus("" + list_category.getId(), false));
                subcategoriesArray.set(i, list_category);
            }

            subCategories.addItemDecoration(new SpacesItemDecoration(14));
            subCategories.setLayoutManager(new GridLayoutManager(SubCategoriesActivity.this, NUM_COLUMNS));
            adapter = new SubCategoriesAdapter(SubCategoriesActivity.this, subcategoriesArray);
            subCategories.setAdapter(adapter);

            adapter.onDeleteClicked(new onDeleteClicked() {
                @Override
                public void deleted(String id, final int adapterPosition) {
                    homePageViewModel.deleteFav(id, false).observe(SubCategoriesActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(@Nullable Boolean aBoolean) {
                            if (aBoolean != null) {
                                if (aBoolean) {
                                    getIsLikedStatus(adapterPosition);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
                }
            });

            adapter.onFavourite(new onSubFavourite() {
                @Override
                public void liked(List_subcategory listCategory, final int adapterPosition) {
                    homePageViewModel.setSubLiked(listCategory).observe(SubCategoriesActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(@Nullable Boolean aBoolean) {
                            if (aBoolean != null) {
                                if (aBoolean) {
                                    getIsLikedStatus(adapterPosition);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
                }
            });
        }
    }


    private void getIsLikedStatus(int adapterPosition) {
        final List_subcategory list_category = subcategoriesArray.get(adapterPosition);
        list_category.setIsLiked(homePageViewModel.getLikedStatus("" + list_category.getId(), false));
        subcategoriesArray.set(adapterPosition, list_category);
    }
}