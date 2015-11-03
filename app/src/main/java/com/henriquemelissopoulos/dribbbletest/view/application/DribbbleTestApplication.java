package com.henriquemelissopoulos.dribbbletest.view.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.henriquemelissopoulos.dribbbletest.BuildConfig;
import com.henriquemelissopoulos.dribbbletest.controller.Config;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by h on 31/10/15.
 */

public class DribbbleTestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initCrashlytics();
        initRealm();
    }


    private void initCrashlytics() {
        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(Config.CRASH_REPORT).build()).build());
        Crashlytics.getInstance().core.setBool("debug", BuildConfig.DEBUG);
    }


    private void initRealm() {
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder(this);
        builder.name("dribbbletest.realm");
        if(BuildConfig.DEBUG) builder.deleteRealmIfMigrationNeeded();

        Realm.setDefaultConfiguration(builder.build());
    }

}
