package com.example.profilechanger.adapters;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profilechanger.R;
import com.example.profilechanger.activities.MainActivity;
import com.example.profilechanger.activities.OpenProfileActivity;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.interfaces.ClickListener;
import com.example.profilechanger.interfaces.SendDataWithKey;
import com.example.profilechanger.models.ProfilesModel;
import com.example.profilechanger.permissions.Permissions;
import com.example.profilechanger.sharedpreferences.MyPreferences;
import com.example.profilechanger.utils.PopUpWindow;
import com.example.profilechanger.utils.SoundProfileActions;

import java.util.ArrayList;

public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ProfileItemHolder>
        implements ClickListener {

    private final Context context;
    private final ArrayList<ProfilesModel> profilesModelArrayList;
    private final PopUpWindow popUpWindow;
    Permissions permissions;
    private final MyDatabase myDatabase;
    private ProfilesModel profilesModel;
    private final boolean isProfilerNeed;
    private SendDataWithKey sendDataWithKey;
    private String profile;
    String pos;
    MyPreferences p;
    boolean dialog = false;

    public ProfilesAdapter(Context context,
                           ArrayList<ProfilesModel> profilesModelArrayList,
                           MyDatabase myDatabase, boolean isProfilerNeed, boolean dialog) {
        this.context = context;
        this.profilesModelArrayList = profilesModelArrayList;
        this.myDatabase = myDatabase;
        this.isProfilerNeed = isProfilerNeed;
        popUpWindow = new PopUpWindow(context, this);
        p = new MyPreferences(context);
        this.dialog = dialog;
        permissions = new Permissions(context);
    }

    public void setSendDataWithKey(SendDataWithKey sendDataWithKey, String profile) {
        this.sendDataWithKey = sendDataWithKey;
        this.profile = profile;
    }

    @NonNull
    @Override
    public ProfileItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (dialog) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_rv_items_2_layout, parent, false);

        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_rv_items_1_layout, parent, false);
        }
        return new ProfileItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileItemHolder holder, int position) {
        profilesModel = profilesModelArrayList.get(position);
        String title = profilesModelArrayList.get(position).getPROFILE_TITLE();
        String id = profilesModelArrayList.get(position).getId();
        holder.profileName_tv.setText(title);
        if (!dialog) {
            if (p.getBoolean(MyAnnotations.IS_LIGHT_THEME, false)) {
                holder.cv.setCardBackgroundColor(ContextCompat.getColor(context,
                        R.color.white));
                holder.profileName_tv.setTextColor(ContextCompat.getColor(context,
                        R.color.colorTextOne));
            } else {
                holder.cv.setCardBackgroundColor(ContextCompat.getColor(context,
                        R.color.colorPrimaryVariantLight));
                holder.profileName_tv.setTextColor(ContextCompat.getColor(context,
                        R.color.white));
            }
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isProfilerNeed) {
                    pos = profilesModelArrayList.get(position).getId();
                    PopupWindow popupWindow = popUpWindow.popupWindowUpDel(5);
                    popupWindow.showAsDropDown(v, Gravity.END, 0);
                }
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProfilerNeed) {
                    pos = profilesModelArrayList.get(position).getId();

                    sendDataWithKey.data(profile, pos, title);
                } else {
                    pos = profilesModelArrayList.get(position).getId();
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
        if (button.matches(MyAnnotations.set)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(context)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getString(R.string.permission))
                            .setMessage(context.getString(R.string.write_settings_alert_text))
                            .setPositiveButton(context.getString(R.string.allow),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            permissions.openAndroidPermissionsMenu((MainActivity) context);
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton(R.string.decline,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    NotificationManager mNotificationManager = (NotificationManager)
                            context.getSystemService(Context.NOTIFICATION_SERVICE);

                    if (mNotificationManager.isNotificationPolicyAccessGranted()) {
                        startProfile(pos);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(context.getString(R.string.permission))
                                .setMessage(context.getString(R.string.do_not_disturb_permission_text))
                                .setPositiveButton(context.getString(R.string.allow),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                permissions.doNoDisturbPermissionDialog();
                                                dialog.dismiss();
                                            }
                                        }).setNegativeButton(R.string.decline,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            } else {
                startProfile(pos);

            }
        } else if (button.matches(MyAnnotations.edit)) {
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

    public void startProfile(String id) {

        MyDatabase database = new MyDatabase(context);
        SoundProfileActions actions = new SoundProfileActions(context);

        Cursor startCursor = database.retrieveProfile(id);
        while (startCursor.moveToNext()) {
            String ringerMode = startCursor.getString(2);
            int ringerLevel = Integer.parseInt(startCursor.getString(3));
            int mediaLevel = Integer.parseInt(startCursor.getString(4));
            int notificationLevel = Integer.parseInt(startCursor.getString(5));
            int systemLevel = Integer.parseInt(startCursor.getString(6));
            String vibrate = startCursor.getString(7);
            String touchSound = startCursor.getString(8);
            String dialPedSound = startCursor.getString(9);

            actions.setRingerMode(ringerMode);
            actions.setVolume(AudioManager.STREAM_RING, ringerLevel);
            actions.setVolume(AudioManager.STREAM_MUSIC, mediaLevel);
            actions.setVolume(AudioManager.STREAM_NOTIFICATION, notificationLevel);
            actions.setVolume(AudioManager.STREAM_SYSTEM, systemLevel);

            if (ringerMode.matches(MyAnnotations.RINGER_MODE_SILENT)) {
                actions.setVibration(0);

            } else {
                if (vibrate.matches(MyAnnotations.ON)) {
                    actions.setVibration(1);
                } else {
                    actions.setVibration(0);
                }
            }
            if (touchSound.matches(MyAnnotations.ON)) {
                actions.setTouchSound(1);
            } else {
                actions.setTouchSound(0);
            }
            if (dialPedSound.matches(MyAnnotations.ON)) {
                actions.setDialingPadTone(1);
            } else {
                actions.setDialingPadTone(0);
            }
        }

    }

    class ProfileItemHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView profileName_tv;

        public ProfileItemHolder(@NonNull View itemView) {
            super(itemView);
            profileName_tv = itemView.findViewById(R.id.profileName_tv);
            if (!dialog) {
                cv = itemView.findViewById(R.id.cv);
            }

        }
    }

}
