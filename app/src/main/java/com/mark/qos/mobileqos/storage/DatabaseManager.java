package com.mark.qos.mobileqos.storage;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mark.qos.mobileqos.MyApplication;
import com.mark.qos.mobileqos.object.ResultItem;

import java.util.ArrayList;

/**
 * Created by tushkevich_m on 22.11.2016.
 */

public class DatabaseManager {

    final String LOG_TAG = "myLogs";
    DatabaseHelper databaseHelper;


    public DatabaseManager(Application application) {

        MyApplication myApplication = (MyApplication) application;
        databaseHelper = myApplication.getDatabaseHelper();
    }

    public void writeNewResult(ResultItem resultitem) {
        Log.d(LOG_TAG, "writeNewResult");
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Задайте значения для каждого столбца
        values.put(DatabaseHelper.ID_SUBSCRIBER, resultitem.getId_subscriber());
        values.put(DatabaseHelper.TYPE_CONNECTION, resultitem.getTypeConnection());
        values.put(DatabaseHelper.CID, resultitem.getCid());
        values.put(DatabaseHelper.PSC, resultitem.getPsd());
        values.put(DatabaseHelper.LAC, resultitem.getLac());
        values.put(DatabaseHelper.MCC, resultitem.getMcc());
        values.put(DatabaseHelper.MNC, resultitem.getMnc());
        values.put(DatabaseHelper.SIGNAL_LEVEL, resultitem.getSignallevel());
        values.put(DatabaseHelper.ASU_LEVEL, resultitem.getAsulevel());
        values.put(DatabaseHelper.SPEED, resultitem.getSpeed());
        values.put(DatabaseHelper.LATITUDE, resultitem.getLatitude());
        values.put(DatabaseHelper.LONGITUDE, resultitem.getLongitude());
        values.put(DatabaseHelper.PING, resultitem.getPing());
        values.put(DatabaseHelper.PACKET_LOSS, resultitem.getPacketlost());
        values.put(DatabaseHelper.DOWNLOAD, resultitem.getDownload());
        values.put(DatabaseHelper.UPLOAD, resultitem.getUpload());
        values.put(DatabaseHelper.DATETIME, resultitem.getDatetime());

        // Вставляем данные в таблицу
        db.insert(DatabaseHelper.DATABASE_TABLE_RESULTS, null, values);
        //   MyApplication.alarmItem.add(new GifItem(name,  (float) latitude, (float) longitude, id, false));
        //   adapter.notifyDataSetChanged();
        db.close();

    }

    public ArrayList<ResultItem> readResults() {
        ArrayList<ResultItem> resultItemArrayList = new ArrayList();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();


        Cursor cursor = db.query(DatabaseHelper.DATABASE_TABLE_RESULTS, new String[]{DatabaseHelper.ID,
                        DatabaseHelper.ID_SUBSCRIBER, DatabaseHelper.TYPE_CONNECTION,
                        DatabaseHelper.CID, DatabaseHelper.PSC,
                        DatabaseHelper.LAC, DatabaseHelper.MCC,
                        DatabaseHelper.MNC, DatabaseHelper.SIGNAL_LEVEL,
                        DatabaseHelper.ASU_LEVEL, DatabaseHelper.SPEED,
                        DatabaseHelper.LATITUDE, DatabaseHelper.LONGITUDE,
                        DatabaseHelper.PING, DatabaseHelper.PACKET_LOSS,
                        DatabaseHelper.DOWNLOAD, DatabaseHelper.UPLOAD,
                        DatabaseHelper.DATETIME},
                null, null,
                null, null, null);

        while (cursor.moveToNext()) {
            ResultItem resultItem = new ResultItem(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID)));
            resultItem.setId_subscriber(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ID_SUBSCRIBER)));
            resultItem.setTypeConnection(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TYPE_CONNECTION)));
            resultItem.setCid(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CID)));
            resultItem.setPsd(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PSC)));
            resultItem.setLac(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.LAC)));
            resultItem.setMcc(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MCC)));
            resultItem.setMnc(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MNC)));
            resultItem.setSignallevel(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SIGNAL_LEVEL)));
            resultItem.setAsulevel(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ASU_LEVEL)));
            resultItem.setSpeed(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SPEED)));
            resultItem.setLatitude(cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.LATITUDE)));
            resultItem.setLongitude(cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.LONGITUDE)));
            resultItem.setPing(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PING)));
            resultItem.setPacketlost(cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.PACKET_LOSS)));
            resultItem.setDownload(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.DOWNLOAD)));
            resultItem.setUpload(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.UPLOAD)));
            resultItem.setDatetime(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.DATETIME)));
            resultItemArrayList.add(resultItem);
        }
        cursor.close();
        db.close();

        return resultItemArrayList;
    }



   /* private static final String DATABASE_CREATE_SCRIPT_RESULTS = "create table "
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
    private static DatabaseHelper databaseHelper;*/


}
