package com.example.profilechanger.fragments;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.profilechanger.R;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.interfaces.ClickListener;
import com.example.profilechanger.utils.BooleansUtils;
import com.example.profilechanger.utils.SoundProfileActions;
import com.example.profilechanger.utils.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static android.media.AudioManager.STREAM_RING;


public class
BottomSheetFragmentMain extends BottomSheetDialogFragment {
    NotificationManager mNotificationManager;
    private SoundProfileActions allActionsUtils;
    private BooleansUtils booleansUtils;
    private SoundProfileActions actions;

    private Utils utils;
    ClickListener clickListener;
    int i = 0;

    public BottomSheetFragmentMain(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public BottomSheetFragmentMain() {
    }

    public static BottomSheetFragmentMain newInstance() {

        return new BottomSheetFragmentMain();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_main, container, false);
        utils = new Utils(getActivity());
        actions = new SoundProfileActions(getActivity());

//        AdView adView =view.findViewById(R.id.adView);
//        adView(adView);
        allActionsUtils = new SoundProfileActions(getActivity());
        booleansUtils = new BooleansUtils(getActivity());
        mNotificationManager = (NotificationManager)
                getActivity().getSystemService(Context.NOTIFICATION_SERVICE);


        SeekBar ring = view.findViewById(R.id.ring);
        SeekBar music = view.findViewById(R.id.music);
        SeekBar notification = view.findViewById(R.id.notification);
        SeekBar system = view.findViewById(R.id.system);
        AudioManager audioManager = (AudioManager) getActivity()
                .getSystemService(Context.AUDIO_SERVICE);

        initControls(ring, STREAM_RING);
        initControls(music, AudioManager.STREAM_MUSIC);
        if (audioManager.getStreamVolume(STREAM_RING)==0)
        {
            system.setEnabled(false);
            notification.setEnabled(false);
        }
        initControls(system, AudioManager.STREAM_SYSTEM);
        initControls(notification, AudioManager.STREAM_NOTIFICATION);

        view.findViewById(R.id.silent_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allActionsUtils.setRingerMode(MyAnnotations.RINGER_MODE_SILENT);
            }
        });

        return view;
    }

    public void doNoDisturbPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Do no disturb").setMessage(MyAnnotations.DO_NOT_DISTURB_MESSAGE)
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
                                // Check if the notification policy access has been granted for the app.
                                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                                startActivity(intent);
                            }

                        }
                    }
                }).setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void goToWifiSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Go to wifi settings").setMessage(MyAnnotations.GO_WIFI_SETTINGS_MESSAGE)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        clickListener.click(MyAnnotations.ON);

                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //TODO
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    private void initControls(SeekBar seek, final int stream) {
        AudioManager audioManager = (AudioManager) getActivity()
                .getSystemService(Context.AUDIO_SERVICE);
        seek.setMax(audioManager.getStreamMaxVolume(stream));
        seek.setProgress(audioManager.getStreamVolume(stream));
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(stream, progress, AudioManager.FLAG_SHOW_UI);
            }

            public void onStartTrackingTouch(SeekBar bar) {
            }

            public void onStopTrackingTouch(SeekBar bar) {
            }
        });
    }
}