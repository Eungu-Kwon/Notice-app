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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
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
    public boolean[] delete_list = null;
    Context context;
    boolean enable = true, isDeleting = false;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView text_title, text_time;
        Switch sw;
        CheckBox delete_check;
        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            text_title = itemView.findViewById(R.id.alarmcontent);
            text_time = itemView.findViewById(R.id.cell_time);
            sw = itemView.findViewById(R.id.is_item_enable);
            delete_check = itemView.findViewById(R.id.delete_checkBox);
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
        delete_list = new boolean[aData.size()];
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alarm_list_item, viewGroup, false);
        AlarmListAdapter.ViewHolder vh = new AlarmListAdapter.ViewHolder(view);
        return vh;
    }

    public void setDeleteMode(boolean state){
        isDeleting = state;
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final int idx = i;
        final AlarmDBHelper dbHelper = new AlarmDBHelper(context, "ALARM_TABLE", null, 1);
        final DBData data = dbHelper.getData(idx);
        if(data == null) return;
        String title = aData.get(i).getTitle();
        Boolean isChecked = aData.get(i).getToggleSw();
        viewHolder.text_title.setText(title);
        viewHolder.text_time.setText(data.getTimeToText());
        viewHolder.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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

        if(isDeleting){
            viewHolder.delete_check.setVisibility(View.VISIBLE);
            viewHolder.sw.setVisibility(View.GONE);
            viewHolder.delete_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    delete_list[idx] = isChecked;
                }
            });
        }
        else{
            delete_list = new boolean[aData.size()];
            viewHolder.delete_check.setVisibility(View.GONE);
            viewHolder.sw.setVisibility(View.VISIBLE);
        }
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
