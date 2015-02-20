package com.alexwglenn.ccrewardsbuddy.viewholders;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.alexwglenn.ccrewardsbuddy.R;
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

    public AddCategoryRateViewHolder(View v) {
        ButterKnife.inject(this, v);
    }
}
