package com.alexwglenn.whatcard;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alexwglenn.whatcard.model.AddCardResponse;
import com.alexwglenn.whatcard.model.AuthorizeResponse;
import com.alexwglenn.whatcard.model.Card;
import com.alexwglenn.whatcard.model.CategoryRate;
import com.alexwglenn.whatcard.viewholders.AddCategoryRateViewHolder;

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

    @InjectView(R.id.scroll)
    public ScrollView scroll;
    @InjectView(R.id.card_name)
    public EditText cardName;
    @InjectView(R.id.bank_name)
    public EditText bankName;
    @InjectView(R.id.cardRateTitle)
    public TextView cardRateTitle;
    @InjectView(R.id.cardRateSeek)
    public SeekBar cardRateSeek;

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
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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

        cardRateSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float cardRate = (float)i / 4;
                cardRateTitle.setText("Card Reward Rate: " + cardRate + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return v;
    }

    private void selectButton(Button button) {

        blueButton.setSelected(false);
        purpleButton.setSelected(false);
        greenButton.setSelected(false);
        orangeButton.setSelected(false);
        redButton.setSelected(false);
        greyButton.setSelected(false);

        if (button == blueButton) {
            blueButton.setSelected(true);
        } else if (button == purpleButton) {
            purpleButton.setSelected(true);
        } else if (button == greenButton) {
            greenButton.setSelected(true);
        } else if (button == orangeButton) {
            orangeButton.setSelected(true);
        } else if (button == redButton) {
            redButton.setSelected(true);
        } else if (button == greyButton) {
            greyButton.setSelected(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(blueButton)) {
            currentColor = getResources().getColor(R.color.light_blue);
            selectButton(blueButton);
        } else if (v.equals(purpleButton)) {
            currentColor = getResources().getColor(R.color.light_purple);
            selectButton(purpleButton);
        } else if (v.equals(greenButton)) {
            currentColor = getResources().getColor(R.color.light_green);
            selectButton(greenButton);
        } else if (v.equals(orangeButton)) {
            currentColor = getResources().getColor(R.color.light_orange);
            selectButton(orangeButton);
        } else if (v.equals(redButton)) {
            currentColor = getResources().getColor(R.color.light_red);
            selectButton(redButton);
        } else if (v.equals(greyButton)) {
            currentColor = getResources().getColor(R.color.light_grey);
            selectButton(greyButton);
        } else if (v.equals(addAnother)) {
            View newView = mInflater.inflate(R.layout.add_category_rate_sublayout, null);
            int insertIndex = categoryLayout.getChildCount() - 1;
            categoryLayout.addView(newView, insertIndex);

            float lastPercentage = 0;
            String lastStartDate = "";
            String lastEndDate = "";

            for (int index = 0; index < categoryLayout.getChildCount() - 1; index++) {
                AddCategoryRateViewHolder viewHolder = new AddCategoryRateViewHolder(categoryLayout.getChildAt(index));

                if (lastPercentage != 0) {
                    viewHolder.setCategoryRate(lastPercentage);
                }

                if (lastStartDate != "") {
                    viewHolder.startDate.setText(lastStartDate);
                }

                if (lastEndDate != "") {
                    viewHolder.endDate.setText(lastEndDate);
                }

                viewHolder.startDate.setOnClickListener(new CardDatePicker(getActivity(), viewHolder.startDate));
                viewHolder.endDate.setOnClickListener(new CardDatePicker(getActivity(), viewHolder.endDate));

                lastPercentage = viewHolder.getCategoryRate();
                lastStartDate = viewHolder.startDate.getText().toString();
                lastEndDate = viewHolder.endDate.getText().toString();
            }

        } else if(v.equals(addCard)) {
            RealmList<CategoryRate> rates = new RealmList<CategoryRate>();

            for (int index = 0; index < categoryLayout.getChildCount() - 1; index++) {
                AddCategoryRateViewHolder viewHolder = new AddCategoryRateViewHolder(categoryLayout.getChildAt(index));
                if (!viewHolder.categoryName.getText().toString().equals("")) {

                    String categoryOrStore = viewHolder.categoryName.getText().toString();

                    String category = "";
                    String store = "";

                    if (viewHolder.isStore.isChecked()) {
                        store = categoryOrStore;
                    } else {
                        category = categoryOrStore;
                    }

                    CategoryRate cRate = new CategoryRate(store, category, viewHolder.getCategoryRate(), new Date(), new Date());
                    rates.add(cRate);
                }
            }

            float cardRate = (float)cardRateSeek.getProgress() / 4;

            addingCard = new Card(cardName.getText().toString(), bankName.getText().toString(), cardRate, rates, currentColor);
            String colorString = Integer.toHexString(currentColor);

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
        dismissAllowingStateLoss();
    }
}
