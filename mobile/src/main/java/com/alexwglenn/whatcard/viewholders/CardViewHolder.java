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
 * Created by aglenn on 2/19/15.
 */
public class CardViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.card_background)
    public CardView cardBackground;
    @InjectView(R.id.cardTitle)
    public TextView cardTitle;
    @InjectView(R.id.cardBasePercentage)
    public TextView cardBasePercentage;
    @InjectView(R.id.cardVariablePercentages)
    public TextView cardVariablePercentages;

    public CardViewHolder(View v) {
        super(v);
        ButterKnife.inject(this, v);
    }

    public void setCard(Card card) {
        cardTitle.setText(card.getBankName() + " " + card.getName());
        cardBasePercentage.setText(card.getBasePercentage() + "%");

        if (card.getCategoryRates() != null) {
            cardVariablePercentages.setText(Integer.toString(card.getCategoryRates().size()));
        } else {
            cardVariablePercentages.setText("0");
        }
        cardBackground.setCardBackgroundColor(Color.parseColor(card.getColor()));
    }
}
