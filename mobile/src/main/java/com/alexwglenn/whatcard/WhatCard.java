package com.alexwglenn.whatcard;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by aglenn on 1/20/16.
 */
public class WhatCard extends Application {

    private WhatCardComponent mComponent;

    @Singleton
    @Component(modules = WhatCardModule.class)
    public interface WhatCardComponent {
        void inject(AddCardFragment addCardFragment);
        void inject(CardFragment cardFragment);
        void inject(LoginActivity loginActivity);
    }

    @Override
    public void onCreate() {
        super.onCreate();


        mComponent = DaggerWhatCard_WhatCardComponent.builder().whatCardModule(new WhatCardModule()).build();

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .name("whatcard.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
//        mComponent.inject(this);
    }

    public WhatCardComponent getComponent() {
        return mComponent;
    }
}
