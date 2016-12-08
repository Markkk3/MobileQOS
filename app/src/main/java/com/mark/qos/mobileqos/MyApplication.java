package com.mark.qos.mobileqos;

import android.app.Application;

import com.mark.qos.mobileqos.storage.DatabaseHelper;

public class MyApplication extends Application {

    private DatabaseHelper dbHelper;

    public final void onCreate() {
        super.onCreate();

        dbHelper = DatabaseHelper.getInstance(this);
    //    dbHelper.openDB();


    }

    public DatabaseHelper getDatabaseHelper() {
        return dbHelper;
    }
}
