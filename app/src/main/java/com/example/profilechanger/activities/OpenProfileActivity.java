package com.example.profilechanger.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profilechanger.R;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.database.MyDatabase;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

public class OpenProfileActivity extends BaseActivity {

    SeekBar ringtone_sb, media_sb, notification_sb, system_sb;
    SwitchMaterial vibrate_mSwitch, touchSound_mSwitch, dialPadSound_mSwitch;
    String profileTitle,
            ringerMode;
    String ringtoneLevel, mediaLevel, notificationLevel,
            systemLevel, vibrate, touchSound, dialPadSound;

    EditText title_mEt;
    Button saveProfile_mBtn, delete_mBtn;
    MyDatabase database;
    TextView ringType_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        database = new MyDatabase(this);
        String id = getIntent().getStringExtra(MyAnnotations.PROFILE_ID);
        boolean isNew = getIntent().getBooleanExtra(MyAnnotations.PROFILE_NEW, false);
        title_mEt = findViewById(R.id.title_mEt);
        CardView geoType_cv = findViewById(R.id.geoType_cv);

        ringtone_sb = findViewById(R.id.ringtone_sb);
        media_sb = findViewById(R.id.media_sb);
        notification_sb = findViewById(R.id.notification_sb);
        system_sb = findViewById(R.id.system_sb);

        vibrate_mSwitch = findViewById(R.id.vibrate_mSwitch);
        touchSound_mSwitch = findViewById(R.id.touchSound_mSwitch);
        dialPadSound_mSwitch = findViewById(R.id.dialPadSound_mSwitch);

        saveProfile_mBtn = findViewById(R.id.saveProfile_mBtn);
        delete_mBtn = findViewById(R.id.delete_mBtn);

        ringType_tv = findViewById(R.id.ringType_tv);

        seekBars();
        switches();


        geoType_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindow popupWindow = geoFencePopupMenu();
                popupWindow.showAsDropDown(geoType_cv,0,0);
            }
        });


        if (isNew) {
            // for new profile every thing will be default
            saveProfile_mBtn.setText(getResources().getString(R.string.save));
            title_mEt.setText(getResources().getString(R.string.un_titled));
            ringType_tv.setText(R.string.ringing);
            ringtone_sb.setProgress(50);
            media_sb.setProgress(50);
            notification_sb.setProgress(50);
            system_sb.setProgress(50);
            vibrate_mSwitch.setChecked(true);
            touchSound_mSwitch.setChecked(true);
            dialPadSound_mSwitch.setChecked(true);


        } else {
            // for existed profile every thing will be user defined
            saveProfile_mBtn.setText(getResources().getString(R.string.update));

            loadProfileFromDb(id);
            title_mEt.setText(profileTitle);

            if (ringerMode.matches(MyAnnotations.RINGER_MODE_NORMAL)) {
                ringType_tv.setText(R.string.ringing);

            } else if (ringerMode.matches(MyAnnotations.RINGER_MODE_SILENT)) {
                ringType_tv.setText(R.string.silent);

            } else if (ringerMode.matches(MyAnnotations.RINGER_MODE_VIBRATE)) {
                ringType_tv.setText(R.string.vibrate);
            }

            ringtone_sb.setProgress(Integer.parseInt(ringtoneLevel));
            media_sb.setProgress(Integer.parseInt(mediaLevel));
            notification_sb.setProgress(Integer.parseInt(notificationLevel));
            system_sb.setProgress(Integer.parseInt(systemLevel));

            vibrate_mSwitch.setChecked(vibrate.matches(MyAnnotations.ON));
            touchSound_mSwitch.setChecked(touchSound.matches(MyAnnotations.ON));
            dialPadSound_mSwitch.setChecked(dialPadSound.matches(MyAnnotations.ON));

        }

        String type = ringType_tv.getText().toString();

        if (type.matches("Ringing")) {
            ringerMode = MyAnnotations.RINGER_MODE_NORMAL;

        } else if (type.matches("Silent")) {
            ringerMode = MyAnnotations.RINGER_MODE_SILENT;

        } else {
            ringerMode = MyAnnotations.RINGER_MODE_VIBRATE;
        }

        if (vibrate_mSwitch.isChecked()) {
            vibrate = MyAnnotations.ON;

        } else {
            vibrate = MyAnnotations.OFF;

        }
        if (touchSound_mSwitch.isChecked()) {
            touchSound = MyAnnotations.ON;
        } else {
            touchSound = MyAnnotations.OFF;

        }

        if (dialPadSound_mSwitch.isChecked()) {
            dialPadSound = MyAnnotations.ON;


        } else {
            dialPadSound = MyAnnotations.OFF;

        }


        saveProfile_mBtn.setOnClickListener(v -> {
            profileTitle = title_mEt.getText().toString();
            vibrate_mSwitch.setChecked(!ringerMode.matches(MyAnnotations.RINGER_MODE_SILENT));
            if (isNew) {
                //check if silent then vibration must be off
                database.insertProfile(profileTitle, ringerMode, ringtoneLevel,
                        mediaLevel, notificationLevel, systemLevel, vibrate, touchSound,
                        dialPadSound);
            } else {
                database.updateProfile(id, profileTitle, ringerMode, ringtoneLevel,
                        mediaLevel, notificationLevel, systemLevel, vibrate, touchSound
                        , dialPadSound);
            }
            finish();
        });
        delete_mBtn.setOnClickListener(v -> {

            if (!isProfileEnabled1(id) || !isProfileEnabled2(id)) {
                database.deleteProfile(id);
                finish();

            } else {
                Toast.makeText(this, "This profile is set with profiler",
                        Toast.LENGTH_SHORT).show();
            }

        });


    }

    public void switches() {
        vibrate_mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    vibrate = MyAnnotations.ON;

                } else {
                    vibrate = MyAnnotations.OFF;

                }

            }
        });
        touchSound_mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    touchSound = MyAnnotations.ON;

                } else {
                    touchSound = MyAnnotations.OFF;

                }
            }
        });

        dialPadSound_mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dialPadSound = MyAnnotations.ON;

                } else {
                    dialPadSound = MyAnnotations.OFF;

                }
            }
        });

    }

    public void seekBars() {

        ringtone_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ringtoneLevel = String.valueOf(progress);
                if (progress == 0) {
                    ringerMode = MyAnnotations.RINGER_MODE_VIBRATE;
                    notification_sb.setProgress(0);
                    system_sb.setProgress(0);
                } else {
                    ringerMode = MyAnnotations.RINGER_MODE_NORMAL;
                    notification_sb.setProgress(40);
                    system_sb.setProgress(40);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        media_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaLevel = String.valueOf(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        notification_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                notificationLevel = String.valueOf(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        system_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                systemLevel = String.valueOf(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private PopupWindow geoFencePopupMenu() {
        PopupWindow geoFencePopupMenu = new PopupWindow(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.ring_types_layout, null);
        TextView ringing_tv = view.findViewById(R.id.ringing_tv);
        TextView silent_tv = view.findViewById(R.id.silent_tv);
        TextView vibrate_tv = view.findViewById(R.id.vibrate_tv);

        geoFencePopupMenu.setFocusable(true);
        geoFencePopupMenu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        geoFencePopupMenu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        geoFencePopupMenu.setContentView(view);
        geoFencePopupMenu.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            geoFencePopupMenu.setElevation(5.0f);
        }

        geoFencePopupMenu.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ringing_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ringtone_sb.getProgress() == 0) {
                    ringtone_sb.setProgress(50);
                }
                if (notification_sb.getProgress() == 0) {
                    notification_sb.setProgress(50);
                }
                if (system_sb.getProgress() == 0) {
                    system_sb.setProgress(50);
                }
                ringerMode = MyAnnotations.RINGER_MODE_NORMAL;
                ringType_tv.setText(R.string.ringing);
                geoFencePopupMenu.dismiss();
            }
        });
        silent_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringtone_sb.setProgress(0);
                notification_sb.setProgress(0);
                system_sb.setProgress(0);

                ringerMode = MyAnnotations.RINGER_MODE_SILENT;
                ringType_tv.setText(R.string.silent);
                geoFencePopupMenu.dismiss();
            }
        });
        vibrate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringtone_sb.setProgress(0);
                notification_sb.setProgress(0);
                system_sb.setProgress(0);

                ringerMode = MyAnnotations.RINGER_MODE_VIBRATE;
                ringType_tv.setText(R.string.vibrate);
                geoFencePopupMenu.dismiss();

            }
        });

        return geoFencePopupMenu;
    }


    public void loadProfileFromDb(String id) {
        Cursor cursor = database.retrieveProfile(id);
        while (cursor.moveToNext()) {

            profileTitle = cursor.getString(1);
            ringerMode = cursor.getString(2);
            ringtoneLevel = cursor.getString(3);
            mediaLevel = cursor.getString(4);
            notificationLevel = cursor.getString(5);
            systemLevel = cursor.getString(6);
            vibrate = cursor.getString(7);
            touchSound = cursor.getString(8);
            dialPadSound = cursor.getString(9);

        }
    }

    public boolean isProfileEnabled1(String id) {
        Cursor cursor = database.retrieveTimeTable();
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
        Cursor cursor = database.retrieveLocation();
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
}