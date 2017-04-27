package com.alexwglenn.whatcard.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by aglenn on 2/19/15.
 */
public class Card {

    String id;
    @PropertyName("card_name")
    String name;
    @PropertyName("bank_name")
    String bankName;
    @PropertyName("base_reward_rate")
    float basePercentage;
    @PropertyName("reward_rates")
    ArrayList<CategoryRate> categoryRates;
    String color;

    public Card() {
        this.id = "";
        this.name = "";
        this.bankName = "";
        this.basePercentage = 0.0f;
        this.categoryRates = null;
        this.color = "#000000";
    }

    public Card(String name, String bankName, float basePercentage, ArrayList<CategoryRate> categoryRates, String color) {
        this.name = name;
        this.bankName = bankName;
        this.basePercentage = basePercentage;
        this.categoryRates = categoryRates;
        this.color = color;
    }

    public Card(String id, String name, String bankName, float basePercentage, ArrayList<CategoryRate> categoryRates, String color) {
        this.id = id;
        this.name = name;
        this.bankName = bankName;
        this.basePercentage = basePercentage;
        this.categoryRates = categoryRates;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Exclude
    public float getBasePercentage() {
        return basePercentage;
    }

    public void setBasePercentage(float basePercentage) {
        this.basePercentage = basePercentage;
    }

    @Exclude
    public ArrayList<CategoryRate> getCategoryRates() {
        return categoryRates;
    }

    public void setCategoryRates(ArrayList<CategoryRate> categoryRates) {
        this.categoryRates = categoryRates;
    }

    @Exclude
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
