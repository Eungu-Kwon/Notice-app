package com.eungu.notice.list_maker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.eungu.notice.AlarmReceiver;
import com.eungu.notice.DBManager.AlarmDBHelper;
import com.eungu.notice.DBManager.DBData;
import com.eungu.notice.MainActivity;
import com.eungu.notice.R;

import java.util.ArrayList;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {
    //TODO Make OnClickListener
    private ArrayList<AlarmListItem> aData = null;
    Context context;
    boolean enable = true;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView1 ;
        Switch sw;
        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.alarmcontent) ;
            sw = itemView.findViewById(R.id.is_item_enable);
        }
    }

    public  AlarmListAdapter(Context context, ArrayList<AlarmListItem> list){
        this.context = context;
        aData = list;
    }
    @NonNull
    @Override
    public AlarmListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alarm_list_item, viewGroup, false);
        AlarmListAdapter.ViewHolder vh = new AlarmListAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final int idx = i;
        String title = aData.get(i).getTitle();
        Boolean isChecked = aData.get(i).getToggleSw();
        viewHolder.textView1.setText(title);
        viewHolder.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlarmDBHelper dbHelper = new AlarmDBHelper(context, "ALARM_TABLE", null, 1);
                DBData data = dbHelper.getData(idx);
                if(isChecked == (data.isNowEnable()==1)) return;
                aData.get(idx).setToggleSw(isChecked);
                data.setNowEnable(isChecked);
                dbHelper.updateData(data, idx);

                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                Intent mAlarmIntent = new Intent(context, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idx, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                if(isChecked){
                    if(Build.VERSION.SDK_INT >= 23)
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, data.getTime().getTimeInMillis(), pendingIntent);
                    else if(Build.VERSION.SDK_INT >= 19)
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, data.getTime().getTimeInMillis(), pendingIntent);
                    else
                        alarmManager.set(AlarmManager.RTC_WAKEUP, data.getTime().getTimeInMillis(), pendingIntent);
                }
                else{
                    if(pendingIntent != null){
                        alarmManager.cancel(pendingIntent);
                        pendingIntent.cancel();
                    }
                }
            }
        });
        viewHolder.sw.setChecked(isChecked);
        viewHolder.sw.setEnabled(enable);
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public int getItemCount() {
        return aData.size();
    }
}
