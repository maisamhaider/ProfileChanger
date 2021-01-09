package com.example.profilechanger.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profilechanger.R;
import com.example.profilechanger.activities.LocationBaseEditActivity;
import com.example.profilechanger.activities.TimeBaseProfilerEditActivity;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.interfaces.ClickListener;
import com.example.profilechanger.models.ProfilerModel;
import com.example.profilechanger.sharedpreferences.MyPreferences;
import com.example.profilechanger.utils.AlarmClass;
import com.example.profilechanger.utils.PopUpWindow;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class ProfilerAdapter extends RecyclerView.Adapter<ProfilerAdapter.ProfilerViewHolder>
        implements ClickListener {

    private Context context;
    private ArrayList<ProfilerModel> modelArrayList;
    private boolean isTimeBased;
    private PopUpWindow popUpWindow;
    private MyDatabase database;
    private ProfilerModel profilerModel;
    private String id;
    private MyPreferences preferences;

    public ProfilerAdapter(Context context, ArrayList<ProfilerModel> modelArrayList,
                           boolean isTimeBased, MyDatabase database) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.isTimeBased = isTimeBased;
        this.database = database;
        popUpWindow = new PopUpWindow(context, this);
        preferences = new MyPreferences(context);
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
        String title = modelArrayList.get(position).getName();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (preferences.getBoolean(MyAnnotations.IS_LIGHT_THEME, false)) {
                holder.profilerTitle_tv.setBackground(ContextCompat
                        .getDrawable(context, R.drawable.ic_button_1_light));
            } else {
                holder.profilerTitle_tv.setBackground(ContextCompat
                        .getDrawable(context, R.drawable.ic_button_2_dark));
            }
        } else {

            switch (context.getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    holder.profilerTitle_tv.setBackground(ContextCompat
                            .getDrawable(context, R.drawable.ic_button_2_dark));
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    holder.profilerTitle_tv.setBackground(ContextCompat
                            .getDrawable(context, R.drawable.ic_button_1_light));
                    break;


            }
        }

        holder.profilerTitle_tv.setText(title);
        holder.itemView.setOnLongClickListener(v -> {
            PopupWindow popupWindow;
            profilerModel = modelArrayList.get(position);
            id = modelArrayList.get(position).getId();
            if (isTimeBased) {
                popupWindow = popUpWindow.popupWindowUpDel(4);

            } else {
                popupWindow = popUpWindow.popupWindowUpDel(2);
            }
            popupWindow.showAsDropDown(v, Gravity.END, 0);


            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            if (isTimeBased) {
                id = modelArrayList.get(position).getId();

                editIntent(MyAnnotations.TIME_PROFILER_ID, id, MyAnnotations.IS_UPDATE,
                        true, true);
            } else {
//                    editIntent(MyAnnotations.LOCATION_PROFILER_ID, id, MyAnnotations.IS_UPDATE,
//                            true, false);
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
            AlarmClass alarmClass = new AlarmClass(context);
            alarmClass.deleteRepeatAlarm(Integer.parseInt(id) + 1000);
            alarmClass.deleteRepeatAlarm(Integer.parseInt(id) + 10000);

        } else {
            deleteData(id);
        }
        int s1 = modelArrayList.size();
        modelArrayList.remove(profilerModel);
        int s2 = modelArrayList.size();
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

    public void deleteData(String id) {
        GeofencingClient geofencingClient = LocationServices.getGeofencingClient(context);
        List<String> idList = new ArrayList<>();
        idList.add(id);
        database.deleteLocation(id);

        geofencingClient.removeGeofences(idList).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    class ProfilerViewHolder extends RecyclerView.ViewHolder {
        TextView profilerTitle_tv;

        public ProfilerViewHolder(@NonNull View itemView) {
            super(itemView);
            profilerTitle_tv = itemView.findViewById(R.id.profilerTitle_tv);
        }
    }
}
