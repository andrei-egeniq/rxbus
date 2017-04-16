package com.egeniq.rxbus;

import android.app.Application;

/**
 * Created by andrei on 15/04/2017.
 */

public class BusApplication extends Application {

    private DataService mDataService;

    @Override
    public void onCreate() {
        super.onCreate();
        mDataService = new DataService();
    }

    public DataService getDataService() {
        return mDataService;
    }
}
