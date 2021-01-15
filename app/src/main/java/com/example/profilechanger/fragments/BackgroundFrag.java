package com.example.profilechanger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.profilechanger.R;
import com.example.profilechanger.activities.MainActivity;
import com.example.profilechanger.activities.PermissionActivity;
import com.example.profilechanger.permissions.Permissions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BackgroundFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BackgroundFrag extends Fragment {
    View view;
    private Permissions permissions;
    TextView turnOn_tv;
    TextView noThanks_tv;

    public static BackgroundFrag newInstance() {
        return new BackgroundFrag();
    }

    public BackgroundFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_background, container, false);
        permissions = new Permissions(getContext());

        turnOn_tv = view.findViewById(R.id.turnOn_tv);
        noThanks_tv = view.findViewById(R.id.noThanks_tv);


        turnOn_tv.setOnClickListener(v -> {
            if (!permissions.locationPer()) {
                permissions.permission();
             }

        });
        noThanks_tv.setOnClickListener((View.OnClickListener) v ->
                ((PermissionActivity)getActivity()).finishAffinity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}