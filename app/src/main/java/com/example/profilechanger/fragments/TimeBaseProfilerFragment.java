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
import com.example.profilechanger.activities.TimeBaseProfilerEditActivity;
import com.example.profilechanger.adapters.ProfilerAdapter;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.models.ProfilerModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeBaseProfilerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeBaseProfilerFragment extends Fragment {

    private MyDatabase database;
    private RecyclerView timeBaseProfiler_rv;
    private ArrayList<ProfilerModel> list;
    TextView noData_tv;

    public TimeBaseProfilerFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TimeBaseProfilerFragment newInstance() {
        return new TimeBaseProfilerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_base_profiler, container,
                false);
        database = new MyDatabase(getContext());
        timeBaseProfiler_rv = view.findViewById(R.id.timeBaseProfiler_rv);
        noData_tv = view.findViewById(R.id.noData_mTv);
        ImageView addNew_mBtn = view.findViewById(R.id.addNew_mBtn);

        addNew_mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(),
                        TimeBaseProfilerEditActivity.class).putExtra(MyAnnotations.IS_UPDATE,
                        false)
                );

            }
        });
        return view;
    }

    public void load() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        int size = list.size();
        ProfilerAdapter adapter = new ProfilerAdapter(getContext(), list, true, database);
        timeBaseProfiler_rv.setAdapter(adapter);
        timeBaseProfiler_rv.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
    }

    public void addDataInList() {
        list = new ArrayList<>();
        ProfilerModel model;
        Cursor cursor = database.retrieveTimeTable();
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