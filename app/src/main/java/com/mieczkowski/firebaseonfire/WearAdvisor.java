package com.mieczkowski.firebaseonfire;

import android.app.Application;

public class WearAdvisor extends Application {

    private static WearAdvisor wearAdvisor;

    public static WearAdvisor instance() {
        return wearAdvisor;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wearAdvisor = this;
    }
}
