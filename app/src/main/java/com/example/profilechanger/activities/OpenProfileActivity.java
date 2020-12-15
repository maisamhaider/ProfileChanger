package com.example.profilechanger.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.profilechanger.R;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.database.MyDatabase;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

public class OpenProfileActivity extends AppCompatActivity {

    SeekBar ringtone_sb, media_sb, notification_sb, system_sb;
    SwitchMaterial vibrate_mSwitch, touchSound_mSwitch, lockScreen_mSwitch, dialPadSound_mSwitch;
    String profileTitle,
            ringerMode;
    String ringtoneLevel, mediaLevel, notificationLevel,
            systemLevel, vibrate, touchSound,
            lockScreenSound, dialPadSound;
    EditText title_mEt;
    Button saveProfile_mBtn, delete_mBtn;
    Spinner spinner;
    MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_profile);
        database = new MyDatabase(this);
        String id = getIntent().getStringExtra(MyAnnotations.PROFILE_ID);
        boolean isNew = getIntent().getBooleanExtra(MyAnnotations.PROFILE_NEW, false);
        title_mEt = findViewById(R.id.title_mEt);
        spinner = findViewById(R.id.ringerMode_spinner);

        ringtone_sb = findViewById(R.id.ringtone_sb);
        media_sb = findViewById(R.id.media_sb);
        notification_sb = findViewById(R.id.notification_sb);
        system_sb = findViewById(R.id.system_sb);

        vibrate_mSwitch = findViewById(R.id.vibrate_mSwitch);
        touchSound_mSwitch = findViewById(R.id.touchSound_mSwitch);
        lockScreen_mSwitch = findViewById(R.id.lockScreen_mSwitch);
        dialPadSound_mSwitch = findViewById(R.id.dialPadSound_mSwitch);

        saveProfile_mBtn = findViewById(R.id.saveProfile_mBtn);
        delete_mBtn = findViewById(R.id.delete_mBtn);

        seekBars();
        switches();

        //spinner
        ArrayList<String> options = new ArrayList<>();
        options.add(getResources().getString(R.string.ringing));
        options.add(getResources().getString(R.string.silent));
        options.add(getResources().getString(R.string.vibrate));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (ringtone_sb.getProgress() == 0) {
                        ringtone_sb.setProgress(50);
                    }
                    if (notification_sb.getProgress() == 0) {
                        notification_sb.setProgress(50);
                    }
                    if (system_sb.getProgress() == 0) {
                        system_sb.setProgress(50);
                    }


                } else if (position == 1) {

                    ringtone_sb.setProgress(0);
                    notification_sb.setProgress(0);
                    system_sb.setProgress(0);

                } else {

                    ringtone_sb.setProgress(0);
                    notification_sb.setProgress(0);
                    system_sb.setProgress(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (isNew) {
            // for new profile every thing will be default
            saveProfile_mBtn.setText(getResources().getString(R.string.save));
            title_mEt.setText(getResources().getString(R.string.un_titled));
            spinner.setSelection(0);

            ringtone_sb.setProgress(50);
            media_sb.setProgress(50);
            notification_sb.setProgress(50);
            system_sb.setProgress(50);
            vibrate_mSwitch.setChecked(true);
            touchSound_mSwitch.setChecked(true);
            lockScreen_mSwitch.setChecked(true);
            dialPadSound_mSwitch.setChecked(true);


        } else {
            // for existed profile every thing will be user defined
            saveProfile_mBtn.setText(getResources().getString(R.string.update));

            loadProfileFromDb(id);
            title_mEt.setText(profileTitle);

            if (ringerMode.matches(MyAnnotations.RINGER_MODE_NORMAL)) {
                spinner.setSelection(0);

            } else if (ringerMode.matches(MyAnnotations.RINGER_MODE_SILENT)) {
                spinner.setSelection(1);

            } else if (ringerMode.matches(MyAnnotations.RINGER_MODE_VIBRATE)) {
                spinner.setSelection(2);
            }

            ringtone_sb.setProgress(Integer.parseInt(ringtoneLevel));
            media_sb.setProgress(Integer.parseInt(mediaLevel));
            notification_sb.setProgress(Integer.parseInt(notificationLevel));
            system_sb.setProgress(Integer.parseInt(systemLevel));

            vibrate_mSwitch.setChecked(vibrate.matches(MyAnnotations.ON));
            touchSound_mSwitch.setChecked(touchSound.matches(MyAnnotations.ON));
            lockScreen_mSwitch.setChecked(lockScreenSound.matches(MyAnnotations.ON));
            dialPadSound_mSwitch.setChecked(dialPadSound.matches(MyAnnotations.ON));

        }

        int position = spinner.getSelectedItemPosition();

        if (position == 0) {
            ringerMode = MyAnnotations.RINGER_MODE_NORMAL;

        } else if (position == 1) {
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
        if (lockScreen_mSwitch.isChecked()) {
            lockScreenSound = MyAnnotations.ON;

        } else {
            lockScreenSound = MyAnnotations.OFF;

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
                        lockScreenSound, dialPadSound);
            } else {
                database.updateProfile(id, profileTitle, ringerMode, ringtoneLevel,
                        mediaLevel, notificationLevel, systemLevel, vibrate, touchSound,
                        lockScreenSound, dialPadSound);
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
        lockScreen_mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    lockScreenSound = MyAnnotations.ON;

                } else {
                    lockScreenSound = MyAnnotations.OFF;

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
            lockScreenSound = cursor.getString(9);
            dialPadSound = cursor.getString(10);

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