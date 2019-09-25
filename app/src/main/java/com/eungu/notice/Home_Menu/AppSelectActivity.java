package com.eungu.notice.Home_Menu;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.eungu.notice.R;

import java.util.List;

public class AppSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_select_list);

        final PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> AppInfo = pm.queryIntentActivities(intent, 0);

        AppSelectListAdapter adapter = new AppSelectListAdapter(getApplicationContext(), AppInfo);
        RecyclerView recyclerView = findViewById(R.id.app_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new AppSelectListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent result = new Intent();
                result.putExtra("appName", AppInfo.get(pos).activityInfo.loadLabel(pm).toString());
                result.putExtra("appPackage", AppInfo.get(pos).activityInfo.packageName);
                setResult(RESULT_OK, result);
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
