package com.alexwglenn.ccrewardsbuddy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;


import com.alexwglenn.ccrewardsbuddy.model.Card;
import com.alexwglenn.ccrewardsbuddy.model.CardsAddedEvent;
import com.alexwglenn.ccrewardsbuddy.model.CardsDeletedEvent;
import com.alexwglenn.ccrewardsbuddy.model.CardsUpdatedEvent;
import com.alexwglenn.ccrewardsbuddy.model.CategoryRate;
import com.alexwglenn.ccrewardsbuddy.model.RewardCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shamanland.fab.FloatingActionButton;
import com.squareup.otto.Subscribe;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RewardCategoryFragment extends Fragment implements AbsListView.OnItemClickListener {

    /**
     * The fragment's ListView/GridView.
     */
    @InjectView(R.id.rewardlist)
    public AbsListView mListView;

    @InjectView(R.id.fab)
    public FloatingActionButton fab;


    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
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

        if (getArguments() != null) {
        }

        updateRewards();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reward_category, container, false);

        // Set the adapter
        ButterKnife.inject(this,view);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Subscribe
    public void onCardsUpdated(CardsUpdatedEvent event) {
        updateRewards();
    }

    @Subscribe public void onCardsDeleted(CardsDeletedEvent event) {
        updateRewards();
    }

    @Subscribe public void onCardsAdded(CardsAddedEvent event) {
        updateRewards();
    }

    private void updateRewards() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String cardsJson = preferences.getString("Cards", "[{\"name\":\"Chase Freedom\",\"basePercentage\":0.01,\"categoryRates\":[{\"Grocery Stores\":0.05},{\"Movie Theatres\":0.05}]},{\"name\":\"Capital One Quicksilver\",\"basePercentage\":0.015, \"categoryRates\":[]}]");
        String cardsJson = preferences.getString("Cards", "");

        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<Card>>() {}.getType();
        List<Card> cards = gson.fromJson(cardsJson, listType);
        if (cards == null) {
            cards = new ArrayList<Card>();
        }

        List<String> cats = new ArrayList<>();

        cats.add("Home Improvement");
        cats.add("Grocery Stores");
        cats.add("Restaurants");
        cats.add("Clothing");
        cats.add("Pet Supplies");
        cats.add("Online Shopping");
        cats.add("Other");

        for (Card c : cards) {
            for (CategoryRate rate : c.categoryRates) {
                cats.add(rate.categoryName);
            }
        }

        List<String> uniqueCats = new ArrayList<>(new HashSet<String>(cats));
        Collections.sort(uniqueCats);

        Map<String,RewardCategory> rewardCats = new HashMap<>();
        for (String categoryName : uniqueCats) {
            for (Card card : cards) {

                // check the base rate of the card first
                if(rewardCats.get(categoryName) != null) {
                    RewardCategory rCat = rewardCats.get(categoryName);
                    if (card.basePercentage > rCat.bestRate) {
                        rCat.bestCard = card;
                        rCat.bestRate = card.basePercentage;
                        rewardCats.put(categoryName, rCat);
                    }
                } else {
                    RewardCategory rCat = new RewardCategory(categoryName, card.basePercentage, card);
                    rewardCats.put(categoryName, rCat);
                }

                // Now check any rotating categories or special ones
                for (CategoryRate rate : card.categoryRates) {
                    if (rate.categoryName.equals(categoryName)) {
                        if(rewardCats.get(categoryName) != null) {
                            RewardCategory rCat = rewardCats.get(categoryName);
                            if (rate.categoryPercentage > rCat.bestRate) {
                                rCat.bestCard = card;
                                rCat.bestRate = rate.categoryPercentage;
                                rewardCats.put(categoryName, rCat);
                            }
                        } else {
                            RewardCategory rCat = new RewardCategory(categoryName, rate.categoryPercentage, card);
                            rewardCats.put(categoryName, rCat);
                        }
                    }
                }
            }
        }

        ArrayList<RewardCategory> rewardList = new ArrayList<>(rewardCats.values());

        if (mAdapter == null) {
            mAdapter = new RewardCategoryAdapter(rewardList, getActivity());
        } else {
            mAdapter.setRewardCategories(rewardList);
            mAdapter.notifyDataSetChanged();
        }
    }


}
