package com.alexwglenn.whatcard.model;

import io.realm.RealmObject;

/**
 * Created by aglenn on 2/19/15.
 */
public class CategoryRate extends RealmObject {

    public String categoryName;
    public float categoryPercentage;

    public CategoryRate() {
        this.categoryName = "";
        this.categoryPercentage = 0.0f;
    }

    public CategoryRate(String categoryName, float categoryPercentage) {
        this.categoryName = categoryName;
        this.categoryPercentage = categoryPercentage;
    }
}
