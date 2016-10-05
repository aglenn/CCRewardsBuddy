package com.alexwglenn.whatcard;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alexwglenn.whatcard.model.AddCardResponse;
import com.alexwglenn.whatcard.model.AuthorizeResponse;
import com.alexwglenn.whatcard.model.Card;
import com.alexwglenn.whatcard.model.CardsAddedEvent;
import com.alexwglenn.whatcard.model.CategoryRate;
import com.alexwglenn.whatcard.viewholders.AddCategoryRateViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Produce;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddCardFragment extends DialogFragment implements View.OnClickListener, Observer<Response<AddCardResponse>> {

    static final String TAG = "AddCardFragment";

    @Inject
    ThisCardService thisCardService;

    @InjectView(R.id.card_name)
    public EditText cardName;
    @InjectView(R.id.card_rate)
    public EditText cardRate;

    @InjectView(R.id.blue_button)
    public Button blueButton;
    @InjectView(R.id.purple_button)
    public Button purpleButton;
    @InjectView(R.id.green_button)
    public Button greenButton;
    @InjectView(R.id.orange_button)
    public Button orangeButton;
    @InjectView(R.id.red_button)
    public Button redButton;
    @InjectView(R.id.grey_button)
    public Button greyButton;

    @InjectView(R.id.category_layout)
    public LinearLayout categoryLayout;
    @InjectView(R.id.add_another)
    public Button addAnother;

    @InjectView(R.id.add_card)
    public Button addCard;
    @InjectView(R.id.cancel)
    public Button cancel;

    private LayoutInflater mInflater;
    private int currentColor;

    private Card addingCard;

    public static AddCardFragment newInstance() {
        AddCardFragment fragment = new AddCardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AddCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.fragment_add_card, container, false);

        ((WhatCard)getActivity().getApplication()).getComponent().inject(this);

        ButterKnife.inject(this, v);

        blueButton.setOnClickListener(this);
        purpleButton.setOnClickListener(this);
        greenButton.setOnClickListener(this);
        orangeButton.setOnClickListener(this);
        redButton.setOnClickListener(this);
        greyButton.setOnClickListener(this);
        addAnother.setOnClickListener(this);
        addCard.setOnClickListener(this);
        cancel.setOnClickListener(this);

        for (int index = 0; index < categoryLayout.getChildCount() - 1; index++) {
            AddCategoryRateViewHolder viewHolder = new AddCategoryRateViewHolder(categoryLayout.getChildAt(index));
            viewHolder.startDate.setOnClickListener(new CardDatePicker(getActivity(), viewHolder.startDate));
            viewHolder.endDate.setOnClickListener(new CardDatePicker(getActivity(), viewHolder.endDate));
        }

        mInflater = LayoutInflater.from(getActivity());

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(blueButton)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.light_blue));
//            }
            currentColor = getResources().getColor(R.color.light_blue);
            addCard.setTextColor(getResources().getColor(R.color.light_blue));
            addAnother.setTextColor(getResources().getColor(R.color.light_blue));
        } else if (v.equals(purpleButton)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.light_purple));
//            }
            currentColor = getResources().getColor(R.color.light_purple);
            addCard.setTextColor(getResources().getColor(R.color.light_purple));
            addAnother.setTextColor(getResources().getColor(R.color.light_purple));
        } else if (v.equals(greenButton)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.light_green));
//            }
            currentColor = getResources().getColor(R.color.light_green);
            addCard.setTextColor(getResources().getColor(R.color.light_green));
            addAnother.setTextColor(getResources().getColor(R.color.light_green));
        } else if (v.equals(orangeButton)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.light_orange));
//            }
            currentColor = getResources().getColor(R.color.light_orange);
            addCard.setTextColor(getResources().getColor(R.color.light_orange));
            addAnother.setTextColor(getResources().getColor(R.color.light_orange));
        } else if (v.equals(redButton)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.light_red));
//            }
            currentColor = getResources().getColor(R.color.light_red);
            addCard.setTextColor(getResources().getColor(R.color.light_red));
            addAnother.setTextColor(getResources().getColor(R.color.light_red));
        } else if (v.equals(greyButton)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.light_grey));
//            }
            currentColor = getResources().getColor(R.color.light_grey);
            addCard.setTextColor(getResources().getColor(R.color.light_grey));
            addAnother.setTextColor(getResources().getColor(R.color.light_grey));
        } else if (v.equals(addAnother)) {
            View newView = mInflater.inflate(R.layout.add_category_rate_sublayout, null);
            int insertIndex = categoryLayout.getChildCount() - 1;
            categoryLayout.addView(newView, insertIndex);

            for (int index = 0; index < categoryLayout.getChildCount() - 1; index++) {
                AddCategoryRateViewHolder viewHolder = new AddCategoryRateViewHolder(categoryLayout.getChildAt(index));
                viewHolder.startDate.setOnClickListener(new CardDatePicker(getActivity(), viewHolder.startDate));
                viewHolder.endDate.setOnClickListener(new CardDatePicker(getActivity(), viewHolder.endDate));
            }

        } else if(v.equals(addCard)) {
            RealmList<CategoryRate> rates = new RealmList<CategoryRate>();

            for (int index = 0; index < categoryLayout.getChildCount() - 1; index++) {
                AddCategoryRateViewHolder viewHolder = new AddCategoryRateViewHolder(categoryLayout.getChildAt(index));
                if (!viewHolder.categoryName.getText().toString().equals("") && !viewHolder.categoryRate.getText().toString().equals("")) {

                    String categoryOrStore = viewHolder.categoryName.getText().toString();

                    String category = "";
                    String store = "";

                    if (viewHolder.isStore.isChecked()) {
                        store = categoryOrStore;
                    } else {
                        category = categoryOrStore;
                    }

                    CategoryRate cRate = new CategoryRate(store, category, Float.parseFloat(viewHolder.categoryRate.getText().toString()), new Date(), new Date());
                    rates.add(cRate);
                }
            }

            addingCard = new Card(cardName.getText().toString(), Float.parseFloat(cardRate.getText().toString()), rates, currentColor);

            Realm realm = Realm.getDefaultInstance();
            AuthorizeResponse session = realm.where(AuthorizeResponse.class).findFirst();

            thisCardService.addUserCard(session.userID, addingCard, session.sessionKey)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(this);



        } else if(v.equals(cancel)) {
            dismissAllowingStateLoss();
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Response<AddCardResponse> addCardResponseResponse) {
        addingCard.setId(addCardResponseResponse.body().cardID);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(addingCard);
        realm.commitTransaction();
    }
}
