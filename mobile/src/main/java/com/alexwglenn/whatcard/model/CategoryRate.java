package com.alexwglenn.whatcard.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by aglenn on 2/19/15.
 */
public class CategoryRate extends RealmObject {

    public String storeName;
    public String categoryName;
    public float rewardRate;
    public Date startDate;
    public Date endDate;

    public CategoryRate() {
        this.storeName = "";
        this.categoryName = "";
        this.rewardRate = 0.0f;
        this.startDate = new Date();
        this.endDate = new Date();
    }

    public CategoryRate(String storeName, String categoryName, float rewardRate, Date startDate, Date endDate) {
        this.storeName = storeName;
        this.categoryName = categoryName;
        this.rewardRate = rewardRate;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
