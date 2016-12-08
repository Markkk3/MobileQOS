package com.mark.qos.mobileqos.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.mark.qos.mobileqos.MainActivity;
import com.mark.qos.mobileqos.R;
import com.mark.qos.mobileqos.data.ResultItem;

import java.util.ArrayList;


public class FragmentMap extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // private GoogleMap mMap;
    private GoogleMap mMap;
    private Double latitude, longitude;

    private MapView mapView;
    final String LOG_TAG = "myLogs";

    private OnFragmentInteractionListener mListener;
    private MainActivity mainActivity;
    ArrayList<ResultItem> resultItemArrayList = new ArrayList();
    public FragmentMap() {

        // Required empty public constructor
    }
    int currentzoom = 12;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(LOG_TAG, "onCreateView");
        mainActivity = (MainActivity) getActivity();
        View v = inflater.inflate(R.layout.activity_maps, container, false);
        //  setContentView(R.layout.activity_maps);
        resultItemArrayList =  mainActivity.getDatabaseManager().readResults();
        Log.d(LOG_TAG, "1");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        Log.d(LOG_TAG, "2");
        mapFragment.getMapAsync(this);

      //  drawResults();

        return v;

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void drawResults(int radius) {

        Log.d(LOG_TAG, "drawResults on Map = " + radius);
        for (int i = 0; i < resultItemArrayList.size(); i++) {
            Log.d(LOG_TAG, "result = " + i);
            ResultItem resultItem = resultItemArrayList.get(i);
            int red = 0;
            int green = 0;

            int downloadSpeed = resultItem.getDownload();
            if(downloadSpeed<1000) {
                red = (int) downloadSpeed/10 + 150;
                green = (int) downloadSpeed/6;
            }
            else {

                if(downloadSpeed<5000) {
                    red = 255;
                    green = (int) 155 + downloadSpeed/100;
                }
                else {
                    if(downloadSpeed>10000) {
                        red = 0;
                        green = 255;
                    }
                    else {
                        green = (int) 155 + downloadSpeed/100;
                        red = (int) (10000 - downloadSpeed) / 20;
                    }
                }
            }


            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(resultItem.getLatitude(), resultItem.getLongitude()))
                    .fillColor(Color.argb(255, red, green, 0))
                    .strokeColor(Color.argb(150, red, green, 0))
                    .radius(radius);

            Circle circle = mMap.addCircle(circleOptions);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(LOG_TAG, "onMapReady");
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(53.88, 27.600);
      //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Minsk"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



        mMap.setMyLocationEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "прошли проверку");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
/*
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if(currentzoom != (int) mMap.getCameraPosition().zoom) {
                    currentzoom = (int) mMap.getCameraPosition().zoom;
                    drawResults((int)(500-8*currentzoom)/currentzoom);
                    Log.d(LOG_TAG, "onCameraMove" + currentzoom);
                }

               // Log.d(LOG_TAG, "onCameraMove" + zoom);
            }
        });
*/
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        /*
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(resultItemArrayList.get(1).getLatitude(), resultItemArrayList.get(1).getLongitude()))
                .fillColor(Color.argb(255, 50, 240, 0))
                .radius(10);

        Circle circle = mMap.addCircle(circleOptions);
*/
        drawResults(30);

    }

    public void onClickLocationSettings(View view) {
        startActivity(new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    };


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
