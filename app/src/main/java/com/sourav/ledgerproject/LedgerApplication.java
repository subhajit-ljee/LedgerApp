package com.sourav.ledgerproject;

import android.app.Application;
import android.content.Context;

import profile.applicationcomponent.AppComponent;
import profile.applicationcomponent.DaggerAppComponent;


public class LedgerApplication extends Application{

    private AppComponent appComponent;
    private static Context context;
    @Override
    public void onCreate(){
        super.onCreate();
        appComponent = DaggerAppComponent.create();
        context = getApplicationContext();
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}
