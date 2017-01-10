package com.mark.qos.mobileqos.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telecom.TelecomManager;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.mark.qos.mobileqos.R;


public class FragmentPhoneInfo extends Fragment implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    TextView textinfo;

    TextView tvsignal;
    TextView tvasu;
    TextView tvcid;
    TextView tvpsc;
    TextView tvlac;
    TextView tvidsubscriber;
    TextView tvoperatorname;
    TextView tvoperatorcode;
    TextView tvphotetype;
    TextView tvnetworktype;

    TextView textinfosignal;
    final String LOG_TAG = "myLogs";
   // Button btngetino;
    TelephonyManager manager;
    MapView mapView;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;

    public FragmentPhoneInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPhoneInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPhoneInfo newInstance(String param1, String param2) {
        FragmentPhoneInfo fragment = new FragmentPhoneInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.d(LOG_TAG, "on Create FragmentInfo");

        TelecomManager managet = (TelecomManager) getActivity().getSystemService(Context.TELECOM_SERVICE);
        manager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
       // CellSignalStrengthLte lte = (CellSignalStrengthLte) CellSignalStrength.
        //   manager.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_phone_info, container, false);
        Log.d(LOG_TAG, "on CreateView FragmentInfo");
        textinfo = (TextView) v.findViewById(R.id.textinfo);
        textinfosignal = (TextView) v.findViewById(R.id.textinfosignal);
     //   btngetino = (Button) v.findViewById(R.id.btngetinfo);
     //   btngetino.setOnClickListener(this);

         tvsignal = (TextView) v.findViewById(R.id.textView1);
         tvasu = (TextView) v.findViewById(R.id.textView2);
         tvcid = (TextView) v.findViewById(R.id.textView3);
         tvpsc = (TextView) v.findViewById(R.id.textView4);
         tvlac = (TextView) v.findViewById(R.id.textView5);
         tvidsubscriber = (TextView) v.findViewById(R.id.textView6);
         tvoperatorname = (TextView) v.findViewById(R.id.textView7);
         tvoperatorcode = (TextView) v.findViewById(R.id.textView8);
         tvphotetype = (TextView) v.findViewById(R.id.textView9);
         tvnetworktype = (TextView) v.findViewById(R.id.textView10);


        mapView = (MapView) v.findViewById(R.id.mapView2);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapView.setFocusable(true);

        getInfo();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.btngetinfo:
                Log.d(LOG_TAG, "Button click");
                getInfo();
                break;*/
        }
    }

    private PhoneStateListener listener = new PhoneStateListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            Log.d(LOG_TAG, "nSignalStrengthsChanged");
            /*textinfosignal.setText(" ");
            Log.d(LOG_TAG, "nSignalStrengthsChanged");
            textinfosignal.append("\n Сигнал: \t" + signalStrength.getGsmSignalStrength());
            textinfosignal.append("\n Сигнал/шум: \t" + signalStrength.getEvdoSnr());
            textinfosignal.append("\n Рейтинг возможност ошибки: \t" + signalStrength.getGsmBitErrorRate());
            textinfosignal.append("\n Уровень: \t" + signalStrength.getLevel());
            */
            try {
                for (final CellInfo info : manager.getAllCellInfo()) {
                    if (info instanceof CellInfoGsm) {
                        final CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                        // do what you need
                        Log.d(LOG_TAG, "gsm");
                       /* textinfosignal.setText(" ");
                        textinfosignal.append("\n Сигнал: \t" + gsm.getDbm());
                        textinfosignal.append("\n AsuLevel \t" + gsm.getAsuLevel());
                        textinfosignal.append("\n Уровень: \t" + gsm.getLevel());
*/
                        tvsignal.setText(gsm.getDbm()+ " dBm");
                        tvasu.setText("" +gsm.getAsuLevel());
                    } else if (info instanceof CellInfoCdma) {
                        final CellSignalStrengthCdma cdma = ((CellInfoCdma) info).getCellSignalStrength();
                        // do what you need
                    } else if (info instanceof CellInfoWcdma) {
                        Log.d(LOG_TAG, "wcdma");

                        final CellSignalStrengthWcdma wcdma = ((CellInfoWcdma) info).getCellSignalStrength();
                        // do what you need

                       /* textinfosignal.setText(" ");
                        textinfosignal.append("\n Сигнал: \t" + wcdma.getDbm());
                        textinfosignal.append("\n AsuLevel \t" + wcdma.getAsuLevel());
                        textinfosignal.append("\n Уровень: \t" + wcdma.getLevel());*/

                        tvsignal.setText(wcdma.getDbm()+ " dBm");
                        tvasu.setText("" +wcdma.getAsuLevel());

                    } else if (info instanceof CellInfoLte) {
                        final CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
                        // do what you need
                        Log.d(LOG_TAG, "lte");
                      /*  textinfosignal.setText(" ");
                        textinfosignal.append("\n Сигнал: \t" + lte.getDbm());
                        textinfosignal.append("\n AsuLevel \t" + lte.getAsuLevel());
                        textinfosignal.append("\n TimingAdvance \t" + lte.getTimingAdvance());
                        textinfosignal.append("\n Уровень: \t" + lte.getLevel());*/

                        tvsignal.setText(lte.getDbm()+ " dBm");
                        tvasu.setText("" + lte.getAsuLevel());

                    } else {
                        throw new Exception("Unknown type of cell signal!");
                    }
                }

            } catch (Exception e) {
                Log.e(LOG_TAG, "Unable to obtain cell signal information", e);
            }
        }

    };



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void getInfo() {

        String operator = manager.getSimOperator();
       manager.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        CellInfo info1;

        Log.d(LOG_TAG, "class - " + manager.getAllCellInfo().get(0));
    //    final CellSignalStrengthGsm gsm1 = ((CellInfoGsm) info1).getCellSignalStrength();
       // Log.d(LOG_TAG, "lte" + gsm1.getDbm());

      //  CellLocation cell = manager.getCellLocation();

        GsmCellLocation gsmCell = (GsmCellLocation) manager.getCellLocation();

        tvcid.setText("" + gsmCell.getCid());
        tvpsc.setText("" + gsmCell.getPsc());
        tvlac.setText("" + gsmCell.getLac());

        tvidsubscriber.setText("" + manager.getSubscriberId());
        tvoperatorname.setText("" + manager.getNetworkOperatorName());
        tvoperatorcode.setText("" + manager.getNetworkOperator());
        tvphotetype.setText("" + convertPhoneTypeToString(manager.getPhoneType()));
        tvnetworktype.setText("" + convertNetworkTypeToString(manager.getNetworkType()));

        /*textinfo.setText("");
        textinfo.append("\n Cid:\t" + gsmCell.getCid());
        textinfo.append("\n Psc:\t" + gsmCell.getPsc());
        textinfo.append("\n Lac:\t" + gsmCell.getLac());

        textinfo.append("\n Состояние вызова:\t" + convertCallStateToString(manager.getCallState()));
        textinfo.append("\n  ID устройства \t" + manager.getDeviceId());
        textinfo.append("\n Версия ПО \t" + manager.getDeviceSoftwareVersion());
        textinfo.append("\n Linel номер \t" + manager.getLine1Number());
        textinfo.append("\n Тип сети \t" + convertNetworkTypeToString(manager.getNetworkType()));
        textinfo.append("\n ISO страны \t" + manager.getNetworkCountryIso());
        textinfo.append("\n Оператор сети \t" + manager.getNetworkOperator());
        textinfo.append("\n Имя оператора сети \t" + manager.getNetworkOperatorName());
        textinfo.append("\n тип телефона \t" + convertPhoneTypeToString(manager.getPhoneType()));
        textinfo.append("\n DataActivity \t" + convertDataActivityToString(manager.getDataActivity()));
        textinfo.append("\n Статут данных \t" + convertDataStateToString(manager.getDataState()));
        textinfo.append("\n ID абанента \t" + manager.getSubscriberId());
        textinfo.append("\n VoiceMailAlphaTag \t" + manager.getVoiceMailAlphaTag());
        textinfo.append("\n VoiceMailNumber \t" + manager.getVoiceMailNumber());
        textinfo.append("\n hasIccCard\t" + manager.hasIccCard());
        textinfo.append("\n Роуминиг\t" + manager.isNetworkRoaming());*/

    /*    textinfo.append("\n ----------------\t" );
        textinfo.append("\n Сим опреатор\t" + manager.getSimOperator());
        textinfo.append("\n Сим ISO страны \t" + manager.getSimCountryIso());
        textinfo.append("\n Сим имя оператора\t" + manager.getSimOperatorName());
        textinfo.append("\n Сим статус\t" + manager.getSimState());
        textinfo.append("\n \t" + 1);
        textinfo.append("\n \t" + 1);
*/

/*
        CdmaCellLocation cdmacell = (CdmaCellLocation) manager.getCellLocation();
      // cdmacell.getBaseStationId();
        textinfo.append("\n BaseStationId:\t" + cdmacell.getBaseStationId());
        textinfo.append("\n NetworkId:\t" + cdmacell.getNetworkId());
        textinfo.append("\n SystemId:\t" + cdmacell.getSystemId());
        textinfo.append("\n Latitude:\t" + cdmacell.getBaseStationLatitude());
        textinfo.append("\n Longitude:\t" + cdmacell.getBaseStationLongitude());
        */
       // singnalStrength =  new SignalStrength();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
         mMap = googleMap;
      /*  LatLng sydney = new LatLng(53.88, 27.600);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Minsk"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
*/
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Log.d(LOG_TAG, "клик на My Location");
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                return false;
            }
        });
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "прошли проверку");
            return;
        }
       // mMap.animateCamera(CameraUpdateFactory.zoomTo(8));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(LOG_TAG, "onConnected");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "не прошли проверку");
            // TODO: Consider calling
            return;
        }


        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d(LOG_TAG, "Latitude()" + String.valueOf(mLastLocation.getLatitude()));
            Log.d(LOG_TAG, "Longitude()" + String.valueOf(mLastLocation.getLongitude()));

            LatLng myLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
          //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Minsk"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(LOG_TAG, "onConnectionFailed");
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private String convertCallStateToString(int callState) {
        switch (callState) {
            case TelephonyManager.CALL_STATE_IDLE:
                return "IDLE";
            case TelephonyManager.CALL_STATE_OFFHOOK:
                return "OFFHOOK";
            case TelephonyManager.CALL_STATE_RINGING:
                return "RINGING";
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

    private String convertDataActivityToString(int dataActivity) {
        switch (dataActivity) {
            case TelephonyManager.DATA_ACTIVITY_DORMANT:
                return "DORMANT"; //спящий
            case TelephonyManager.DATA_ACTIVITY_IN:
                return "IN";
            case TelephonyManager.DATA_ACTIVITY_INOUT:
                return "INOUT";
            case TelephonyManager.DATA_ACTIVITY_NONE:
                return "NONE";
            case TelephonyManager.DATA_ACTIVITY_OUT:
                return "OUT";

            default:
                return "Не определено";
        }
    }

    private String convertDataStateToString(int dataState) {
        switch (dataState) {
            case TelephonyManager.DATA_CONNECTED:
                return "CONNECTED";
            case TelephonyManager.DATA_CONNECTING:
                return "CONNECTING";
            case TelephonyManager.DATA_DISCONNECTED:
                return "DISCONNECTED";
            case TelephonyManager.DATA_SUSPENDED:
                return "DATA_SUSPENDED";

            default:
                return "Не определено";
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

    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


}
