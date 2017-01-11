package com.mark.qos.mobileqos.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mark.qos.mobileqos.MainActivity;
import com.mark.qos.mobileqos.R;
import com.mark.qos.mobileqos.adapter.RvAdapter;
import com.mark.qos.mobileqos.object.ResultItem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    Button btnsave;
    ExpandableListView expandableListView;
    RecyclerView recyclerView;

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

        btnsave = (Button) v.findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportCVS();
            }
        });

        tvresults = (TextView) v.findViewById(R.id.tvresults);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleview);


        Log.d(LOG_TAG, "resultItemArrayList.size = " + resultItemArrayList.size());
        tvresults.setText("Всего " +  resultItemArrayList.size() + " измерений");
       // tvresults.append("\n");
       // demo();
        RvAdapter adapter = new RvAdapter(resultItemArrayList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        adapter.notifyDataSetChanged();

        return v;
    }
/*
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
*/
    public void exportCVS() {

        String FileString = "Time;IDsubscriber;Download;Upload;Ping;PacketLost;latintude;Longitude;Speed;MCC;MNC;LAC;PSD;CID;Signal Level;ASU;" + "\n";
        for (int i = resultItemArrayList.size()-1; i>0; i--) {
            ResultItem resultItem = resultItemArrayList.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(resultItem.getDatetime());
            int mHour = calendar.get(Calendar.HOUR_OF_DAY);
            int mMinute = calendar.get(Calendar.MINUTE);

            FileString += "" +DateFormat.getDateInstance(DateFormat.SHORT).format(resultItem.getDatetime()) + " "
                    +  mHour + ":" + mMinute  + ";";
            FileString += resultItem.getId_subscriber() + ";";
            FileString += resultItem.getDownload() + ";";
            FileString += resultItem.getUpload() + ";";
            FileString += resultItem.getPing() + ";";
            FileString += resultItem.getPacketlost() + ";";
            FileString += resultItem.getLatitude() + ";";
            FileString += resultItem.getLongitude() + ";";
            FileString += resultItem.getSpeed() + ";";
            FileString += resultItem.getMcc() + ";";
            FileString += resultItem.getMnc() + ";";
            FileString += resultItem.getLac() + ";";
            FileString += resultItem.getPsd() + ";";
            FileString += resultItem.getCid() + ";";
            FileString += resultItem.getSignallevel() + ";";
            FileString += resultItem.getAsulevel() + ";" + "\n";;

        }

        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + "MobileQOS");
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, "data.csv");
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write(FileString);
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
            Toast.makeText(this.getActivity(),  "Файл записан на: " + sdFile.getAbsolutePath() + "", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
/*
    public void share() {
        //String shareBody = "Here is the share content body";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
    }

    private class SaveTask extends AsyncTask<Void, Void, String> {

        private ProgressDialog dialog = ProgressDialog.show(MainActivity.this,
                getString(R.string.saving_title),
                getString(R.string.saving_to_sd), true);

        protected String doInBackground(Void... none) {
            mCanvas.getThread().freeze();
            String pictureName = getUniquePictureName(getSaveDir());
            saveBitmap(pictureName);
            return pictureName;
        }
        */

/*
        protected void onPostExecute(String pictureName) {

            Uri uri = Uri.fromFile(new File(pictureName));
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            dialog.hide();
            mCanvas.getThread().activate();

        }
    }

    private void startShareActivity(String pictureName) {
        Uri uri = Uri.fromFile(new File(pictureName));
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType(PICTURE_MIME);
        i.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(i,
                getString(R.string.share_image_title)));
    }

    private String getSaveDir() {
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + '/' + getString(R.string.app_name) + '/';

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }
    */




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
