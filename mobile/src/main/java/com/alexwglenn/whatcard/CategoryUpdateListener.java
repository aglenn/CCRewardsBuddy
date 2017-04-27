package com.alexwglenn.whatcard;

import com.alexwglenn.whatcard.model.Card;

import java.util.ArrayList;

/**
 * Created by aglenn on 4/26/17.
 */

public interface CategoryUpdateListener {
    void categoriesUpdated(ArrayList<String> categories);
    void userCardsUpdated(ArrayList<Card> cards);
}
