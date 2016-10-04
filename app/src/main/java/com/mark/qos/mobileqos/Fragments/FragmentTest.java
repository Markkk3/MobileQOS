package com.mark.qos.mobileqos.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.mark.qos.mobileqos.R;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class FragmentTest extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
 //   private NumberProgressBar bnp;
    Handler h;

    Button btnStartTest;
    private GraphView graph;
    private LineGraphSeries<DataPoint> series;

    public FragmentTest() {

    }

    public static FragmentTest newInstance(String param1, String param2) {
        FragmentTest fragment = new FragmentTest();
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
        View v = inflater.inflate(R.layout.fragment_fragment_test, container, false);

        btnStartTest = (Button) v.findViewById(R.id.btnstarttest);
        btnStartTest.setOnClickListener(this);

        graph = (GraphView) v.findViewById(R.id.graph);
       // graph.setPivotY(100);
       // graph.setPivotX(25);
        graph.setMinimumHeight(100);
        /*
        series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });

        graph.addSeries(series);
*/


      //  bnp = (NumberProgressBar)v.findViewById(R.id.number_progress_bar);
      //  bnp.setOnProgressBarListener(this);




        return v;
    }


    private void starttest() {
        series = new LineGraphSeries<DataPoint>();

        h = new Handler();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    for (int cnt = 1; cnt < 20; cnt++) {
                        TimeUnit.MILLISECONDS.sleep(1000);
                        // обновляем ProgressBar
                        Random random =  new Random();

                        series.appendData(new DataPoint(cnt, random.nextInt(100)), false, 100);


                        h.post(updateProgress);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();


        /*
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);
        */
    }


    Runnable updateProgress = new Runnable() {
        public void run() {
       //     bnp.incrementProgressBy(5);
            graph.addSeries(series);
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onProgressChange(int current, int max) {
        if(current == max) {
            Toast.makeText(getContext(), getString(R.string.app_name), Toast.LENGTH_SHORT).show();
      //      bnp.setProgress(0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnstarttest:
                starttest();
                    break;
        }
    }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
