package com.alexwglenn.whatcard;

import android.app.Application;

import com.alexwglenn.whatcard.util.DatabaseManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by aglenn on 1/20/16.
 */
public class WhatCard extends Application {

    private WhatCardComponent mComponent;

    @Singleton
    @Component(modules={WhatCardModule.class})
    public interface WhatCardComponent {
        void inject(DatabaseManager databaseManager);
        void inject(AddCardFragment fragment);
        void inject(AddCategoryFragment fragment);
        void inject(CardFragment fragment);
        void inject(RewardCategoryFragment fragment);
        void inject(MainActivity activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
            mComponent = DaggerWhatCard_WhatCardComponent.builder().whatCardModule(new WhatCardModule()).build();
    }

    public WhatCardComponent getComponent() {
        return mComponent;
    }
}
