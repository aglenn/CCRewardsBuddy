package com.alexwglenn.whatcard;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alexwglenn.whatcard.model.Card;
import com.alexwglenn.whatcard.model.CardsAddedEvent;
import com.alexwglenn.whatcard.model.CategoryRate;
import com.alexwglenn.whatcard.viewholders.AddCategoryRateViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shamanland.fab.FloatingActionButton;
import com.squareup.otto.Produce;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddCardFragment extends DialogFragment implements View.OnClickListener{

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

        mInflater = LayoutInflater.from(getActivity());

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(blueButton)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.light_blue));
//            }
            currentColor = getResources().getColor(R.color.light_blue);
            addCard.setTextColor(getResources().getColor(R.color.light_blue));
            addAnother.setTextColor(getResources().getColor(R.color.light_blue));
        } else if (v.equals(purpleButton)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.light_purple));
//            }
            currentColor = getResources().getColor(R.color.light_purple);
            addCard.setTextColor(getResources().getColor(R.color.light_purple));
            addAnother.setTextColor(getResources().getColor(R.color.light_purple));
        } else if (v.equals(greenButton)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.light_green));
//            }
            currentColor = getResources().getColor(R.color.light_green);
            addCard.setTextColor(getResources().getColor(R.color.light_green));
            addAnother.setTextColor(getResources().getColor(R.color.light_green));
        } else if (v.equals(orangeButton)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.light_orange));
//            }
            currentColor = getResources().getColor(R.color.light_orange);
            addCard.setTextColor(getResources().getColor(R.color.light_orange));
            addAnother.setTextColor(getResources().getColor(R.color.light_orange));
        } else if (v.equals(redButton)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.light_red));
//            }
            currentColor = getResources().getColor(R.color.light_red);
            addCard.setTextColor(getResources().getColor(R.color.light_red));
            addAnother.setTextColor(getResources().getColor(R.color.light_red));
        } else if (v.equals(greyButton)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.light_grey));
//            }
            currentColor = getResources().getColor(R.color.light_grey);
            addCard.setTextColor(getResources().getColor(R.color.light_grey));
            addAnother.setTextColor(getResources().getColor(R.color.light_grey));
        } else if (v.equals(addAnother)) {
            View newView = mInflater.inflate(R.layout.add_category_rate_sublayout, null);
            int index = categoryLayout.getChildCount() - 1;
            categoryLayout.addView(newView, index);

        } else if(v.equals(addCard)) {
            ArrayList<CategoryRate> rates = new ArrayList<CategoryRate>();

            for (int index = 0; index < categoryLayout.getChildCount() - 1; index++) {
                AddCategoryRateViewHolder viewHolder = new AddCategoryRateViewHolder(categoryLayout.getChildAt(index));
                if (!viewHolder.categoryName.getText().toString().equals("") && !viewHolder.categoryRate.getText().toString().equals("")) {
                    CategoryRate cRate = new CategoryRate(viewHolder.categoryName.getText().toString(), Float.parseFloat(viewHolder.categoryRate.getText().toString()));
                    rates.add(cRate);
                }
            }

            Card c = new Card(cardName.getText().toString(), Float.parseFloat(cardRate.getText().toString()), rates, currentColor);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String cardsJson = preferences.getString("Cards", "");
            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Card>>() {}.getType();

            ArrayList<Card> cardList = gson.fromJson(cardsJson, listType);
            if (cardList == null) {
                cardList = new ArrayList<Card>();
            }
            cardList.add(c);

            preferences.edit().putString("Cards", gson.toJson(cardList, listType)).commit();

            BusProvider.getInstance().post(produceCardAddedEvent());

            dismissAllowingStateLoss();
        } else if(v.equals(cancel)) {
            dismissAllowingStateLoss();
        }
    }

    @Produce public CardsAddedEvent produceCardAddedEvent() {
        return new CardsAddedEvent();
    }
}
