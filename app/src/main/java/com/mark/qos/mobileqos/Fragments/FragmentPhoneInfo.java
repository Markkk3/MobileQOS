package com.mark.qos.mobileqos.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.telecom.TelecomManager;
import android.telephony.CellLocation;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mark.qos.mobileqos.R;


public class FragmentPhoneInfo extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    TextView textinfo;
    final String LOG_TAG = "myLogs";
    Button btngetino;
    private SignalStrength singnalStrength;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_phone_info, container, false);
        Log.d(LOG_TAG, "on CreateView FragmentInfo");
        textinfo = (TextView) v.findViewById(R.id.textinfo);
        btngetino = (Button) v.findViewById(R.id.btngetinfo);
        btngetino.setOnClickListener(this);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btngetinfo:
                Log.d(LOG_TAG, "Button click");
                getInfo();
                break;
        }
    }

    public void getInfo() {
        TelephonyManager manager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String operator = manager.getSimOperator();
        textinfo.setText("" + operator);
      //  CellLocation cell = manager.getCellLocation();

        GsmCellLocation gsmCell = (GsmCellLocation) manager.getCellLocation();
        textinfo.append("\n Cid:\t" + gsmCell.getCid());
        textinfo.append("\n Psc:\t" + gsmCell.getPsc());
        textinfo.append("\n Lac:\t" + gsmCell.getLac());

        textinfo.append("\n Состояние вызова:\t" + convertCallStateToString(manager.getCallState()));
        textinfo.append("\n  ID устройства \t" + manager.getDeviceId());
        textinfo.append("\n Версия ПО \t" + manager.getDeviceSoftwareVersion());
        textinfo.append("\n Linel номер \t" + manager.getLine1Number());
        textinfo.append("\n Тип сети \t" + manager.getNetworkType());
        textinfo.append("\n ISO страны \t" + manager.getNetworkCountryIso());
        textinfo.append("\n Оператор сети \t" + manager.getNetworkOperator());
        textinfo.append("\n Имя оператора сети\t" + manager.getNetworkOperatorName());
        textinfo.append("\n тип телефона\t" + manager.getPhoneType());
        textinfo.append("\n \t" + manager.getDataActivity());
        textinfo.append("\n \t" + 1);
        textinfo.append("\n \t" + 1);
        textinfo.append("\n \t" + 1);
        textinfo.append("\n \t" + 1);


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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
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
}
