package com.alexwglenn.whatcard.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aglenn on 1/13/16.
 */
public class AddCardResponse {

    @SerializedName("card_id")
    public int cardID;

    @SerializedName("message")
    public String errorMessage;
}
