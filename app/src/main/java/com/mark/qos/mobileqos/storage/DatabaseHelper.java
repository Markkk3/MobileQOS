package com.mark.qos.mobileqos.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tushkevich_m on 15.08.2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // названия столбцов
    public static final String ID = "id";
    public static final String ID_SUBSCRIBER = "id_subscriber";
    public static final String TYPE_CONNECTION = "type_connection";
    public static final String DATETIME = "datetime";
    public static final String CID = "cid";
    public static final String PSD = "psd";
    public static final String LAC = "lac";
    public static final String MCC = "mcc";
    public static final String MNC = "mnc";
    public static final String SIGNAL_LEVEL = "signal_level";
    public static final String ASU_LEVEL = "asu_level";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String SPEED = "speed";
    public static final String PING = "ping";
    public static final String PACKET_LOSS = "packet_loss";
    public static final String DOWNLOAD = "download";
    public static final String UPLOAD = "upload";
    // имя базы данных
    private static final String DATABASE_NAME = "databasemeasurement.db";
    // версия базы данных
    private static final int DATABASE_VERSION = 1;
    // имя таблицы
    public static final String DATABASE_TABLE_RESULTS = "results";

    private static final String DATABASE_CREATE_SCRIPT_RESULTS = "create table "
            + DATABASE_TABLE_RESULTS + " (" + ID
            + " integer primary key autoincrement, " + ID_SUBSCRIBER  + " real, "
            + TYPE_CONNECTION + " text," + CID + " integer, "
            + PSD + " integer," + LAC + " integer, "
            + MCC + " integer," + MNC + " integer, "
            + SIGNAL_LEVEL + " integer," + ASU_LEVEL + " integer, "
            + SPEED   + " integer, " + LATITUDE + " real,"
            + LONGITUDE  + " real, " + PING + " integer,"
            + PACKET_LOSS  + " integer, " + DOWNLOAD + " integer,"
            + UPLOAD + " integer, " + DATETIME + " DATETIME);";
    private static DatabaseHelper databaseHelper;

    // flag INTEGER DEFAULT 0,
    //+ BaseColumns._ID
    //  + " integer primary key autoincrement, "
    //" TEXT NOT NULL, "
    //" DATETIME"

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_SCRIPT_RESULTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static DatabaseHelper getInstance(Context context) {
        if(databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }
}
