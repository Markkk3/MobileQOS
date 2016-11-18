package com.mark.qos.mobileqos.Fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.mark.qos.mobileqos.R;
import com.mark.qos.mobileqos.test.PingAsyncTask;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;


public class FragmentTest extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    final String LOG_TAG = "myLogss";
    private OnFragmentInteractionListener mListener;
 //   private NumberProgressBar bnp;
    Handler h;
    ArrayList<Integer> arrayListPing = new ArrayList();

    Button btnStartTest;
    private Line line;
    private LinePoint linePoint;
    LineGraph li;
    TextView tvpingresult;
    ProgressBar progressBarPing;
    ProgressBar progressBarDownload;


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
        View v = inflater.inflate(R.layout.fragment_test, container, false);

        btnStartTest = (Button) v.findViewById(R.id.btnstarttest);
        btnStartTest.setOnClickListener(this);
        tvpingresult = (TextView) v.findViewById(R.id.tvpingresult);
        progressBarPing = (ProgressBar) v.findViewById(R.id.progressBarPing);
        progressBarPing.setVisibility(View.GONE);
        progressBarDownload = (ProgressBar) v.findViewById(R.id.progressBarDownload);



    //    graph = (GraphView) v.findViewById(R.id.graph);

               
       // graph.setPivotY(100);
       // graph.setPivotX(25);
      //  graph.setMinimumHeight(100);

    //    LineGraph li = (LineGraph)v.findViewById(R.id.graph);
        line = new Line();
        linePoint = new LinePoint();
        linePoint.setX(0);
        linePoint.setY(5);
        line.addPoint(linePoint);
        linePoint = new LinePoint();
        linePoint.setX(1);
        linePoint.setY(15);
        line.addPoint(linePoint);
        linePoint = new LinePoint();
        linePoint.setX(2);
        linePoint.setY(10);
        line.addPoint(linePoint);


        line.setColor(Color.parseColor("#FFBB33"));
        li = (LineGraph)v.findViewById(R.id.graph);
/*
        li = (LineGraph)v.findViewById(R.id.graph);
        li.addLine(line);
        li.setRangeY(0, 100);
        li.setRangeX(0, 20);
        li.setLineToFill(0);
        */
        /*
        Bar d2 = new Bar();
        d2.setColor(Color.parseColor("#FFBB33"));
        d2.setName("Test2");
        d2.setValue(20);

        points.add(d2);



*/
      //  bnp = (NumberProgressBar)v.findViewById(R.id.number_progress_bar);
      //  bnp.setOnProgressBarListener(this);




        return v;
    }

    public void drawGraph(ArrayList<Integer> arrayList) {
        progressBarPing.setVisibility(View.GONE);

        line = new Line();
        li.removeAllLines();
        int max = arrayList.get(2);
        int count = 0;
        int sum = 0;
        /*
        linePoint = new LinePoint();
        linePoint.setX(0);
        linePoint.setY(5);
        line.addPoint(linePoint);

      */
        for (int  j = 0; j<5; j++) {
            int max1 = arrayList.get(0);
            int num = 0;
            for (int i = 0; i < arrayList.size(); i++) {
                if(max1 < arrayList.get(i)) {
                    num = i;
                    max1 = arrayList.get(i);
                }

            }
            Log.d(LOG_TAG, "удаляем: " + num + " "+ arrayList.get(num));
            arrayList.remove(num);
        }

        Log.d(LOG_TAG, "размер: " + arrayList.size());

        for (int  i=0; i<arrayList.size(); i++){
            if (arrayList.get(i)> max) {
                max = arrayList.get(i);
            }
            count++;
            sum += arrayList.get(i);
            linePoint = new LinePoint();
            linePoint.setX(i);
            linePoint.setY(arrayList.get(i));
            line.addPoint(linePoint);
        }

        tvpingresult.setText(sum/count + " мс");



        line.setColor(Color.parseColor("#FFBB33"));

      //  LineGraph li = (LineGraph)v.findViewById(R.id.graph);
        li.addLine(line);
        li.setRangeY(0, (max+ max/10));
        li.setRangeX(0, 14);

        li.setLineToFill(0);

    }


    private void starttest() {
        tvpingresult.setText("");
        progressBarPing.setVisibility(View.VISIBLE);


        InetAddress serv_addr1 = null;
        try {
            serv_addr1 = InetAddress.getByName("93.85.84.234");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Random r = new Random();
        //  portServer = 5002 + r.nextInt(4);

        final int portServer = 52001;
        Log.d("UDP", "Отправляем на порт " + portServer);
        final String mess = "testping";
        final InetAddress serv_addr = serv_addr1;


       // drawGraph(arrayListPing);

        Log.d("UDP", "Получили " + arrayListPing.size());


        h = new Handler();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {

                    try {
                        arrayListPing = new PingAsyncTask(serv_addr, portServer, mess, true).execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                        h.post(updateProgress);

                } catch (Exception e) {
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

            drawGraph(arrayListPing);
       //     bnp.incrementProgressBy(5);
          //  barGraph.setBars(points);
           // graph.addSeries(series);
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
                Log.d(LOG_TAG, "Запускаем тест");
                starttest();
                    break;
        }
    }

    public void setPingResult(int ping) {
        Log.d(LOG_TAG, "Получили пинг = " + ping);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
