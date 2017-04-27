package com.alexwglenn.whatcard.viewholders;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alexwglenn.whatcard.R;
import com.alexwglenn.whatcard.model.Card;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by aglenn on 2/20/15.
 */
public class CategoryRewardViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.card_background)
    public CardView cardBackground;
    @InjectView(R.id.categoryName)
    public TextView categoryName;
    @InjectView(R.id.bestCardName)
    public TextView bestCardName;
    @InjectView(R.id.bestCardRate)
    public TextView bestCardRate;

    public CategoryRewardViewHolder(View v) {
        super(v);
        ButterKnife.inject(this, v);
    }

    public void setUp(Card card, String category, float bestRate) {
        categoryName.setText(category);
        bestCardName.setText(card.getBankName() + " " + card.getName());
        bestCardRate.setText(bestRate + "%");
        cardBackground.setCardBackgroundColor(Color.parseColor(card.getColor()));
    }
}
