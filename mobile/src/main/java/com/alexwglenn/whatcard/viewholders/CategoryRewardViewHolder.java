package com.alexwglenn.whatcard.viewholders;

import android.view.View;
import android.widget.TextView;

import com.alexwglenn.whatcard.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by aglenn on 2/20/15.
 */
public class CategoryRewardViewHolder {

    @InjectView(R.id.categoryName)
    public TextView categoryName;
    @InjectView(R.id.bestCardName)
    public TextView bestCardName;
    @InjectView(R.id.bestCardRate)
    public TextView bestCardRate;

    public CategoryRewardViewHolder(View v) {
        ButterKnife.inject(this, v);
    }
}
