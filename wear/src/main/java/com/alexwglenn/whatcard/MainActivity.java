package com.alexwglenn.whatcard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alexwglenn.whatcard.model.Card;
import com.alexwglenn.whatcard.model.CategoryRate;
import com.alexwglenn.whatcard.model.RewardCategory;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Subscribe;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity {

//    @InjectView(R.id.textV)
//    public TextView mTextView;
    @InjectView(R.id.gridView)
    GridViewPager gridViewPager;
    @InjectView(R.id.page_indicator)
    DotsPageIndicator pageIndicator;
    @InjectView(R.id.no_data)
    TextView noData;

    private RewardCategoryAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        updateRewards();

        gridViewPager.setAdapter(mAdapter);
        pageIndicator.setPager(gridViewPager);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Subscribe
    public void onCategoriesUpdated(CategoryUpdatedEvent event) {
        updateRewards();
    }

    private void updateRewards() {

        final Activity finalThis = this;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(finalThis);
                String categoriesJson = preferences.getString("RewardCategories", "");

                Log.d("Main", "Loaded string: " + categoriesJson);

                Type categoryListType = new TypeToken<ArrayList<RewardCategory>>() {}.getType();

                ArrayList<RewardCategory> categoryList = gson.fromJson(categoriesJson, categoryListType);
                if (categoryList == null) {
                    categoryList = new ArrayList<>();
                }

                Log.d("Main", "Loading adapter with " + categoryList.size() + " objects");

                if (categoryList.size() > 0) {
                    noData.setVisibility(View.GONE);
                    if (mAdapter == null) {
                        mAdapter = new RewardCategoryAdapter(categoryList, finalThis);
                    } else {
                        mAdapter.setRewardCategories(categoryList);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    noData.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
