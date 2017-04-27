package com.alexwglenn.whatcard.util;

import android.content.Context;
import android.util.Log;

import com.alexwglenn.whatcard.CategoryUpdateListener;
import com.alexwglenn.whatcard.WhatCard;
import com.alexwglenn.whatcard.model.Card;
import com.firebase.ui.database.ChangeEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by aglenn on 4/26/17.
 */

public class DatabaseManager {

    private static String TAG = "DatabaseManager";

    AuthManager mAuthManager;
    private FirebaseDatabase mDatabase;
    private ArrayList<CategoryUpdateListener> mListeners = new ArrayList<>();
    private ValueEventListener mUserCardVel;
    private ValueEventListener mCategoriesVel;

    public DatabaseManager(AuthManager authManager) {
        mAuthManager = authManager;
        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.setPersistenceEnabled(true);
    }

    public void saveCard(Card card) {
        DatabaseReference cardRef = mDatabase.getReference("cards/" + card.getId());

        if (card.getId() == null) {
            cardRef = mDatabase.getReference("cards").push();
            card.setId(cardRef.getKey());
        }

        cardRef.setValue(card);

        DatabaseReference userCardsRef = mDatabase.getReference("users/" + mAuthManager.getCurrentUserID() + "/cards/" + card.getId());
        userCardsRef.setValue(true);
    }

    public void saveUserCategory(String category) {
        DatabaseReference newCategoryRef = mDatabase.getReference("users/" + mAuthManager.getCurrentUserID() + "/categories").push();
        newCategoryRef.setValue(category);
    }

    public void deleteUserCard(Card card) {
        DatabaseReference cardRef = mDatabase.getReference("users/" + mAuthManager.getCurrentUserID() + "/cards/" + card.getId());
        cardRef.removeValue();
    }

    public DatabaseReference getUserCardsRef() {
        DatabaseReference userCardsRef = mDatabase.getReference("users/" + mAuthManager.getCurrentUserID() + "/cards");
        Log.d(TAG, "userRef: " + userCardsRef.toString());
        return userCardsRef;
    }

    public DatabaseReference getAllCardsRef() {
        DatabaseReference cardsRef = mDatabase.getReference("cards");
        Log.d(TAG, "card ref: " + cardsRef.toString());
        return cardsRef;
    }

    public void addCategoryUpdateListener(final CategoryUpdateListener listener) {
        mListeners.add(listener);

        if (mUserCardVel != null) {
            return;
        }

        DatabaseReference cardsRef = mDatabase.getReference("cards");
        DatabaseReference userCardsRef = mDatabase.getReference("users/" + mAuthManager.getCurrentUserID() + "/cards");
        final FirebaseIndexArray userCards = new FirebaseIndexArray(userCardsRef, cardsRef);
        userCards.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {
                ArrayList<Card> cards = new ArrayList<Card>();

                for (int arrayIndex = 0; arrayIndex < userCards.getCount(); arrayIndex++) {
                    cards.add(userCards.getItem(arrayIndex).getValue(Card.class));
                }

                for (CategoryUpdateListener listener : mListeners) {
                    listener.userCardsUpdated(cards);
                }
            }

            @Override
            public void onDataChanged() {
                ArrayList<Card> cards = new ArrayList<Card>();

                for (int arrayIndex = 0; arrayIndex < userCards.getCount(); arrayIndex++) {
                    cards.add(userCards.getItem(arrayIndex).getValue(Card.class));
                }

                for (CategoryUpdateListener listener : mListeners) {
                    listener.userCardsUpdated(cards);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        DatabaseReference userCategoriesRef = mDatabase.getReference("users/" + mAuthManager.getCurrentUserID() + "/categories");
        mCategoriesVel = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> categories = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    categories.add(snapshot.getValue(String.class));
                }

                for (CategoryUpdateListener listener : mListeners) {
                    listener.categoriesUpdated(categories);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        userCategoriesRef.addValueEventListener(mCategoriesVel);
    }

    public void removeCategoryUpdateListener(CategoryUpdateListener listener) {
        mListeners.remove(listener);
    }

}
