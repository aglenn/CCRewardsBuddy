package com.alexwglenn.whatcard;

import com.alexwglenn.whatcard.util.AuthManager;
import com.alexwglenn.whatcard.util.DatabaseManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aglenn on 1/13/16.
 */

@Module
public class WhatCardModule {

    @Provides
    @Singleton
    DatabaseManager provideDatabaseManager() {
        DatabaseManager db = new DatabaseManager(provideAuthManager());
        return db;
    }

    @Provides
    @Singleton
    AuthManager provideAuthManager() {
        AuthManager auth = new AuthManager();
        return auth;
    }

}
