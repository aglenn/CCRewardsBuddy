package com.alexwglenn.ccrewardsbuddy.viewholders;

import android.view.View;
import android.widget.TextView;

import com.alexwglenn.ccrewardsbuddy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by aglenn on 2/19/15.
 */
public class CardViewHolder {

    @InjectView(R.id.cardTitle)
    public TextView cardTitle;
    @InjectView(R.id.cardBasePercentage)
    public TextView cardBasePercentage;
    @InjectView(R.id.cardVariablePercentages)
    public TextView cardVariablePercentages;

    public CardViewHolder(View v) {
        ButterKnife.inject(this, v);
    }
}
