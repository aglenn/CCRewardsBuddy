package com.alexwglenn.ccrewardsbuddy;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

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


        }
    }
}