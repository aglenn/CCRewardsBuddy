package com.alexwglenn.whatcard;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.alexwglenn.whatcard.model.Card;
import com.alexwglenn.whatcard.model.CategoryRate;
import com.alexwglenn.whatcard.model.RewardCategory;
import com.alexwglenn.whatcard.util.DatabaseManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class RewardCategoryFragment extends Fragment implements AbsListView.OnItemClickListener, CategoryUpdateListener {

    private static String TAG = "Reward Category Fragment";

    @Inject
    DatabaseManager mDatabaseManager;

    @InjectView(R.id.rewardlist)
    public RecyclerView mRecyclerView;

    @InjectView(R.id.fab)
    public FloatingActionButton fab;

    private ArrayList<String> mRewardCategories;
    private ArrayList<Card> mCards;
    private RewardCategoryAdapter mAdapter;

    public static RewardCategoryFragment newInstance() {
        RewardCategoryFragment fragment = new RewardCategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RewardCategoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((WhatCard) getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reward_category_grid, container, false);

        // Set the adapter
        ButterKnife.inject(this,view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mDatabaseManager.addCategoryUpdateListener(this);

        ArrayList<RewardCategory> rewardList = new ArrayList<>();
        mAdapter = new RewardCategoryAdapter(rewardList, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVisible()) {
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    Fragment prev = getChildFragmentManager().findFragmentByTag(AddCardFragment.class.getSimpleName());
                    if(prev != null) {
                        ft.remove(prev).commit();
                        ft = getChildFragmentManager().beginTransaction();
                    }
                    ft.addToBackStack(null);

                    AddCategoryFragment addCategoryDialog = AddCategoryFragment.newInstance();
                    addCategoryDialog.show(getChildFragmentManager(), AddCategoryFragment.class.getSimpleName());
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDatabaseManager.addCategoryUpdateListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mDatabaseManager.removeCategoryUpdateListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void updateRewards() {

        if (mCards == null || mRewardCategories == null) {
            return;
        }

        for (Card c : mCards) {
            if (c.getCategoryRates() == null) {
                continue;
            }
            for (CategoryRate rate : c.getCategoryRates()) {
                String category = rate.getCategoryName();
                if (category.equals("")) {
                    category = rate.getStoreName();
                }
                mRewardCategories.add(category);
            }
        }

        List<String> uniqueCats = new ArrayList<>(new HashSet<String>(mRewardCategories));
        Collections.sort(uniqueCats,String.CASE_INSENSITIVE_ORDER);

        Map<String,RewardCategory> rewardCats = new HashMap<>();
        for (String categoryName : uniqueCats) {
            for (Card card : mCards) {
                // check the base rate of the card first
                if(rewardCats.get(categoryName) != null) {
                    RewardCategory rCat = rewardCats.get(categoryName);
                    if (card.getBasePercentage() > rCat.bestRate) {
                        rCat.bestCard = card;
                        rCat.bestRate = card.getBasePercentage();
                        rewardCats.put(categoryName, rCat);
                    }
                } else {
                    RewardCategory rCat = new RewardCategory(categoryName, card.getBasePercentage(), card);
                    rewardCats.put(categoryName, rCat);
                }

                if (card.getCategoryRates() == null) {
                    continue;
                }

                // Now check any rotating categories or special ones
                for (CategoryRate rate : card.getCategoryRates()) {

                    boolean inRange = false;
                    if (rate.getStartDate().equals("") && rate.getEndDate().equals("")) { // always valid
                        inRange = true;
                    } else {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        // check if these are even valid right now
                        try {
                            Date startDate = format.parse(rate.getStartDate());
                            Date endDate = format.parse(rate.getEndDate());
                            Date now = new Date();

                            if (now.after(startDate) && now.before(endDate)) {
                                inRange = true;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (!inRange) {
                        continue;
                    }

                    String category = rate.getCategoryName();
                    if (category.equals("")) {
                        category = rate.getStoreName();
                    }
                    if (category.equals(categoryName)) {
                        if(rewardCats.get(categoryName) != null) {
                            RewardCategory rCat = rewardCats.get(categoryName);
                            if (rate.getRewardRate() > rCat.bestRate) {
                                rCat.bestCard = card;
                                rCat.bestRate = rate.getRewardRate();
                                rewardCats.put(categoryName, rCat);
                            }
                        } else {
                            RewardCategory rCat = new RewardCategory(categoryName, rate.getRewardRate(), card);
                            rewardCats.put(categoryName, rCat);
                        }
                    }
                }
            }
        }

        ArrayList<RewardCategory> rewardList = new ArrayList<>(rewardCats.values());
        mAdapter.setRewardCategories(rewardList);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void categoriesUpdated(ArrayList<String> categories) {
        mRewardCategories = categories;
        updateRewards();
    }

    @Override
    public void userCardsUpdated(ArrayList<Card> cards) {
        mCards = cards;
        updateRewards();
    }
}
