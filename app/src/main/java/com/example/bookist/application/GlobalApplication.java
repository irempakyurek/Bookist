package com.example.bookist.application;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class GlobalApplication extends Application {

    private static GlobalApplication singleton;

    public static GlobalApplication getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("bookist.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm realm = Realm.getInstance(config);
        Realm.setDefaultConfiguration(config);
    }

}
