package com.alexwglenn.whatcard.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by aglenn on 7/28/16.
 */

public class AuthorizeResponse extends RealmObject {
    @SerializedName("Session-Key")
    public String sessionKey;
    @SerializedName("user_id")
    public String userID;
}
