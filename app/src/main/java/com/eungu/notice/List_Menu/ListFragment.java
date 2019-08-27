package com.eungu.notice.List_Menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.Toast;

import com.eungu.notice.DBManager.*;
import com.eungu.notice.Extra.AFragmentListener;
import com.eungu.notice.Extra.ComputeClass;
import com.eungu.notice.List_Menu.list_maker.*;
import com.eungu.notice.R;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    public static final String PREFS_NAME = "SettingFile";

    ArrayList<AlarmListItem> list = null;
    AlarmListAdapter listAdapter = null;
    AFragmentListener fragmentListener;
    boolean isServiceRunning = false, isDeleting = false;
    RecyclerView recyclerView;
    Switch main_switch;

    View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        mView = view;
        button_setting();
        init_list();
        main_sw_init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener = (AFragmentListener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    void button_setting(){
        final ImageButton setting_btn = mView.findViewById(R.id.setting_btn);
        final Button delete_done = mView.findViewById(R.id.delete_done_btn);
        delete_done.setVisibility(View.GONE);

        delete_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmDBHelper dbHelper = new AlarmDBHelper(mView.getContext(), "ALARM_TABLE", null, 1);
                boolean[] delete_list = listAdapter.delete_list;
                for(int i = list.size()-1; i >= 0; i--){
                    if(delete_list[i]){
                        dbHelper.deleteColumn(i);
                        list.remove(i);
                    }
                }
                dbHelper.computeID();
                delete_done.setVisibility(View.GONE);
                setting_btn.setVisibility(View.VISIBLE);
                main_switch.setVisibility(View.VISIBLE);
                isDeleting = false;
                listAdapter.setDeleteMode(false);

                listAdapter.notifyDataSetChanged();
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mView.getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                Menu menu = popup.getMenu();
                inflater.inflate(R.menu.popup_menu, menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.m_item_1:
                                Intent intent = new Intent(mView.getContext(), AlarmSettingActivity.class);
                                intent.putExtra("isNew", true);
                                startActivity(intent);
                                break;
                            case R.id.m_item_2:
                                if(!isDeleting) {
                                    delete_done.setVisibility(View.VISIBLE);
                                    isDeleting = true;
                                    listAdapter.setDeleteMode(true);
                                    setting_btn.setVisibility(View.INVISIBLE);
                                    main_switch.setVisibility(View.INVISIBLE);
                                    fragmentListener.isDeleting(true);
                                }
                                listAdapter.notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

    }

    void main_sw_init(){
        main_switch = (Switch)mView.findViewById(R.id.main_switch);

        isServiceRunning = new ComputeClass().isLaunchingService(mView.getContext(), NotiService.class);
        main_switch.setChecked(isServiceRunning);
        listAdapter.setEnable(isServiceRunning);
        main_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent main_service = new Intent(mView.getContext(), NotiService.class);
                if(isChecked) {
                    SharedPreferences settings = mView.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("isRunning", true);
                    editor.commit();
                    if (Build.VERSION.SDK_INT >= 26) {
                        mView.getContext().startForegroundService(main_service);
                    }
                    else
                        mView.getContext().startService(main_service);
                }
                else {
                    //TODO make dialog
                    SharedPreferences settings = mView.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("isRunning", false);
                    editor.commit();
                    mView.getContext().stopService(main_service);
                }
                listAdapter.setEnable(isChecked);
                listAdapter.notifyDataSetChanged();
                isServiceRunning = isChecked;
            }
        });
//        if(isServiceRunning) {
//            Intent main_service = new Intent(mView.getContext(), NotiService.class);
//            mView.getContext().stopService(main_service);
//            mView.getContext().startService(main_service);
//        }
    }

    void init_list(){
        list = new ArrayList<>();
        listAdapter = new AlarmListAdapter(mView.getContext(), list);

        recyclerView = mView.findViewById(R.id.alarmlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        recyclerView.setAdapter(listAdapter);
    }

    public final void setList(){
        list.clear();

        AlarmDBHelper dbHelper = new AlarmDBHelper(mView.getContext(), "ALARM_TABLE", null, 1);
        for(int i = 0; i < dbHelper.getItemsCount(); i++){
            AlarmListItem item = new AlarmListItem();
            DBData dbData = dbHelper.getData(i);
            item.setTitle(dbData.getTitle());
            item.setToggleSw(dbData.isNowEnable()==1);
            item.setTime(dbData.getTimeToText());
            list.add(item);
        }
        listAdapter.notifyDataSetChanged();
        Log.i("mTAg", "aa");
    }

    public final void cancelDelete(){
        ImageButton setting_btn = mView.findViewById(R.id.setting_btn);
            Button delete_done = mView.findViewById(R.id.delete_done_btn);
            delete_done.setVisibility(View.GONE);
            setting_btn.setVisibility(View.VISIBLE);
            main_switch.setVisibility(View.VISIBLE);
            isDeleting = false;
            listAdapter.setDeleteMode(false);

            listAdapter.notifyDataSetChanged();
            fragmentListener.isDeleting(false);
    }
}
