package com.alexwglenn.ccrewardsbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by aglenn on 2/19/15.
 */
public class CardAdapter extends BaseAdapter {

    private ArrayList<Card> cards;
    private Context mContext;
    private LayoutInflater mInflater;

    public CardAdapter(ArrayList<Card> cards, Context context) {
        this.cards = cards;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Card getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CardViewHolder viewHolder;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.card_item, null);
            viewHolder = new CardViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (CardViewHolder) convertView.getTag();
        }

        Card c = getItem(position);

        viewHolder.cardTitle.setText(c.name);
        viewHolder.cardBasePercentage.setText(c.basePercentage + "%");
        viewHolder.cardVariablePercentages.setText(Integer.toString(c.categoryRates.size()));

        return convertView;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
