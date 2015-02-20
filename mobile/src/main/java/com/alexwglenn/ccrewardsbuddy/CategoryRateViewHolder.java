package com.alexwglenn.ccrewardsbuddy;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by aglenn on 2/19/15.
 */
public class CategoryRateViewHolder {

    @InjectView(R.id.categoryName)
    public EditText categoryName;
    @InjectView(R.id.categoryRate)
    public EditText categoryRate;

    public CategoryRateViewHolder(View v) {
        ButterKnife.inject(this, v);
    }
}
