package com.mark.qos.mobileqos.test;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.mark.qos.mobileqos.data.ResultItem;

/**
 * Created by tushkevich_m on 22.11.2016.
 */

public class PhoneInfo  {

    private final TelephonyManager manager;
    ResultItem resultItem;
    final String LOG_TAG = "myLogss";

    public PhoneInfo(Context context, ResultItem resultItem) {
        this.resultItem = resultItem;
        manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void getInfo() {

        String operator = manager.getSimOperator();
      //  manager.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        CellInfo info1;

        Log.d(LOG_TAG, "class - " + manager.getAllCellInfo().get(0));
        //    final CellSignalStrengthGsm gsm1 = ((CellInfoGsm) info1).getCellSignalStrength();
        // Log.d(LOG_TAG, "lte" + gsm1.getDbm());

        //  CellLocation cell = manager.getCellLocation();

        GsmCellLocation gsmCell = (GsmCellLocation) manager.getCellLocation();
        int id;
        long id_subscriber;
        String Type_connection;
        long datetime;
        int cid;
        int psd;
        int lac;
        int mcc;
        int mnc;
        int signal_level;
        int asu_level;
        float latitude;
        float longitude;
        int speed;
        int ping;
        int packet_loss;
        int download;
        int upload;

        resultItem.setId_subscriber(manager.getSubscriberId());
        resultItem.setTypeConnection(convertNetworkTypeToString(manager.getNetworkType()));
        resultItem.setDatetime(System.currentTimeMillis());
        resultItem.setCid(gsmCell.getCid());
        resultItem.setPsd(gsmCell.getPsc());
        resultItem.setLac(gsmCell.getLac());
       // testResult.setMnc();
       // testResult.setMcc();

        try {
            for (final CellInfo info : manager.getAllCellInfo()) {
                if (info instanceof CellInfoGsm) {
                    final CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                    // do what you need
                    Log.d(LOG_TAG, "gsm");

                    resultItem.setSignallevel(gsm.getDbm());
                    resultItem.setAsulevel(gsm.getAsuLevel());
                } else if (info instanceof CellInfoCdma) {
                    final CellSignalStrengthCdma cdma = ((CellInfoCdma) info).getCellSignalStrength();
                    // do what you need
                } else if (info instanceof CellInfoWcdma) {
                    Log.d(LOG_TAG, "wcdma");

                    final CellSignalStrengthWcdma wcdma = ((CellInfoWcdma) info).getCellSignalStrength();
                    // do what you need
                    resultItem.setSignallevel(wcdma.getDbm());
                    resultItem.setAsulevel(wcdma.getAsuLevel());

                } else if (info instanceof CellInfoLte) {
                    final CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
                    // do what you need
                    Log.d(LOG_TAG, "lte");
                    resultItem.setSignallevel(lte.getDbm());
                    resultItem.setAsulevel(lte.getAsuLevel());


                } else {
                    throw new Exception("Unknown type of cell signal!");
                }
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "Unable to obtain cell signal information", e);
        }


    }

    private String convertPhoneTypeToString(int phoneType) {
        switch (phoneType) {
            case TelephonyManager.PHONE_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.PHONE_TYPE_GSM:
                return "GSM";
            case TelephonyManager.PHONE_TYPE_NONE:
                return "NONE";
            case TelephonyManager.PHONE_TYPE_SIP:
                return "SIP";

            default:
                return "Не определено";
        }
    }

    private String convertNetworkTypeToString(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "1xRTT";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "EHRPD";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "EVDO_0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "EVDO_A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "EVDO_B";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPAP";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "IDEN";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "UNKNOWN";

            default:
                return "Не определено";
        }
    }

}
