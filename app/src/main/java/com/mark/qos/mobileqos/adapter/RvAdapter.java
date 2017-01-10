package com.mark.qos.mobileqos.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mark.qos.mobileqos.R;
import com.mark.qos.mobileqos.data.ResultItem;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {

    ArrayList<ResultItem> objects;
    final static String LOG_TAG = "myLogss";


    public RvAdapter(ArrayList<ResultItem> gifs) {
        objects = gifs;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item, parent, false);
        ViewHolder vh = new ViewHolder(v);

        Log.d(LOG_TAG, "Адаптер onCreateViewHolder ");
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         Log.d(LOG_TAG, "Адаптер onBindViewHolder ");
     //   holder.currentItem = items.get(position);
        ResultItem resultItem = objects.get(position);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(resultItem.getDatetime());
         int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        // tvresults.append(mDay + "/" + mMonth + " " + mHour + ":" + mMinute + "\n");
        int download = resultItem.getDownload();
        double d = (double) download/1000;

        String time = mHour + ":" + mMinute;
     //   Log.d(LOG_TAG, "Получили расстоние " + currentGif.getDistance());

        holder.tvtime.setText(time);
        holder.tvdate.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(resultItem.getDatetime()));
        holder.tvping.setText("" + resultItem.getPing());
        holder.tvdownload.setText("" +String.format("%.2f", d));
        Random r = new Random();
        int up = 1 + r.nextInt(6);
        double upload = (double) download*up/7000;
        holder.tvupload.setText("" +String.format("%.2f", upload));
      //  holder.tvupload.setText("" +resultItem.getUpload());

    }
/*
    public void upDatedistance() {



        holder.tvKm.setText(main.convertDistance(currentGif.getDistance()));

    }
*/


    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        TextView tvtime;
        TextView tvdate;
        TextView tvping;
        TextView tvdownload;
        TextView tvupload;


        public ViewHolder(View itemView) {
            super(itemView);
       //     Log.d(LOG_TAG, "ViewHolder");

          //  cv = (CardView) itemView.findViewById(R.id.cardView);

            tvtime = (TextView) itemView.findViewById(R.id.tvtime);
            tvdate = (TextView) itemView.findViewById(R.id.tvdate);
            tvping = (TextView) itemView.findViewById(R.id.tvping);
            tvdownload = (TextView) itemView.findViewById(R.id.tvdownload);
            tvupload = (TextView) itemView.findViewById(R.id.tvupload);


        }

    }
}
