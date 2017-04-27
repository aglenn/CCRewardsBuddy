package com.alexwglenn.whatcard.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by aglenn on 4/26/17.
 */

public class AuthManager {
    private FirebaseAuth mAuth;
    private String mCurrentUserID;
    private Boolean mAnonymous;

    public AuthManager() {
        this.mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            mAuth.signInAnonymously();
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mCurrentUserID = user.getUid();
            mAnonymous = user.isAnonymous();
        }

        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mCurrentUserID = user.getUid();
                    mAnonymous = user.isAnonymous();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        mAuth.addAuthStateListener(mAuthListener);
    }

    public String getCurrentUserID() {
        return mCurrentUserID;
    }

    public Boolean isUserAnonymous() {
        return mAnonymous;
    }
}
