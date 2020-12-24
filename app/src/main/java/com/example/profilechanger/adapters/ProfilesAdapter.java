package com.example.profilechanger.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profilechanger.R;
import com.example.profilechanger.activities.OpenProfileActivity;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.interfaces.ClickListener;
import com.example.profilechanger.interfaces.SendDataWithKey;
import com.example.profilechanger.models.ProfilesModel;
import com.example.profilechanger.utils.PopUpWindow;

import java.util.ArrayList;

public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ProfileItemHolder>
        implements ClickListener {

    private final Context context;
    private final ArrayList<ProfilesModel> profilesModelArrayList;
    private final PopUpWindow popUpWindow;

    private final MyDatabase myDatabase;
    private ProfilesModel profilesModel;
    private final boolean isProfilerNeed;
    private SendDataWithKey sendDataWithKey;
    private String profile;
    String pos;

    public ProfilesAdapter(Context context,
                           ArrayList<ProfilesModel> profilesModelArrayList,
                           MyDatabase myDatabase, boolean isProfilerNeed) {
        this.context = context;
        this.profilesModelArrayList = profilesModelArrayList;
        this.myDatabase = myDatabase;
        this.isProfilerNeed = isProfilerNeed;
        popUpWindow = new PopUpWindow(context, this);
    }

    public void setSendDataWithKey(SendDataWithKey sendDataWithKey, String profile) {
        this.sendDataWithKey = sendDataWithKey;
        this.profile = profile;
    }

    @NonNull
    @Override
    public ProfileItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_rv_items_layout, parent, false);
        return new ProfileItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileItemHolder holder, int position) {
        profilesModel = profilesModelArrayList.get(position);
        String title = profilesModelArrayList.get(position).getPROFILE_TITLE();
        String    id = profilesModelArrayList.get(position).getId();
        holder.profileName_tv.setText(title);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isProfilerNeed) {
                    pos  = profilesModelArrayList.get(position).getId();
                    PopupWindow popupWindow = popUpWindow.popupWindowUpDel();
                    popupWindow.showAsDropDown(v, Gravity.END, 0);
                }
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProfilerNeed) {
                    pos  = profilesModelArrayList.get(position).getId();

                    sendDataWithKey.data(profile,pos,title);
                } else {
                    pos  = profilesModelArrayList.get(position).getId();
                    editIntent(pos);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return profilesModelArrayList.size();
    }

    @Override
    public void click(String button) {
        if (button.matches(MyAnnotations.edit)) {
            editIntent(pos);
        } else if (button.matches(MyAnnotations.delete)) {

            if (!isProfileEnabled1(pos) || !isProfileEnabled2(pos)) {
                myDatabase.deleteProfile(pos);
                profilesModelArrayList.remove(profilesModel);
                notifyDataSetChanged();

            } else {
                Toast.makeText(context, "This profile is set with profiler",
                        Toast.LENGTH_SHORT).show();
            }
        }


    }

    public boolean isProfileEnabled1(String id) {
        Cursor cursor = myDatabase.retrieveTimeTable();
        if (cursor.getCount() == 0) {
            return false;
        }
        while (cursor.moveToNext()) {
            String i = cursor.getString(10);
            String i1 = cursor.getString(11);
            if (id.matches(i) || id.matches(i1)) {
                return true;
            }

        }
        return false;
    }

    public boolean isProfileEnabled2(String id) {
        Cursor cursor = myDatabase.retrieveLocation();
        if (cursor.getCount() == 0) {
            return false;
        }
        while (cursor.moveToNext()) {
            String i = cursor.getString(9);
            String i1 = cursor.getString(10);
            if (id.matches(i) || id.matches(i1)) {
                return true;
            }
        }
        return false;
    }

    public void editIntent(String id) {
        Intent intent = new Intent(context, OpenProfileActivity.class);
        intent.putExtra(MyAnnotations.PROFILE_ID, id);
        intent.putExtra(MyAnnotations.PROFILE_NEW, false);
        context.startActivity(intent);
    }

    class ProfileItemHolder extends RecyclerView.ViewHolder {
        TextView profileName_tv;

        public ProfileItemHolder(@NonNull View itemView) {
            super(itemView);
            profileName_tv = itemView.findViewById(R.id.profileName_tv);

        }
    }
}
