package com.alexwglenn.ccrewardsbuddy;

import java.util.ArrayList;

/**
 * Created by aglenn on 2/19/15.
 */
public class Card {

    public String name;
    public float basePercentage;
    public ArrayList<CategoryRate> categoryRates;

    public Card(String name, float basePercentage, ArrayList<CategoryRate> categoryRates) {
        this.name = name;
        this.basePercentage = basePercentage;
        this.categoryRates = categoryRates;
    }
}
