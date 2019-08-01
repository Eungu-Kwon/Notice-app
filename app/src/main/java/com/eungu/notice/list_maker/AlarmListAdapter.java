package com.eungu.notice.list_maker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.eungu.notice.R;

import java.util.ArrayList;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {

    private ArrayList<AlarmListItem> aData = null;

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
    public  AlarmListAdapter(ArrayList<AlarmListItem> list){
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String title = aData.get(i).getTitle();
        Boolean isChecked = aData.get(i).getToggleSw();
        viewHolder.textView1.setText(title);
        viewHolder.sw.setChecked(isChecked);

    }

    @Override
    public int getItemCount() {
        return aData.size();
    }
}
