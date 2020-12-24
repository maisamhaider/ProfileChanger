package com.example.profilechanger.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
    private MediaPlayer mediaPlayer;
    private final SendDataWithKey setPath;

    private final MyPreferences sharedPreferences;

    public NotificationSoundsAdapter(Context context, ArrayList<String> names,
                                     ArrayList<String> paths, SendDataWithKey setPath) {
        this.context = context;
        this.names = names;
        this.paths = paths;
        this.setPath = setPath;
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
        holder.notification_sound_holder_Tv.setText(names.get(position));


        holder.notification_sound_select_cb.setChecked(paths.get(position)
                .matches(sharedPreferences.getString(MyAnnotations.NOTIFICATION_SOUND_PATH,
                        paths.get(0))));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
                holder.notification_sound_select_cb.setChecked(true);
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
        TextView notification_sound_holder_Tv, hidden_notification_sound_holder_Tv;
        LinearLayout notificationItemLL;

        public NotificationSoundViewHolder(@NonNull View itemView) {
            super(itemView);
            notification_sound_select_cb = itemView.findViewById(R.id.notification_sound_select_cb);
            notification_sound_holder_Tv = itemView.findViewById(R.id.notification_sound_holder_Tv);
            hidden_notification_sound_holder_Tv =
                    itemView.findViewById(R.id.hidden_notification_sound_holder_Tv);
            notificationItemLL = itemView.findViewById(R.id.notificationItemLL);
        }
    }
}
