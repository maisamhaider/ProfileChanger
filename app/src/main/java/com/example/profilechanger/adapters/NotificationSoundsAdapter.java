package com.example.profilechanger.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.profilechanger.R;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.interfaces.SendDataWithKey;
import com.example.profilechanger.sharedpreferences.MyPreferences;

import java.util.ArrayList;

public class NotificationSoundsAdapter extends
        RecyclerView.Adapter<NotificationSoundsAdapter.NotificationSoundViewHolder> {

    private final Context context;
    private final ArrayList<String> names;
    ArrayList<String> paths;
    ArrayList<String> selectNames;
    private MediaPlayer mediaPlayer;
    private final SendDataWithKey setPath;

    private final MyPreferences sharedPreferences;

    public NotificationSoundsAdapter(Context context, ArrayList<String> names,
                                     ArrayList<String> paths, SendDataWithKey setPath) {
        this.context = context;
        this.names = names;
        this.paths = paths;
        this.setPath = setPath;
        selectNames = new ArrayList<>();
        sharedPreferences = new MyPreferences(context);

    }

    @NonNull
    @Override
    public NotificationSoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_sounds_item_layout, parent, false);

        return new NotificationSoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationSoundViewHolder holder,
                                 final int position) {

        holder.notification_sound_select_cb.setText(names.get(position));
        holder.notification_sound_select_cb.setChecked(paths.get(position)
                .matches(sharedPreferences.getString(MyAnnotations.NOTIFICATION_SOUND_PATH,
                        paths.get(0))));


 /*       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer = MediaPlayer.create(context, Uri.parse(paths.get(position)));
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    } else
                        mediaPlayer.start();
                }
                selectNames.clear();
                selectNames.add(names.get(position));
                for (int i = 0; i < names.size(); i++) {
                    holder.notification_sound_select_cb.setChecked
                            (names.get(i).matches(selectNames.get(0)));
                }
                setPath.data("", paths.get(position), names.get(position));
                notifyDataSetChanged();

            }
        });*/
        holder.notification_sound_select_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = MediaPlayer.create(context, Uri.parse(paths.get(position)));
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    } else
                        mediaPlayer.start();
                }
                selectNames.clear();
                selectNames.add(names.get(position));
                for (int i = 0; i < names.size(); i++) {
                    holder.notification_sound_select_cb.setChecked
                            (names.get(i).matches(selectNames.get(0)));
                }
                setPath.data("", paths.get(position), names.get(position));
            notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return names.size();
    }

    public class NotificationSoundViewHolder extends RecyclerView.ViewHolder {

        CheckBox notification_sound_select_cb;
        LinearLayout notificationItemLL;

        public NotificationSoundViewHolder(@NonNull View itemView) {
            super(itemView);
            notification_sound_select_cb = itemView.findViewById(R.id.notification_sound_select_cb);
            notificationItemLL = itemView.findViewById(R.id.notificationItemLL);
        }
    }
}
