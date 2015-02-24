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

import com.alexwglenn.whatcard.model.CategoryAddedEvent;
import com.alexwglenn.whatcard.model.RewardCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Produce;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by aglenn on 2/20/15.
 */
public class AddCategoryFragment extends DialogFragment implements View.OnClickListener {

    @InjectView(R.id.categoryName)
    public EditText categoryName;
    @InjectView(R.id.cancel)
    public Button cancel;

    @InjectView(R.id.add_category)
    public Button addCategory;

    private LayoutInflater mInflater;
    private int currentColor;

    public static AddCategoryFragment newInstance() {
        AddCategoryFragment fragment = new AddCategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AddCategoryFragment() {
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

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_category, container, false);

        ButterKnife.inject(this, v);

        cancel.setOnClickListener(this);
        addCategory.setOnClickListener(this);

        mInflater = LayoutInflater.from(getActivity());

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.equals(cancel)) {
            dismissAllowingStateLoss();
        }
        else if (v.equals(addCategory)) {
            RewardCategory rCat = new RewardCategory(categoryName.getText().toString(), 0.0f, null);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String categoriesJson = preferences.getString("RewardCategories", "");
            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<RewardCategory>>() {}.getType();

            ArrayList<RewardCategory> categoryList = gson.fromJson(categoriesJson, listType);
            if (categoryList == null) {
                categoryList = new ArrayList<>();
            }
            categoryList.add(rCat);

            preferences.edit().putString("RewardCategories", gson.toJson(categoryList, listType)).commit();

            BusProvider.getInstance().post(produceCategoryAddedEvent());

            dismissAllowingStateLoss();

        }
    }

    @Produce
    public CategoryAddedEvent produceCategoryAddedEvent() {
        return new CategoryAddedEvent();
    }
}