package com.alexwglenn.ccrewardsbuddy.model;

/**
 * Created by aglenn on 2/20/15.
 */
public class RewardCategory {
    public String categoryName;
    public float bestRate;
    public Card bestCard;

    public RewardCategory(String categoryName, float bestRate, Card bestCard) {
        this.categoryName = categoryName;
        this.bestRate = bestRate;
        this.bestCard = bestCard;
    }
}
