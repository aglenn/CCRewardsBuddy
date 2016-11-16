package com.alexwglenn.whatcard.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by aglenn on 2/19/15.
 */
public class Card extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;
    @SerializedName("bank_name")
    private String bankName;
    @SerializedName("base_reward_rate")
    private float basePercentage;
    @SerializedName("reward_rates")
    private RealmList<CategoryRate> categoryRates;
    private int color;

    public Card() {
        this.id = 0;
        this.name = "";
        this.bankName = "";
        this.basePercentage = 0.0f;
        this.categoryRates = null;
        this.color = 0;
    }

    public Card(String name, String bankName, float basePercentage, RealmList<CategoryRate> categoryRates, int color) {
        this.name = name;
        this.bankName = bankName;
        this.basePercentage = basePercentage;
        this.categoryRates = categoryRates;
        this.color = color;
    }

    public Card(int id, String name, String bankName, float basePercentage, RealmList<CategoryRate> categoryRates, int color) {
        this.id = id;
        this.name = name;
        this.bankName = bankName;
        this.basePercentage = basePercentage;
        this.categoryRates = categoryRates;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBasePercentage() {
        return basePercentage;
    }

    public void setBasePercentage(float basePercentage) {
        this.basePercentage = basePercentage;
    }

    public RealmList<CategoryRate> getCategoryRates() {
        return categoryRates;
    }

    public void setCategoryRates(RealmList<CategoryRate> categoryRates) {
        this.categoryRates = categoryRates;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
