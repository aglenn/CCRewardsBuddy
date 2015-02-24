package com.alexwglenn.whatcard.model;

/**
 * Created by aglenn on 2/19/15.
 */
public class CategoryRate {

    public String categoryName;
    public float categoryPercentage;

    public CategoryRate(String categoryName, float categoryPercentage) {
        this.categoryName = categoryName;
        this.categoryPercentage = categoryPercentage;
    }
}
