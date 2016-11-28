package com.mark.qos.mobileqos;

import android.app.Application;

import com.mark.qos.mobileqos.storage.DatabaseHelper;

/**
 * Created by tushkevich_m on 22.11.2016.
 */

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
