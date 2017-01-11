package com.mark.qos.mobileqos.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.mark.qos.mobileqos.MainActivity;
import com.mark.qos.mobileqos.R;
import com.mark.qos.mobileqos.object.ResultItem;
import com.mark.qos.mobileqos.object.Tower;

import java.util.ArrayList;
import java.util.List;


public class FragmentMap extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final String LOG_TAG = "myLogs";
    Circle circle;

    ArrayList<ResultItem> resultItemArrayList = new ArrayList();
    int radius = 30;
    private final Runnable prepareTest = new Runnable() {

        @Override
        public void run() {
            try {

                Log.d(LOG_TAG, "prepareTest start");

                Message msg1 = Message.obtain(mHandlerPrepare, 1);
                mHandlerPrepare.sendMessage(msg1);

                for (int i = 0; i < resultItemArrayList.size(); i++) {
                    //  Log.d(LOG_TAG, "result = " + i);
                    ResultItem resultItem = resultItemArrayList.get(i);
                    int red = 0;
                    int green = 0;

                    int downloadSpeed = resultItem.getDownload();
                    if (downloadSpeed < 1000) { // от красного 127,0,0 до оранжевого  255,127,0
                        red = (int) 127 + 127*downloadSpeed/2000;
                        green = (int) 127*downloadSpeed / 2000;
                    } else {

                        if (downloadSpeed < 3000) { // от  оранжевого 255,127,0 до желтого 255,255,0
                            red = 255;
                            green = (int) 127 + 127*(downloadSpeed-1000) / 2000;
                        } else {
                            if (downloadSpeed < 5000) { // от желтого 255,255,0  до зеленого 0,255,0
                                red = (int) 255 - 255*(downloadSpeed-3000)/2000;
                                green = 255;
                            } else {
                                if (downloadSpeed < 15000) { // от зелеоного 0,255,0 до темно зеленого 0,127,0
                                    red = 0;
                                    green = (int) 255 - 127*(downloadSpeed-5000)/10000;
                                }
                                else {
                                    green = 127;
                                    red = 0;
                                }
                            }
                        }
                    }


                    CircleOptions circleOptions = new CircleOptions()
                            .center(new LatLng(resultItem.getLatitude(), resultItem.getLongitude()))
                            .fillColor(Color.argb(255, red, green, 0))
                            .strokeColor(Color.argb(150, red, green, 0))
                            .radius(radius);


                    Message msg2 = Message.obtain(mHandlerPrepare, 2);
                    msg2.obj = (CircleOptions) circleOptions;
                    mHandlerPrepare.sendMessage(msg2);
                }


            } catch (Exception e) {
                Log.d(LOG_TAG, "Exception " + e.toString());
            }

        }
    };
    int currentzoom = 12;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // private GoogleMap mMap;
    private GoogleMap mMap;
    private final Handler mHandlerPrepare = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {

                case 1:
                    mMap.clear();
                    //addHeatMap();

                    break;
                case 2:
                    mMap.addCircle((CircleOptions) msg.obj);
                    break;
            }
            //  Log.d(LOG_TAG, "handleMessage = "  + msg.arg1);
        }
    };
    private Double latitude, longitude;
    private MapView mapView;
    private OnFragmentInteractionListener mListener;
    private MainActivity mainActivity;

    public FragmentMap() {

        // Required empty public constructor
    }

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
        resultItemArrayList = mainActivity.getDatabaseManager().readResults();
        Log.d(LOG_TAG, "1");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        Log.d(LOG_TAG, "2");
        mapFragment.getMapAsync(this);

        //  drawResults();

        // drawTower();


        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void drawResults() {
        Log.d(LOG_TAG, "drawResults on Map = " + radius);
        for (int i = 0; i < resultItemArrayList.size(); i++) {
            //  Log.d(LOG_TAG, "result = " + i);
            ResultItem resultItem = resultItemArrayList.get(i);
            int red = 0;
            int green = 0;

            int downloadSpeed = resultItem.getDownload();
            if (downloadSpeed < 1000) {
                red = (int) downloadSpeed / 10 + 150;
                green = (int) downloadSpeed / 6;
            } else {

                if (downloadSpeed < 5000) {
                    red = 255;
                    green = (int) 155 + downloadSpeed / 100;
                } else {
                    if (downloadSpeed > 10000) {
                        red = 0;
                        green = 255;
                    } else {
                        green = (int) 155 + downloadSpeed / 100;
                        red = (int) (10000 - downloadSpeed) / 20;
                    }
                }
            }


            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(resultItem.getLatitude(), resultItem.getLongitude()))
                    .fillColor(Color.argb(255, red, green, 0))
                    .strokeColor(Color.argb(150, red, green, 0))
                    .radius(radius);
            mMap.addCircle(circleOptions);

        }
    }


    private final Runnable prepareTestTower = new Runnable() {

        @Override
        public void run() {
            try {

                Log.d(LOG_TAG, "prepareTest start");

                Message msg1 = Message.obtain(mHandlerPrepare, 1);
                mHandlerPrepare.sendMessage(msg1);
                Message msg2 = Message.obtain(mHandlerPrepare, 2);
               // msg2.obj = (CircleOptions) circleOptions;
                mHandlerPrepare.sendMessage(msg2);


            } catch (Exception e) {
                Log.d(LOG_TAG, "Exception " + e.toString());
            }

        }
    };

    private void addHeatMap() {
        List<LatLng> list = new ArrayList<>();
       List<WeightedLatLng> weightedLatLngList = new ArrayList<>();

        int[] colors = {
                Color.rgb(127, 0, 0), // red
                Color.rgb(255, 127, 0),    // orange
                Color.rgb(255, 255, 0),    // yellow
                Color.rgb(0, 255, 0),    // green
                Color.rgb(0, 127, 0)    // dark green
        };

        float[] startPoints = {
                0.1f, 0.2f, 0.4f, 0.6f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);

        for (int i = 0; i < resultItemArrayList.size(); i++) {
          /*  list.add(new LatLng(resultItemArrayList.get(i).getLatitude(),
                    resultItemArrayList.get(i).getLongitude()));*/
            Log.d(LOG_TAG, "Весовой коэф " + resultItemArrayList.get(i).getDownload()/100);

            WeightedLatLng weightedLatLn = new WeightedLatLng(new LatLng(
                    resultItemArrayList.get(i).getLatitude(),
                    resultItemArrayList.get(i).getLongitude()),
                    resultItemArrayList.get(i).getDownload()/100);
            weightedLatLngList.add(weightedLatLn);
        }



        // Get the data: latitude/longitude positions of police stations.

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
//                .data(list)
                .weightedData(weightedLatLngList)
                .gradient(gradient)
                .radius(20)
                .build();

        // Add a tile overlay to the map, using the heat map tile provider.
       mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
       // mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }


    public void drawTower() {

        ArrayList<Tower> towerList = new ArrayList();
        //ArrayList<Integer> towercolor = new ArrayList();
        boolean newtower = true;
        int maincolor = Color.WHITE;

        for (int i = 16; i < resultItemArrayList.size(); i++) {
          //  Log.d(LOG_TAG, "result = " + i);
            newtower = true;
            ResultItem resultItem = resultItemArrayList.get(i);

            resultItem.getLac();
            resultItem.getPsd();
            resultItem.getCid();
            for (int j = 0; j < towerList.size(); j++) {
            //    Log.d(LOG_TAG, "towerList.size() = " + towerList.size());
            //    Log.d(LOG_TAG, "resultItem.getCid() = " + resultItem.getCid());
            //    Log.d(LOG_TAG, "towerList.get(j).getId() = " + j + " - " + towerList.get(j).getId());

                if (resultItem.getCid() == towerList.get(j).getId()) {
                    newtower = false;
                    maincolor = towerList.get(j).getColor();
                    break;
                }
            }
            //  Log.d(LOG_TAG, "towerList.size() = " + towerList.size());
            if (newtower) {
                int red = 0;
                int green = 0;
                int blue = 0;

                if (towerList.size() <= 10) {
                    red = 25 * towerList.size();
                    green = 25 * towerList.size();
                    blue = 0;
                } else {

                    if (towerList.size() <= 20) {
                        red = (int) 0;
                        green = (int) 25 * (towerList.size() - 10);
                        blue = (int) 25 * (towerList.size() - 10);
                    } else {
                        if (towerList.size() <= 30) {
                            red = (int) 25 * (towerList.size() - 20);
                            ;
                            green = 0;
                            blue = (int) 25 * (towerList.size() - 20);
                        } else {
                            if (towerList.size() <= 40) {
                                red = (int) 50 * (towerList.size() - 30);
                                green = (int) 250 - 20 * (towerList.size() - 30);
                                blue = (int) 250 - 20 * (towerList.size() - 30);
                            } else {
                                if (towerList.size() <= 50) {
                                    red = (int) 250 - 25 * (towerList.size() - 40);
                                    green = (int) 250 - 10 * (towerList.size() - 40);
                                    blue = (int) 250 - 10 * (towerList.size() - 40);
                                }
                            }
                        }

                    }
                }


                maincolor = Color.argb(255, red, green, blue);
                if (towerList.size() > 50) {
                    maincolor = Color.WHITE;
                }
                towerList.add(new Tower(resultItem.getCid(), Color.WHITE));

            }


            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(resultItem.getLatitude(), resultItem.getLongitude()))
                    .fillColor(maincolor)
                    .strokeColor(maincolor)
                    .radius(radius);
            mMap.addCircle(circleOptions);

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

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (currentzoom != (int) mMap.getCameraPosition().zoom) {
                    currentzoom = (int) mMap.getCameraPosition().zoom;
                    if (currentzoom > 15) {
                        radius = 2 + (22 - currentzoom);
                    } else {
                        radius = (19 - currentzoom) * (19 - currentzoom);
                    }

                    Log.d(LOG_TAG, "radius" + radius);
                    addHeatMap();

                    new Thread(prepareTest).start();
                    Log.d(LOG_TAG, "onCameraMove" + currentzoom);
                }

                // Log.d(LOG_TAG, "onCameraMove" + zoom);
            }
        });

        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        /*
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(resultItemArrayList.get(1).getLatitude(), resultItemArrayList.get(1).getLongitude()))
                .fillColor(Color.argb(255, 50, 240, 0))
                .radius(10);

        Circle circle = mMap.addCircle(circleOptions);
*/


        new Thread(prepareTest).start();


       // drawTower();

    }

    public void onClickLocationSettings(View view) {
        startActivity(new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    ;


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
