package com.eungu.notice.Home_Menu;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eungu.notice.R;

import java.util.List;

public class AppSelectListAdapter extends RecyclerView.Adapter<AppSelectListAdapter.ViewHolder> {
    List<ResolveInfo> AppInfo;
    PackageManager pm;
    Context context;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        ImageView iv;
        ViewHolder(View itemView){
            super(itemView);
            tv = itemView.findViewById(R.id.app_item_tv);
            iv = itemView.findViewById(R.id.app_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AppSelectListAdapter.ViewHolder viewHolder, int i) {
        String appName = AppInfo.get(i).activityInfo.loadLabel(pm).toString();
        appName = appName.replace("\n", " ");
        viewHolder.tv.setText(appName);
        viewHolder.iv.setBackground(AppInfo.get(i).loadIcon(pm));
    }

    public AppSelectListAdapter(Context context, List<ResolveInfo> AppInfo) {
        this.context = context;
        pm = context.getPackageManager();
        this.AppInfo = AppInfo;
    }

    @Override
    public int getItemCount() {
        return AppInfo.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.app_select_item, viewGroup, false);
        AppSelectListAdapter.ViewHolder vh = new AppSelectListAdapter.ViewHolder(view);
        return vh;
    }
}
