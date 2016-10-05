package com.alexwglenn.whatcard.viewholders;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.alexwglenn.whatcard.R;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by aglenn on 2/19/15.
 */
public class AddCategoryRateViewHolder {

    @InjectView(R.id.categoryName)
    public EditText categoryName;
    @InjectView(R.id.categoryRate)
    public EditText categoryRate;
    @InjectView(R.id.startDate)
    public EditText startDate;
    @InjectView(R.id.endDate)
    public EditText endDate;
    @InjectView(R.id.isStore)
    public AppCompatCheckBox isStore;

    public AddCategoryRateViewHolder(View v) {
        ButterKnife.inject(this, v);
    }
}
