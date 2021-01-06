package com.example.profilechanger.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profilechanger.R;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.interfaces.LongClickListener;
import com.example.profilechanger.interfaces.SelectAll;
import com.example.profilechanger.models.HistoryModel;
import com.example.profilechanger.utils.TimeUtil;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;


import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ItemHolder>
        implements SelectAll {

    Context context;
    ArrayList<HistoryModel> list;
    MyDatabase myDatabase;
    private ArrayList<String> sendingList;
    private SelectAll selectAll;
    private boolean isDone;
    boolean AllSelected = false;
    ItemHolder holder;
    boolean isLongClicked = false;
    boolean isDown = false;
    TimeUtil timeUtil;
    LongClickListener longClickListener;

    public HistoryAdapter(Context context,
                          ArrayList<HistoryModel> list,
                          MyDatabase myDatabase,
                          SelectAll selectAll,
                          LongClickListener longClickListener,
                          boolean isDone) {
        this.context = context;
        this.list = list;
        this.myDatabase = myDatabase;
        this.selectAll = selectAll;
        this.longClickListener = longClickListener;
        this.isDone = isDone;
        sendingList = new ArrayList<>();
        if (isDone) {
            for (HistoryModel h : list) {
                sendingList.add(String.valueOf(h.getId()));
            }
        }
        timeUtil = new TimeUtil(context);
    }

    public ArrayList<String> getSendingList() {
        return sendingList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_items_layout, parent, false);
        return new ItemHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        this.holder = holder;
        myDatabase = new MyDatabase(context);
        int id = list.get(position).getId();
        String title = list.get(position).getTitle();
        String latitude = list.get(position).getLatitude();
        String longitude = list.get(position).getLongitude();
        String circleSize = list.get(position).getCircleSize();
        String state = list.get(position).getState();
        long expirationTime = list.get(position).getExpirationTime();
        String expirationEnd = list.get(position).getExpirationEnd();
        String geofenceType = list.get(position).getGeofenceType();
        String dateNow = list.get(position).getDateNow();


        holder.title_tv.setText(title);
        holder.latitude_tv.setText(latitude);
        holder.longitude_tv.setText(longitude);
        holder.expirationTime_tv.setText(timeUtil.getFormattedDateAndTime(Long.parseLong(expirationEnd)));
        if (MILLISECONDS.toHours(expirationTime) == 1) {

            holder.expirationDuration_tv.setText(MILLISECONDS.
                    toHours(expirationTime) + "hour");
        } else {
            holder.expirationDuration_tv.setText(MILLISECONDS.
                    toHours(expirationTime) + "hours");
        }
        holder.geoFenceType_tv.setText(geofenceType);
        holder.circleSize_tv.setText(circleSize + " (m)");
        holder.state_tv.setText(state);

        if (isDone) {
            isLongClicked = true;
            longClickListener.onLongClicked(true);
        }
        if (isLongClicked) {
            if (sendingList.contains(String.valueOf(id))) {
                imageShow();
                holder.imageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                        R.drawable.ic_launcher_background, null));
            } else {
                imageShow();
                holder.imageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                        R.drawable.ic_launcher_background, null));
            }

        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                isLongClicked = true;
                longClickListener.onLongClicked(true);
                for (HistoryModel h : list) {
                    holder.imageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                            R.drawable.ic_launcher_background, null));
                    holder.imageView.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();

                }
                return true;
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLongClicked) {
                    String pos = String.valueOf(id);
                    if (sendingList.contains(pos)) {
                        sendingList.remove(pos);
                        selectAll.selected(false);
                        holder.imageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                                R.drawable.ic_launcher_background, null));
                    } else {

                        sendingList.add(pos);
                        selectAll.selected(list.size() == sendingList.size());

                        holder.imageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                                R.drawable.ic_launcher_background, null));
                    }
                }
            }
        });
        holder.swiping_ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isDown) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN /*&&
                            event.getAction() == MotionEvent.ACTION_HOVER_MOVE*/) {

                        isDown = false;
                        holder.clLayout_two.setVisibility(View.GONE);
                    }
                } else {
                    if (event.getAction() == MotionEvent.ACTION_DOWN /*&&
                            event.getAction() == MotionEvent.ACTION_HOVER_MOVE*/) {
                        isDown = true;

                        holder.clLayout_two.setVisibility(View.VISIBLE);
                    }

                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public SelectAll getSelectAll() {
        return this;
    }

    @Override
    public void selected(boolean selectedAll) {
        if (selectedAll) {
            selectAll();
        } else {
            deSelectAll();
        }
    }

    public void selectAll() {
        isLongClicked = true;
        longClickListener.onLongClicked(true);
        if (!list.isEmpty()) {
            sendingList.clear();
        }
        for (HistoryModel h : list) {
            sendingList.add(String.valueOf(h.getId()));
            //setCheck box checked
            holder.imageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.ic_launcher_background, null));
            imageShow();
            notifyDataSetChanged();

        }
    }

    public void deSelectAll() {
        if (!sendingList.isEmpty()) {
            sendingList.clear();
            //setCheck box Unchecked;
            holder.imageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.ic_launcher_background, null));
            imageShow();
            notifyDataSetChanged();
        }

    }

    public void changeImage(boolean selectOne) {
        if (selectOne) {
            for (HistoryModel h : list) {
                sendingList.add(String.valueOf(h.getId()));
                //setCheck box checked
                holder.imageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                        R.drawable.ic_launcher_background, null));
            }
        } else {
            for (HistoryModel h : list) {
                sendingList.add(String.valueOf(h.getId()));
                //setCheck box checked
                holder.imageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                        R.drawable.ic_launcher_background, null));
            }
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView title_tv, latitude_tv, longitude_tv, expirationTime_tv,
                geoFenceType_tv, circleSize_tv, state_tv, expirationDuration_tv;
        ImageView imageView;
        ConstraintLayout clLayout_two, main_cl;
        LinearLayout swiping_ll, clLlayout_one;
        CardView cardView;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            main_cl = itemView.findViewById(R.id.main_cl);
            clLlayout_one = itemView.findViewById(R.id.clLlayout_one);
            cardView = itemView.findViewById(R.id.cardView);
            imageView = itemView.findViewById(R.id.imageView);
            title_tv = itemView.findViewById(R.id.title_tv);
            circleSize_tv = itemView.findViewById(R.id.circleSize_tv);
            latitude_tv = itemView.findViewById(R.id.latitude_tv);
            longitude_tv = itemView.findViewById(R.id.longitude_tv);
            expirationTime_tv = itemView.findViewById(R.id.expirationTime_tv);
            expirationDuration_tv = itemView.findViewById(R.id.expirationDuration_tv);
            geoFenceType_tv = itemView.findViewById(R.id.geoFenceType_tv);
            state_tv = itemView.findViewById(R.id.state_tv);
            clLayout_two = itemView.findViewById(R.id.clLayout_two);
            swiping_ll = itemView.findViewById(R.id.swiping_ll);
        }
    }

    public void deleteData() {
        GeofencingClient geofencingClient = LocationServices.getGeofencingClient(context);
        List<String> idList = new ArrayList<>();
        if (!sendingList.isEmpty()) {
            for (String id : sendingList) {
                idList.add(id);
                myDatabase.deleteLocation(id);
            }
            geofencingClient.removeGeofences(idList).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
            imageHide();

            sendingList.clear();
            isLongClicked = false;
            longClickListener.onLongClicked(false);
        } else {
            Toast.makeText(context, "No item selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void imageHide() {
        for (HistoryModel h : list) {
            holder.imageView.setVisibility(View.GONE);
        }

    }

    public void imageShow() {
        for (HistoryModel h : list) {
            holder.imageView.setVisibility(View.VISIBLE);

        }

    }



}
