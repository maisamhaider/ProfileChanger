package com.example.profilechanger.activities;

 import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

 import android.app.Activity;
 import android.content.pm.ActivityInfo;
 import android.os.Bundle;

import com.example.profilechanger.R;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.fragments.BottomSheetFragmentMain;
import com.example.profilechanger.fragments.MapsFragment;
import com.example.profilechanger.interfaces.ClickListener;

public class LocationBaseEditActivity extends BaseActivity  implements ClickListener {
    BottomSheetFragmentMain bottomSheetFragmentMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_base_edit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String id = getIntent().getStringExtra(MyAnnotations.LOCATION_PROFILER_ID);
        boolean isUpdate = getIntent().getBooleanExtra(MyAnnotations.IS_UPDATE, false);

        bottomSheetFragmentMain = new BottomSheetFragmentMain(this);
        ConstraintLayout bottomSheetStartCl = findViewById(R.id.bottomSheetStart_cl);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = new MapsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MyAnnotations.LOCATION_PROFILER_ID, id);
        bundle.putBoolean(MyAnnotations.IS_UPDATE, isUpdate);
        fragment.setArguments(bundle);
        ft.replace(R.id.fragmentsContainer, fragment, null).commit();
/*
        bottomSheetStartCl.setVisibility(View.VISIBLE);
        if (bottomSheetFragmentMain.isHidden()) {

        } else {
            bottomSheetStartCl.setVisibility(View.VISIBLE);

        }*/
//
//        bottomSheetStartCl.performClick();
//        bottomSheetStartCl.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    bottomSheetFragmentMain.show(getSupportFragmentManager(),
//                            "Main_bottom_sheet");
//                }
//                return true;
//            }
//        });

    }


    @Override
    public void click(String button) {
//        if (button.matches(MyAnnotations.ON)) {
//
//            if (bottomSheetFragmentMain != null && bottomSheetFragmentMain.isVisible()) {
//                bottomSheetFragmentMain.dismiss();
//            }
//        }
    }
}