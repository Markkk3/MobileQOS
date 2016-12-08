package com.mark.qos.mobileqos.test;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by tushkevich_m on 21.11.2016.
 */

public class DownloadTest  {

    private static final String TAG = DownloadTest.class.getSimpleName();


    private static final double EDGE_THRESHOLD = 176.0;
    private static final double BYTE_TO_KILOBIT = 0.0078125;
    private static final double KILOBIT_TO_MEGABIT = 0.0009765625;

    private final String downloadFileUrl350 = "http://sp1.life.com.by/random350x350.jpg"; // 245 388
    private final String downloadFileUrl500 = "http://sp1.life.com.by/random500x500.jpg"; // 505 544
    private final String downloadFileUrl1000 = "http://sp1.life.com.by/random1000x1000.jpg"; // 1 986 284
    private final String downloadFileUrl1500 = "http://sp1.life.com.by/random1500x1500.jpg"; // 4 468 241
    private final String downloadFileUrl2000 = "http://sp1.life.com.by/random2000x2000.jpg"; // 7 907 740


    private static final int EXPECTED_SIZE_1000 = 1986284;

    private final int MSG_UPDATE_STATUS = 0;
    private final int MSG_UPDATE_CONNECTION_TIME = 1;
    private final int MSG_COMPLETE_STATUS = 2;
    private final static int UPDATE_THRESHOLD = 500;
    private final static int UPDATE_THRESHOLD_PREPARE = 300;
    final String LOG_TAG = "myLogss";
    ArrayList<Long> speedFirst = new ArrayList<>();
    ArrayList<Long> timeFirst = new ArrayList();
    ArrayList<Long> speedSecond = new ArrayList<>();
    ArrayList<Long> timeSecond = new ArrayList();
    ArrayList<Long> speedThird = new ArrayList<>();
    ArrayList<Long> timeThird = new ArrayList();

    private boolean threadFirstRun = false;
    private boolean threadSecondRun = false;
    private boolean stopAllThread = false;
    private boolean threadThirdRun = false;
    private long updatedelta = 0;
    String ulrset;
    int downloadSizeFirst = 0;
    int downloadSizeSecond = 0;
    int downloadSizeThird = 0;

    TimeThread timeThread ;
    ArrayList<Float> resultSpeedB = new ArrayList();
    ArrayList<Long> resultTimeB= new ArrayList();
    GraphView graphView;
    TextView tvDownloadSpeed;
    Thread prepareTestThread;
    DownloadTestInterface downloadTestInterface;
    float finishSpeed =0;
    //Thread mWorkerFirstThread = new Thread(mWorkerFirst);


    public void registerCallBack(DownloadTestInterface callback){
        this.downloadTestInterface = callback;
    }
    public DownloadTest(GraphView graphView, TextView tvDownloadSpeed) {

        this.graphView = graphView;
        this.tvDownloadSpeed = tvDownloadSpeed;
        prepareTestThread = new Thread(prepareTest);;

    }
/*
    @Override
    protected void doInBackground(String... strings) {

        start();
        prepareTestThread.start();

        return null;
    }*/

    public void start() {
        speedFirst.clear();
        timeFirst.clear();
        speedSecond.clear();
        timeSecond.clear();
        speedThird.clear();
        timeThird.clear();

        new Thread(prepareTest).start();

    }



    private final Handler mHandlerPrepare = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {

                case MSG_COMPLETE_STATUS:


                    int time = msg.arg1;
                    int preparespeed = msg.arg2;
                    Log.d(LOG_TAG, "time = " + time);
                    if (time < 201) {
                        ulrset = downloadFileUrl2000;
                    } else {
                        if (time < 700) {
                            ulrset = downloadFileUrl1500;
                        } else {
                            ulrset = downloadFileUrl1000;
                        }
                    }


                    downloadSizeFirst = 0;
                    downloadSizeSecond = 0;
                    downloadSizeThird = 0;
                 //   mTxtStatus.setText("Основной тест " + preparespeed + " t=" + time);
                    //      mBtnStart.setEnabled(true);
                    timeThread = new TimeThread();
                    timeThread.start();
                    Thread mWorkerFirstThread = new Thread(mWorkerFirst);
                    mWorkerFirstThread.setPriority(8);
                    mWorkerFirstThread.start();
                    Thread mWorkerSecondThread = new Thread(mWorkerSecond);
                    mWorkerSecondThread.setPriority(8);
                    mWorkerSecondThread.start();
                    Thread mWorkerThirdThread = new Thread(mWorkerThird);
                    mWorkerThirdThread.setPriority(8);
                    mWorkerThirdThread.start();

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private final Runnable prepareTest = new Runnable() {

        @Override
        public void run() {
            InputStream stream = null;
            try {

                File file = File.createTempFile("Mustachify", "downloadp");
                Log.d(LOG_TAG, "mWorker2 ");
                URL url = new URL(downloadFileUrl350);
                BufferedInputStream bis = new BufferedInputStream(url.openStream());
                FileOutputStream fis = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int count = 0;
                long start = System.currentTimeMillis();
                long old = start;
                long t = 0;
                int j = 0;
                int downloadSize = 0;
                while ((count = bis.read(buffer, 0, 1024)) != -1) {
                    downloadSize += 1024;
                    if (j == 10) {
                        t = System.currentTimeMillis() - old;
                        Log.d(LOG_TAG, "updateDelta: " + 1024);
                        Log.d(LOG_TAG, "count: " + count);
                        Log.d(LOG_TAG, "t= " + t);
                        fis.write(buffer, 0, count);
                        old = System.currentTimeMillis();
                        j = 0;
                    }
                    j++;
                }

                int bytesIn = downloadSize;
                long downloadTime = (System.currentTimeMillis() - start);
                Log.d(LOG_TAG, "time: " + downloadTime);
                Log.d(LOG_TAG, "size: " + downloadSize);
                fis.close();
                bis.close();
                Log.d(LOG_TAG, "SPEED med: " + 8 * downloadSize / downloadTime);
                Log.d(LOG_TAG, "bytein: " + bytesIn);

                Message msg = Message.obtain(mHandlerPrepare, MSG_COMPLETE_STATUS);
                msg.arg1 = (int) downloadTime;
                msg.arg2 = (int) (8 * downloadSize / downloadTime);
                mHandlerPrepare.sendMessage(msg);

            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    //Suppressed
                }
            }

        }
    };

    private final Runnable mWorkerFirst = new Runnable() {

        @Override
        public void run() {
            InputStream stream = null;
            threadFirstRun = true;
            try {


                downloadSizeFirst=0;
                long t = 0;
                long old = 0;
                int j = 0;
                Log.d(LOG_TAG, "mWorker1 ");
                URL url = new URL(ulrset);
                int count = 0;
                long start = System.currentTimeMillis();

                InputStream is = new BufferedInputStream(url.openStream());

                //   InputStream is = new URL(ulrset).openStream();
                byte[] buffer = new byte[1024];

                while ((count = is.read(buffer)) != -1) {
                    //     Log.d(LOG_TAG, "count: "+ count);
                    //     Log.d(LOG_TAG, "buf: "+ buffer.length);
                    //   count=count + 1;
                    downloadSizeFirst += count;
                    if (stopAllThread) {
                        break;
                    }
                }

                Log.d(LOG_TAG, "закгружено всего1: " + downloadSizeFirst);
                long downloadTime = (System.currentTimeMillis() - start);
                Log.d(LOG_TAG, "timeFirst1: " + downloadTime);
                //  fis.close();
                is.close();

                Message msg = Message.obtain(mHandler, MSG_COMPLETE_STATUS, calculate(downloadTime, downloadSizeFirst));
                msg.arg1 = downloadSizeFirst;
                msg.arg2 = 1;
                mHandler.sendMessage(msg);

            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    //Suppressed
                }
            }

        }
    };

    private final Runnable mWorkerSecond = new Runnable() {


        @Override
        public void run() {

            InputStream stream = null;
            threadSecondRun = true;
            try {

                downloadSizeSecond = 0;
                File file = File.createTempFile("Mustachify", "download2");
                Log.d(LOG_TAG, "mWorker2 ");
                URL url = new URL(ulrset);

                int count = 0;
                long start = System.currentTimeMillis();
                InputStream is = new BufferedInputStream(url.openStream());
                byte[] buffer = new byte[1024];

                while ((count = is.read(buffer)) != -1) {
                    // Log.d(LOG_TAG, "count2: "+ count);
                    // count=count + 1;
                    downloadSizeSecond += count;
                    if (stopAllThread) {
                        break;
                    }
                }

                Log.d(LOG_TAG, "закгружено всего2: " + downloadSizeSecond);
                long downloadTime = (System.currentTimeMillis() - start);
                Log.d(LOG_TAG, "timeSecond2: " + downloadTime);
                // fis.close();
                is.close();
                // bis.close();

                Message msg = Message.obtain(mHandler, MSG_COMPLETE_STATUS, calculate(downloadTime, downloadSizeSecond));
                msg.arg1 = downloadSizeSecond;
                msg.arg2 = 2;
                mHandler.sendMessage(msg);

            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    //Suppressed
                }
            }

        }
    };


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_STATUS:
                    final SpeedInfo info1 = (SpeedInfo) msg.obj;

                    // Title progress is in range 0..10000
                    //setProgress(100 * msg.arg1);
                    //    mTxtProgress.setText(String.format(getResources().getString(R.string.update_downloaded), msg.arg2, EXPECTED_SIZE_1000));
                    break;

                case MSG_COMPLETE_STATUS:
                    final SpeedInfo info2 = (SpeedInfo) msg.obj;

                    switch (msg.arg2) {
                        case 1:
                            threadFirstRun = false;
                            break;
                        case 2:
                            threadSecondRun = false;
                            break;
                        case 3:
                            threadThirdRun = false;
                            break;
                    }


                    if (!threadFirstRun && !threadSecondRun && !threadThirdRun) {
                        timeThread.interrupt();

                        if (timeThread != null) {
                            Thread dummy = timeThread;
                            timeThread = null;
                            dummy.interrupt();
                        }
                        creategraph();
                        //   speedCalculation2();
                    }


                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private final Runnable mWorkerThird = new Runnable() {


        @Override
        public void run() {

            InputStream stream = null;
            threadThirdRun = true;
            try {

                downloadSizeThird = 0;
                File file = File.createTempFile("Mustachify", "download3");
                Log.d(LOG_TAG, "mWorker3 ");
                URL url = new URL(ulrset);
                FileOutputStream fis = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int count = 0;
                long start = System.currentTimeMillis();
                InputStream is = new BufferedInputStream(url.openStream());

                while ((count = is.read(buffer)) != -1) {
                    // Log.d(LOG_TAG, "count3: "+ count);
                    //  fis.write(buffer, 0, count);

                    downloadSizeThird += count;
                    if (stopAllThread) {
                        break;
                    }
                }

                //  Log.d(LOG_TAG, "закгружено всего2: " + bytesIn);
                long downloadTime = (System.currentTimeMillis() - start);
                Log.d(LOG_TAG, "timeSecond3: " + downloadTime);
                is.close();
                //  fis.close();
                //  bis.close();

                Message msg = Message.obtain(mHandler, MSG_COMPLETE_STATUS, calculate(downloadTime, downloadSizeThird));
                msg.arg1 =  downloadSizeThird;
                msg.arg2 = 3;
                mHandler.sendMessage(msg);

            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    //Suppressed
                }
            }

        }
    };



    private class TimeThread extends Thread { // описываем объект Runnable в конструкторе
        public void run() {
            stopAllThread = false;
            while (!Thread.currentThread().isInterrupted()) {
                long l = System.currentTimeMillis();
                //  Log.d(LOG_TAG, "поток работает" + Thread.currentThread().isInterrupted());
                speedFirst.add((long)downloadSizeFirst);
                speedSecond.add((long)downloadSizeSecond);
                speedThird.add((long)downloadSizeThird);
                Log.d(LOG_TAG, "заргужено для 1 = " + downloadSizeFirst);
                Log.d(LOG_TAG, "загружено для 2 = " + downloadSizeSecond);
                Log.d(LOG_TAG, "загружено для 3 = " + downloadSizeThird);

                timeFirst.add(l);
                timeSecond.add(l);
                timeThird.add(l);
                if(timeFirst.size() > 12000/UPDATE_THRESHOLD) {
                    stopAllThread = true;
                    Log.d(LOG_TAG, "останавливаем загрузку, размер = " + timeFirst.size());
                }


                if (!threadFirstRun && !threadSecondRun) {

                }

                try {
                    Thread.sleep(UPDATE_THRESHOLD);
                } catch (InterruptedException e) {

                    return;
                }
            }


        }

    }

    private int speedCalculation() {

        long mintime = timeFirst.get(0);
        long maxtime = timeFirst.get(0);
        long endtime = timeFirst.get(timeFirst.size() - 1);
        long starttime = timeFirst.get(0);

        if (endtime > timeSecond.get(timeSecond.size() - 1)) {
            endtime = timeSecond.get(timeSecond.size() - 1);
        }

        if (starttime < timeSecond.get(0)) {
            starttime = timeSecond.get(0);
        }

        //   Log.d(LOG_TAG, "время остановки самого быстрого потока " + endtime);
        //   Log.d(LOG_TAG, "время запуска самого тормознутого потока " + starttime);

        long delta = endtime-starttime;
        //    Log.d(LOG_TAG, "время загрузки для расчета " + delta);
        float d = delta*0.2f;
        //    Log.d(LOG_TAG, "доавим  " + d);
        long t = starttime + (long) d;
        //   Log.d(LOG_TAG, "Начало измерения " + t);

        float speed1 = 0;
        float speed2 = 0;
        long sumSpeed1=0;
        int count=0;
        for (int i = 0; i < timeFirst.size(); i++) {
            if (timeFirst.get(i)> t && timeFirst.get(i)<endtime) {
                //        Log.d(LOG_TAG, "время от начала1= " + (timeFirst.get(i)-starttime));
                sumSpeed1 = sumSpeed1 + speedFirst.get(i);
                count++;
            }
        }
        Log.d(LOG_TAG, "count1= " + count);
        if(count!=0) {
            speed1 = sumSpeed1 / count;
            //      Log.d(LOG_TAG, "sumSpeed1 = " + speed1);
        }
        count=0;
        long sumSpeed2=0;
        for (int i = 0; i < timeSecond.size(); i++) {
            if (timeSecond.get(i)> t && timeSecond.get(i)<endtime) {
                //      Log.d(LOG_TAG, "время от начала2= " + (timeSecond.get(i)-starttime));
                sumSpeed2 = sumSpeed2 + speedSecond.get(i);
                count++;
            }
        }
        Log.d(LOG_TAG, "count2= " + count);
        if(count!=0) {
            speed2 = sumSpeed2 / count;
            //   Log.d(LOG_TAG, "sumSpeed2 = " + speed2);


           // tvDownloadSpeed.setText("" +  (speed1+speed2) + " kb/s");
        }

        return 1;
    }

    private void speedCalculation2() {
        float speed1 = 0;
        float speed2 = 0;
        float speed3 = 0;
        long start = 0;

        for (int i = 0; i < timeFirst.size(); i++) {

            if (timeFirst.get(i) !=0) {
                Log.d(LOG_TAG, "timeFirst.get(i) " + timeFirst.get(i));
                start = timeFirst.get(i);
                break;
            }
        }

        for (int i = timeFirst.size()-1; i >0; i--) {

            if (speedFirst.get(i)!=speedFirst.get(i-1)) {
                Log.d(LOG_TAG, "speedFirst.get(i) " + speedFirst.get(i));
                speed1 = (float) 8*speedFirst.get(i)/(timeFirst.get(i)-start);
                break;
            }
        }

        for (int i = timeSecond.size()-1; i >0; i--) {

            if (speedSecond.get(i)!=speedSecond.get(i-1)) {
                Log.d(LOG_TAG, "speedSecond.get(i)" + speedSecond.get(i));
                speed2 = (float) 8*speedSecond.get(i)/(timeSecond.get(i)-start);
                break;
            }
        }

        for (int i = timeSecond.size()-1; i >0; i--) {

            if (speedThird.get(i)!=speedThird.get(i-1)) {
                Log.d(LOG_TAG, "speedThrid.get(i)" + speedThird.get(i));
                speed3 = (float) 8*speedThird.get(i)/(timeThird.get(i)-start);
                break;
            }
        }

       // tvDownloadSpeed.setText("" +  (speed1 + speed2 + speed3) + " kb/s");

    }

    private void creategraph() {

        // Log.d(LOG_TAG, "speed.size() " + speedFirst.size());
        // Log.d(LOG_TAG, "time.size() " + timeFirst.get(timeFirst.size() - 1));

        //  Log.d(LOG_TAG, "time1.size() " + timeFirst.get(0));
        //  Log.d(LOG_TAG, "time2.size() " + timeSecond.get(0));

        //   Log.d(LOG_TAG, "разница в запуске 2 =" + (timeFirst.get(0) - timeSecond.get(0)));
/*
        GraphView graph1 = (GraphView) findViewById(R.id.graph1);
        GraphView graph2 = (GraphView) findViewById(R.id.graph2);
        GraphView graph3 = (GraphView) findViewById(R.id.graph3);
        GraphView graph4 = (GraphView) findViewById(R.id.graph4);
        GraphView graph5 = (GraphView) findViewById(R.id.graph5);
        GraphView graph6 = (GraphView) findViewById(R.id.graph6);
        GraphView graph7 = (GraphView) findViewById(R.id.graph7);
        graph1.removeAllSeries();
        graph2.removeAllSeries();
        graph3.removeAllSeries();
        graph4.removeAllSeries();
        graph5.removeAllSeries();
        graph6.removeAllSeries();
        graph7.removeAllSeries();
        */

        graphView.removeAllSeries();
        long mintime = timeFirst.get(0);
        long maxtime = timeFirst.get(0);

        for (int i = 0; i < timeFirst.size(); i++) {
            if (mintime > timeFirst.get(i)) {
                mintime = timeFirst.get(i);
            }

            if (maxtime < timeFirst.get(i)) {
                maxtime = timeFirst.get(i);
            }
        }
        for (int i = 0; i < timeSecond.size(); i++) {
            if (mintime > timeSecond.get(i)) {
                mintime = timeSecond.get(i);
            }

            if (maxtime < timeSecond.get(i)) {
                maxtime = timeSecond.get(i);
            }
        }


        //  Log.d(LOG_TAG, "mintime = " + mintime);
        //  Log.d(LOG_TAG, "maxtime = " + maxtime);
        //  Log.d(LOG_TAG, "разница = " + (maxtime - mintime));
        DataPoint dataPoint1[] = new DataPoint[speedFirst.size()-1];
        for (int i = 0; i < speedFirst.size()-1; i++) {
            long l = speedFirst.get(i+1) - speedFirst.get(i);
            //     Log.d(LOG_TAG, "загрузили 1 за период: " + l);
            //     Log.d(LOG_TAG, "timeFirst.get(i) =  " + (timeFirst.get(i)-mintime)/100);
            dataPoint1[i] = new DataPoint((timeFirst.get(i) - mintime)/100, (speedFirst.get(i+1) - speedFirst.get(i))/1024);
        }

        DataPoint dataPoint2[] = new DataPoint[speedSecond.size()-1];
        for (int i = 0; i < speedSecond.size()-1; i++) {
            long l = speedSecond.get(i+1) - speedSecond.get(i);
            //    Log.d(LOG_TAG, "загрузили 2 за период: " + l);
            dataPoint2[i] = new DataPoint((timeSecond.get(i) - mintime)/100, (speedSecond.get(i+1)- speedSecond.get(i))/1024);
        }

        DataPoint dataPoint3[] = new DataPoint[speedSecond.size()-1];
        for (int i = 0; i < speedThird.size()-1; i++) {
            long l = speedThird.get(i+1) - speedThird.get(i);
            //   Log.d(LOG_TAG, "загрузили 3 за период: " + l);
            //   Log.d(LOG_TAG, "i= " + i);
            //  Log.d(LOG_TAG, "размер= " +  speedThird.size());
            dataPoint3[i] = new DataPoint((timeThird.get(i) - mintime)/100, (speedThird.get(i+1)- speedThird.get(i))/1024);
        }

        DataPoint dataPoint5[] = new DataPoint[speedSecond.size()-1];
        for (int i = 0; i < speedFirst.size()-1; i++) {
            long sum1= (speedFirst.get(i+1)-speedFirst.get(i));
            long sum2 =(speedSecond.get(i+1)-speedSecond.get(i));
            long sum3 =(speedThird.get(i+1)-speedThird.get(i));
            //  Log.d(LOG_TAG, "sum двух потоков1 = " + sum1);
            //  Log.d(LOG_TAG, "sum двух потоков2 = " + sum2);
            long g = sum1+sum2 + sum3;
            //  Log.d(LOG_TAG, "sum двух потоков всего = " + g);

            dataPoint5[i] = new DataPoint((timeSecond.get(i) - mintime)/100, g);
        }

        DataPoint dataPoint6[] = new DataPoint[speedSecond.size()-1];
        for (int i = 0; i < speedFirst.size()-1; i++) {
            long sum= (speedFirst.get(i+1)-speedFirst.get(i)) + (speedSecond.get(i+1)-speedSecond.get(i)) + (speedThird.get(i+1)-speedThird.get(i));
            // Log.d(LOG_TAG, "sum двух потоков для скорости = " + sum);
            long timeper = (timeFirst.get(i+1) -timeFirst.get(i));
            float speeed =  (float)8*sum/timeper;
            // Log.d(LOG_TAG, "speed = " + speeed);
            dataPoint6[i] = new DataPoint((timeFirst.get(i) - mintime)/100, speeed);
        }



        //  speedSecond = changeArray(speedSecond);
        DataPoint dataPoint41[] = new DataPoint[speedSecond.size()];
        DataPoint dataPoint42[] = new DataPoint[speedSecond.size()];
        DataPoint dataPoint43[] = new DataPoint[speedSecond.size()];
        for (int i = 0; i < speedSecond.size(); i++) {
            //   dataPoint4[i] = new DataPoint(timeSecond.get(i) - mintime, speedSecond.get(i));

            dataPoint41[i] = new DataPoint((timeFirst.get(i) - mintime)/100,  speedFirst.get(i)/1024);
            dataPoint42[i] = new DataPoint((timeSecond.get(i) - mintime)/100, speedSecond.get(i)/1024);
            dataPoint43[i] = new DataPoint((timeThird.get(i) - mintime)/100, speedThird.get(i)/1024);
        }

   LineGraphSeries<DataPoint> series7 = new LineGraphSeries<DataPoint>(Beze());
        /*
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(dataPoint1);
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(dataPoint2);
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>(dataPoint3);
        LineGraphSeries<DataPoint> series41 = new LineGraphSeries<DataPoint>(dataPoint41);
        LineGraphSeries<DataPoint> series42 = new LineGraphSeries<DataPoint>(dataPoint42);
        LineGraphSeries<DataPoint> series43 = new LineGraphSeries<DataPoint>(dataPoint43);
        LineGraphSeries<DataPoint> series5 = new LineGraphSeries<DataPoint>(dataPoint5);
        LineGraphSeries<DataPoint> series6 = new LineGraphSeries<DataPoint>(dataPoint6);
*/
        /*
        graph1.getViewport().setXAxisBoundsManual(true);
        graph2.getViewport().setXAxisBoundsManual(true);
        graph3.getViewport().setXAxisBoundsManual(true);
        graph4.getViewport().setXAxisBoundsManual(true);
        graph5.getViewport().setXAxisBoundsManual(true);
        graph6.getViewport().setXAxisBoundsManual(true);
        graph7.getViewport().setXAxisBoundsManual(true);
        graph1.getViewport().setMinX(0);
        graph2.getViewport().setMinX(0);
        graph3.getViewport().setMinX(0);
        graph4.getViewport().setMinX(0);
        graph5.getViewport().setMinX(0);
        graph6.getViewport().setMinX(0);
        graph7.getViewport().setMinX(0);
        graph1.getViewport().setMinY(0);
        graph2.getViewport().setMinY(0);
        graph3.getViewport().setMinY(0);
        graph4.getViewport().setMinY(0);
        graph5.getViewport().setMinY(0);
        graph6.getViewport().setMinY(0);
        graph7.getViewport().setMinY(0);
        */

       // graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);
       // graphView.getViewport().setMinX(0);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(series7.getHighestValueY()*1.5);
       // graphView.getViewport().setMaxX(series7.length);
       // graphView.getViewport().setMaxX((maxtime - mintime)/100 * 1.1);

       // graphView.getViewport().setXAxisBoundsManual(true);
/*
        graph1.getViewport().setMaxX((maxtime - mintime)/100 * 1.1);
        graph2.getViewport().setMaxX((maxtime - mintime)/100 * 1.1);
        graph3.getViewport().setMaxX((maxtime - mintime)/100 * 1.1);
        graph4.getViewport().setMaxX((maxtime - mintime)/100 * 1.1);
        graph5.getViewport().setMaxX((maxtime - mintime)/100 * 1.1);
        graph6.getViewport().setMaxX((maxtime - mintime)/100 * 1.1);
        graph7.getViewport().setMaxX((maxtime - mintime)/100 * 1.1);
        */


/*
        graph1.addSeries(series1);
        graph2.addSeries(series2);
        graph3.addSeries(series3);
        graph4.addSeries(series41);
        graph4.addSeries(series42);
        graph4.addSeries(series43);
        graph5.addSeries(series5);
        graph6.addSeries(series6);
        graph7.addSeries(series7);
        */
        graphView.addSeries(series7);

        downloadTestInterface.finishDownloadTest(finishSpeed);

    }

    private DataPoint[] Beze() {
        ArrayList<Float> resultSpeed = new ArrayList();
        ArrayList<Long> resultTime= new ArrayList();
        resultTimeB.clear();
        resultSpeedB.clear();

        float sumspeed = 0;
        int k=0;

        long timemMeasurement = timeFirst.get(timeFirst.size()-1) - timeFirst.get(0);

        for (int i = 0; i < speedFirst.size(); i++) {
            resultTime.add(timeFirst.get(i)-timeFirst.get(0));

            if(i<speedFirst.size()-1) {
                long sum1 = (speedFirst.get(i+1) - speedFirst.get(i));
                long sum2 = (speedSecond.get(i+1) - speedSecond.get(i));
                long sum3 = (speedThird.get(i+1) - speedThird.get(i));
                // long sum= (speedFirst.get(i+1)-speedFirst.get(i)) + (speedSecond.get(i+1)-speedSecond.get(i));
                // Log.d(LOG_TAG, "sum двух потоков для скорости = " + sum);
                long timeper = (timeFirst.get(i+1) - timeFirst.get(i));
                float speeed =  (float)8*(sum1+sum2+sum3)/timeper;
                resultSpeed.add(speeed);
                if((timeFirst.get(i) - timeFirst.get(0)) > timemMeasurement*0.15) {
                    Log.d(LOG_TAG, "i = " + i + " timetest = " + timemMeasurement);
                    sumspeed = sumspeed + speeed;
                    k++;
                }
            }
        }
        finishSpeed = (float) sumspeed/k;
        tvDownloadSpeed.setText("" +  finishSpeed + " kb/s");
        Log.d(LOG_TAG, "Скорость = " +  finishSpeed + " kb/s");

        for (int i = 0; i < resultSpeed.size(); i++) {

        }

        //    DataPoint dataPoint7[] = new DataPoint[speedSecond.size()-1];
        float oldSpeed = resultSpeed.get(0);
        long oldTime = resultTime.get(0);
        float currentSpeed = 0;
        long currentSpeed2 = 0;
        long currentTime = 0;
        long currentTime2 = 0;

        for (int i = 0; i < resultSpeed.size()-1; i++) {
            Log.d(LOG_TAG, "i=" + i);
            currentSpeed = (resultSpeed.get(i+1) - oldSpeed);
            currentTime =  (resultTime.get(i+1) - oldTime);
            int stepSpeed = (int) currentSpeed/10;
            int stepTime = (int) currentTime/10;
            // dataPoint7[i] = new DataPoint(currentTime/100, currentSpeed);
            for(int j = 0; j < 4; j++) {

                oldSpeed=  oldSpeed + stepSpeed;
                oldTime = oldTime + stepTime;
                Log.d(LOG_TAG, "j=" + j +" oldSpeed = " + oldSpeed + " OldTime = " + oldTime);
                resultSpeedB.add(oldSpeed);
                resultTimeB.add(oldTime);

            }


        }

        DataPoint dataPoint[] = new DataPoint[resultSpeedB.size()];
        for (int i = 0; i < resultSpeedB.size(); i++) {
            dataPoint[i] = new DataPoint((resultTimeB.get(i))/100, resultSpeedB.get(i)/1000);
        }

        return dataPoint;

    }

    private SpeedInfo calculate(final long downloadTime, final long bytesIn) {
        Log.d(LOG_TAG, "calculat = " + bytesIn + " time = " + downloadTime);
        SpeedInfo info = new SpeedInfo();
        //from mil to sec
        long bytespersecond = (bytesIn / downloadTime) * 1000;
        double kilobits = bytespersecond * BYTE_TO_KILOBIT;
        double megabits = kilobits * KILOBIT_TO_MEGABIT;
        Log.d(LOG_TAG, "bytespersecond = " + bytespersecond);
        Log.d(LOG_TAG, "kilobit = " + kilobits);
        Log.d(LOG_TAG, "megabit = " + megabits);
        info.downspeed = bytespersecond;
        info.kilobits = kilobits;
        info.megabits = megabits;

        return info;
    }

    private static class SpeedInfo {
        public double kilobits = 0;
        public double megabits = 0;
        public double downspeed = 0;
    }



}
