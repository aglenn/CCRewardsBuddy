package com.alexwglenn.whatcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alexwglenn.whatcard.model.Card;
import com.alexwglenn.whatcard.viewholders.CardViewHolder;

import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by aglenn on 2/19/15.
 */
public class CardAdapter extends RealmBaseAdapter<Card> {

    private Context mContext;
    private LayoutInflater mInflater;

    public CardAdapter(OrderedRealmCollection<Card> cardCollection, Context context) {
        super(context, cardCollection);
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
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

        Card c = adapterData.get(position);

        viewHolder.cardTitle.setText(c.getName());
        viewHolder.cardBasePercentage.setText(c.getBasePercentage() + "%");
        viewHolder.cardVariablePercentages.setText(Integer.toString(c.getCategoryRates().size()));

        return convertView;
    }
}
