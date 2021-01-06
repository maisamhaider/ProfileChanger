package com.example.profilechanger.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.profilechanger.R;
import com.example.profilechanger.adapters.ProfilesAdapter;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.models.ProfilesModel;
import com.example.profilechanger.sharedpreferences.MyPreferences;

import java.util.ArrayList;

public class
ProfilesActivity extends BaseActivity {

    private MyDatabase database;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
        database = new MyDatabase(this);
        MyPreferences preferences = new MyPreferences(this);
        recyclerView = findViewById(R.id.profiles_rv);

        ImageView profileBack_iv = findViewById(R.id.profileBack_iv);
        profileBack_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView addNewProfile_mBtn = findViewById(R.id.addNewProfile_mBtn);
        addNewProfile_mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilesActivity.this,
                        OpenProfileActivity.class);
                intent.putExtra(MyAnnotations.PROFILE_NEW, true);
                startActivity(intent);
            }
        });
        boolean isFirstTime = preferences.getBoolean(MyAnnotations.PRE_PROFILES_LOADED, false);
        if (!isFirstTime) {
            insertPreDefinedProfiles();
            preferences.setBoolean(MyAnnotations.PRE_PROFILES_LOADED, true);
        }
        loadProfiles();

    }


    public void loadProfiles() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(ProfilesActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<ProfilesModel> list = new ArrayList<>();
        ProfilesModel profilesModel;
        Cursor cursor = database.retrieveProfile();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                profilesModel = new ProfilesModel();
                String id = cursor.getString(0);
                String title = cursor.getString(1);

                profilesModel.setId(id);
                profilesModel.setPROFILE_TITLE(title);
                list.add(profilesModel);
            }
        }
        ProfilesAdapter profilesAdapter = new ProfilesAdapter(this, list, database,
                false,false);
        recyclerView.setAdapter(profilesAdapter);
        profilesAdapter.notifyDataSetChanged();

    }

    public void insertPreDefinedProfiles() {
        database.insertProfile(getResources().getString(R.string.general),
                MyAnnotations.RINGER_MODE_NORMAL,
                "90", "90", "90", "90",
                MyAnnotations.ON, MyAnnotations.ON, MyAnnotations.ON);

        database.insertProfile(getResources().getString(R.string.loud),
                MyAnnotations.RINGER_MODE_NORMAL,
                "100", "100", "100", "100",
                MyAnnotations.ON, MyAnnotations.ON, MyAnnotations.ON);

        database.insertProfile(getResources().getString(R.string.silent),
                MyAnnotations.RINGER_MODE_SILENT,
                "0", "0", "0", "0",
                MyAnnotations.OFF, MyAnnotations.OFF, MyAnnotations.OFF);

        database.insertProfile(getResources().getString(R.string.medium),
                MyAnnotations.RINGER_MODE_NORMAL,
                "60", "60", "60", "60",
                MyAnnotations.ON, MyAnnotations.ON, MyAnnotations.ON);

        database.insertProfile(getResources().getString(R.string.low),
                MyAnnotations.RINGER_MODE_NORMAL,
                "40", "40", "40", "40", MyAnnotations.ON,
                MyAnnotations.ON, MyAnnotations.ON);

        database.insertProfile(getResources().getString(R.string.vibrate),
                MyAnnotations.RINGER_MODE_VIBRATE,
                "0", "0", "0", "0", MyAnnotations.ON,
                MyAnnotations.OFF, MyAnnotations.OFF);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfiles();
    }
}