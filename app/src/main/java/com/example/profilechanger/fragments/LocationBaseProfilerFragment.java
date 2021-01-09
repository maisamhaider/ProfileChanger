package com.example.profilechanger.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.profilechanger.R;
import com.example.profilechanger.activities.BaseActivity;
import com.example.profilechanger.activities.LocationBaseEditActivity;
import com.example.profilechanger.adapters.ProfilerAdapter;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.models.ProfilerModel;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationBaseProfilerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationBaseProfilerFragment extends Fragment {

    private MyDatabase database;
    private RecyclerView locationBaseProfiler_rv;
    private ArrayList<ProfilerModel> list;
    TextView noData_tv;
    public LocationBaseProfilerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LocationBaseProfilerFragment newInstance() {
        return new LocationBaseProfilerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_base_plofiler, container, false);
        database = new MyDatabase(getContext());
        locationBaseProfiler_rv = view.findViewById(R.id.locationBaseProfiler_rv);
          noData_tv = view.findViewById(R.id.noData_mTv);
        ImageView addNew_mBtn = view.findViewById(R.id.addNewLocation_mBtn);

        AdView adView = view.findViewById(R.id.adView);
        ((BaseActivity) getContext()).adView(adView);
        addDataInList();
        load();
        if (list.isEmpty()) {
            noData_tv.setVisibility(View.VISIBLE);
        } else {
            noData_tv.setVisibility(View.GONE);

        }
        addNew_mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (((BaseActivity) getContext())!=null) {
                    ((BaseActivity) getContext()).startActivity(new Intent(getActivity(),
                            LocationBaseEditActivity.class));
                }
            }
        });
        return view;
    }

    public void load() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        ProfilerAdapter adapter = new ProfilerAdapter(getContext(), list, false, database);
        locationBaseProfiler_rv.setLayoutManager(layoutManager);
        locationBaseProfiler_rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void addDataInList() {
        list = new ArrayList<>();
        ProfilerModel model;
        Cursor cursor = database.retrieveLocation();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                model = new ProfilerModel();
                String id = cursor.getString(0);
                String title = cursor.getString(1);
                model.setId(id);
                model.setName(title);
                list.add(model);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        addDataInList();
        load();
        if (list.isEmpty()) {
            noData_tv.setVisibility(View.VISIBLE);
        } else {
            noData_tv.setVisibility(View.GONE);

        }
    }
}