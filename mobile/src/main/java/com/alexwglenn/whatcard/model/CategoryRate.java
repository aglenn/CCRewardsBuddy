package com.alexwglenn.whatcard.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by aglenn on 2/19/15.
 */
public class CategoryRate {

    @PropertyName("store_name")
    String storeName;
    @PropertyName("category_name")
    String categoryName;
    @PropertyName("reward_rate")
    float rewardRate;
    @PropertyName("start_date")
    String startDate;
    @PropertyName("end_date")
    String endDate;

    public CategoryRate() {
        this.storeName = "";
        this.categoryName = "";
        this.rewardRate = 0.0f;
        this.startDate = "";
        this.endDate = "";
    }

    public CategoryRate(String storeName, String categoryName, float rewardRate, String startDate, String endDate) {
        this.storeName = storeName;
        this.categoryName = categoryName;
        this.rewardRate = rewardRate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Exclude
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Exclude
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Exclude
    public float getRewardRate() {
        return rewardRate;
    }

    public void setRewardRate(float rewardRate) {
        this.rewardRate = rewardRate;
    }

    @Exclude
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Exclude
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
