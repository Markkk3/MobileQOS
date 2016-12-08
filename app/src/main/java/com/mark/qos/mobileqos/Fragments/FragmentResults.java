package com.mark.qos.mobileqos.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mark.qos.mobileqos.MainActivity;
import com.mark.qos.mobileqos.R;
import com.mark.qos.mobileqos.data.ResultItem;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class FragmentResults extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final String LOG_TAG = "myLogs";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    MainActivity mainActivity;
    ArrayList<ResultItem> resultItemArrayList = new ArrayList();
    TextView tvresults;

    public FragmentResults() {
    }


    public static FragmentResults newInstance(String param1, String param2) {
        FragmentResults fragment = new FragmentResults();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_results, container, false);
        mainActivity = (MainActivity) getActivity();
       resultItemArrayList =  mainActivity.getDatabaseManager().readResults();

        tvresults = (TextView) v.findViewById(R.id.tvresults);

        Log.d(LOG_TAG, "resultItemArrayList.size = " + resultItemArrayList.size());
        tvresults.append("\n");
        demo();

        return v;
    }

    public void demo() {

        for (int i = resultItemArrayList.size()-1; i>0; i--) {
            ResultItem resultItem = resultItemArrayList.get(i);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(resultItem.getDatetime());
           // int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            int mHour = calendar.get(Calendar.HOUR_OF_DAY);
            int mMinute = calendar.get(Calendar.MINUTE);

           // tvresults.append(mDay + "/" + mMonth + " " + mHour + ":" + mMinute + "\n");
            tvresults.append(DateFormat.getDateInstance(DateFormat.SHORT).format(resultItem.getDatetime()) + " "
                    +  mHour + ":" + mMinute  + "\n");
            tvresults.append("Down = " + resultItem.getDownload() + " ping = "  + resultItem.getPing() + " p/l = " + resultItem.getPacketlost() + "\n");
            tvresults.append("lat = " + resultItem.getLatitude() + " long = "  + resultItem.getLongitude() + " speed = " + resultItem.getSpeed() + "\n");
            tvresults.append("ID sub = " + resultItem.getId_subscriber() + " mcc = "  + resultItem.getMcc() + " mnc = " + resultItem.getMnc() + "\n");
            tvresults.append("lac = " + resultItem.getLac() + " psd = "  + resultItem.getPsd() + " cid = " + resultItem.getCid() + "\n");
            tvresults.append(resultItem.getTypeConnection() + " LevelSignal = " + resultItem.getSignallevel()+ " asu = "  + resultItem.getAsulevel() + "\n");
            tvresults.append("\n");
            tvresults.append("\n");


        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
}
