package com.example.hw1.Database;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MSPv3.getInstance(this);
    }
}