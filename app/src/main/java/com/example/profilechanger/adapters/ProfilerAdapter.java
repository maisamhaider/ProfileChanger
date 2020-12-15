package com.example.profilechanger.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profilechanger.R;
import com.example.profilechanger.activities.LocationBaseEditActivity;
import com.example.profilechanger.activities.LocationBaseProfilerActivity;
import com.example.profilechanger.activities.TimeBaseProfilerEditActivity;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.interfaces.ClickListener;
import com.example.profilechanger.models.ProfilerModel;
import com.example.profilechanger.utils.PopUpWindow;

import java.util.ArrayList;

public class ProfilerAdapter extends RecyclerView.Adapter<ProfilerAdapter.ProfilerViewHolder>
        implements ClickListener {

    private Context context;
    private ArrayList<ProfilerModel> modelArrayList;
    private boolean isTimeBased;
    private PopUpWindow popUpWindow;
    private MyDatabase database;
    private ProfilerModel profilerModel;
    private String id;

    public ProfilerAdapter(Context context, ArrayList<ProfilerModel> modelArrayList,
                           boolean isTimeBased, MyDatabase database) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.isTimeBased = isTimeBased;
        this.database = database;
        popUpWindow = new PopUpWindow(context, this);
    }

    @NonNull
    @Override
    public ProfilerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profiler_item_layout,
                parent, false);

        return new ProfilerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilerViewHolder holder, int position) {
        profilerModel = modelArrayList.get(position);
        id = modelArrayList.get(position).getId();
        String title = modelArrayList.get(position).getId();

        holder.profilerTitle_tv.setText(title);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                PopupWindow popupWindow = popUpWindow.popupWindowUpDel();
                popupWindow.showAsDropDown(v, Gravity.END, 0);
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimeBased) {
                    editIntent(MyAnnotations.TIME_PROFILER_ID, id, MyAnnotations.IS_UPDATE,
                            true, true);
                } else {
                    editIntent(MyAnnotations.LOCATION_PROFILER_ID, id, MyAnnotations.IS_UPDATE,
                            true, false);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    @Override
    public void click(String button) {
        if (button.matches(MyAnnotations.edit)) {

            if (isTimeBased) {
                editIntent(MyAnnotations.TIME_PROFILER_ID, id, MyAnnotations.IS_UPDATE,
                        true, true);


            } else {
                editIntent(MyAnnotations.LOCATION_PROFILER_ID, id, MyAnnotations.IS_UPDATE,
                        true, false);

            }

        } else if (button.matches(MyAnnotations.delete)) {

            deleteFun(id);
        }
    }

    public void deleteFun(String id) {

        if (isTimeBased) {
            database.deleteTimeTable(id);

        } else {
            database.deleteLocation(id);
        }
        modelArrayList.remove(profilerModel);
        notifyDataSetChanged();


    }

    public void editIntent(String key, String id, String isUpdate, boolean isUpdateValue,
                           boolean isTimeBased) {
        Intent intent;
        if (isTimeBased) {
            intent = new Intent(context, TimeBaseProfilerEditActivity.class);

        } else {

            intent = new Intent(context, LocationBaseEditActivity.class);

        }

        intent.putExtra(key, id);
        intent.putExtra(isUpdate, isUpdateValue);
        context.startActivity(intent);
    }

    class ProfilerViewHolder extends RecyclerView.ViewHolder {
        TextView profilerTitle_tv;

        public ProfilerViewHolder(@NonNull View itemView) {
            super(itemView);
            profilerTitle_tv = itemView.findViewById(R.id.profilerTitle_tv);
        }
    }
}
