package com.example.profilechanger.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profilechanger.R;
import com.example.profilechanger.adapters.ProfilesAdapter;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.annotations.NoAnnotation;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.geofences.GeoFence;
import com.example.profilechanger.interfaces.SendDataWithKey;
import com.example.profilechanger.models.ProfilesModel;
import com.example.profilechanger.permissions.Permissions;
import com.example.profilechanger.sharedpreferences.MyPreferences;
import com.example.profilechanger.utils.BooleansUtils;
import com.example.profilechanger.utils.SoundProfileActions;
import com.example.profilechanger.utils.TimeUtil;
import com.example.profilechanger.utils.Utils;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsFragment extends Fragment implements LocationListener, SendDataWithKey {
    private GeofencingClient geoClient;
    private GoogleMap map;
    private Permissions permissions;
    private MyPreferences preferences;
    private MyDatabase myDatabase;
    private GeoFence geoFence;
    private TimeUtil timeUtils;
    private TextView geoFencesLimitTv;
    private final long geoFencesLimit = 20;
    NotificationManager notificationManager;
    private AlertDialog dialog;

    SoundProfileActions actions;
    BooleansUtils booleansUtils;
    MyPreferences myPreferences;

    SearchView search_sv;
    TextView circle_tv, geoFenceType_tv, expirationTime_tv, onExitProfile_tv, onEnterProfile_tv;
    TextInputEditText input_et;
    private float geoFenceCircle = NoAnnotation.WALK;
    String geoFenceType = MyAnnotations.ENTER;
    String geoExpireTime = String.valueOf(DateUtils.HOUR_IN_MILLIS);
    String profileStartId = MyAnnotations.N_A;
    String profileEndId = MyAnnotations.N_A;

    Context context;

    Utils utils;

    CardView onInter_cv;
    TextView onInter_tv;
    CardView onExit_cv;
    TextView onExit_tv;
    //    View help_map_layout;
    View view;
    //    String id = "0";
//    boolean isUpdate = false;
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            map = googleMap;
            enableUserCurrentLocation();
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(context),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.permission();
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                moveToCurrentLocation(latLng);
            }
            if (myDatabase.retrieveRowsAmountLocation() != 0) {
                if (preferences.getBoolean(MyAnnotations.BOOT_COMPLETED, false)) {
                    //add geo_fences again if user want to re-add undone geo_fences
                    onBootComplete(context);

                } else {
                    //just set mark and circle.do not add geo_fences
                    setFromDbToGeoFenceAgain();
                }
            }
            map.setOnMapLongClickListener(latLng -> {
                if (getGeoFenceLimit() < geoFencesLimit) {
                    addGeoProfilerDialog(latLng);
                } else {
                    Toast.makeText(context, "Limit is reached", Toast.LENGTH_SHORT).show();
                }
            });

            map.setOnMyLocationButtonClickListener(() -> {
                if (!isLocationEnabled(context)) {
                    showDialog("Location", "To move on your current location " +
                            "you have to ON the device location.");
                }
                return false;
            });
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maps, container, false);

        geoFencesLimitTv = view.findViewById(R.id.geo_fences_amount_tv);
        search_sv = view.findViewById(R.id.search_sv);
        context = getContext();

        if (context != null) {
            preferences = new MyPreferences(context);
            actions = new SoundProfileActions(context);

        }
        permissions = new Permissions(context);
        booleansUtils = new BooleansUtils(context);
        geoClient = LocationServices.getGeofencingClient(context);
        geoFence = new GeoFence(context);
        myDatabase = new MyDatabase(context);
        utils = new Utils(context);
        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        myPreferences = new MyPreferences(context);

        timeUtils = new TimeUtil();
        // check time if its 12am then change the time and renew the limit of geo Fences
        if (checkGeoFenceChangeDate()) {
            geoFencesLimitTv.setText(String.valueOf(geoFencesLimit));
        } else {
            geoFencesLimitTv.setText(String.valueOf(geoFencesLimit - getGeoFenceLimit()));
        }

        search_sv.setQueryHint("address");
        search_sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String location) {
                if (booleansUtils.isWifiEnable()) {
                    List<Address> addressList = new ArrayList<>();

                    if (location != null || !location.equals("")) {
                        Geocoder geocoder = new Geocoder(context);
                        try {
                            addressList = geocoder.getFromLocationName(location, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addressList != null) {
                            if (!addressList.isEmpty()) {
                                Address address = addressList.get(0);
                                LatLng latLng = new LatLng(address.getLatitude(),
                                        address.getLongitude());
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                            } else {
                                Toast.makeText(geoFence, getString(R.string.not_found)
                                        , Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(context, getString(R.string.pleas_enter_title),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.no_internet_conection),
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // add geo fence
    private void geoFenceFun(int id, String title, LatLng latLng, float radius,
                             String transitionType, String expirationTime) {
        Geofence geofence = geoFence.getGeoFence(String.valueOf(id)
                , latLng, radius, transitionType, expirationTime);

        GeofencingRequest geofencingRequest = geoFence.getGeoFenceRequest(geofence);
        PendingIntent pendingIntent = geoFence.getPendingIntent(id);
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(context),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.permission();
            return;
        }
        geoClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(aVoid -> {
                    addMarkAndMoveCamera(latLng, title);
                    addCircleOfGeofence(latLng, geoFenceCircle);
                    myPreferences.addLong(MyAnnotations.GEO_FENCE_LIMIT,
                            myPreferences.getLong(MyAnnotations.GEO_FENCE_LIMIT, 0)
                                    + 1);
                    geoFencesLimitTv.setText(String.valueOf(geoFencesLimit - getGeoFenceLimit()));
                }).addOnFailureListener(e -> Toast.makeText(context, geoFence.getErrorMessage(e),
                Toast.LENGTH_SHORT).show());
    }

    private void addMarkAndMoveCamera(LatLng latLng, String title) {
        map.addMarker(new MarkerOptions().position(latLng).title(title));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    // add circle around the mark on map
    private void addCircleOfGeofence(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(225, 0, 50, 50));
        circleOptions.fillColor(Color.argb(40, 0, 10, 70));
        circleOptions.strokeWidth(5);
        map.addCircle(circleOptions);


    }


    private void moveToCurrentLocation(LatLng latLng) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
    }

    private void enableUserCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissions.permission();
            return;
        }
        map.setMyLocationEnabled(true);
    }

    // is location on or off
    public boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

    public void showDialog(String title, String message) {
        AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle(title).setMessage(message).setCancelable(true);
        adb.setPositiveButton(R.string.ok, (dialog, which) -> {
            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            dialog.dismiss();
        }).setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        androidx.appcompat.app.AlertDialog dialog = adb.create();
        dialog.show();
    }

//    public void showWifiDialog(String title, String message) {
//        AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(context)
//                .setTitle(title).setMessage(message).setCancelable(true);
//        adb.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                booleansUtils.setWifiOnOff(true);
//                dialog.dismiss();
//            }
//        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        androidx.appcompat.app.AlertDialog dialog = adb.create();
//        dialog.show();
//    }


    private void onBootComplete(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Alert").setMessage("If you want to recover all old" +
                        " selected areas it will reduce the limit of marks")
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    //if user want to add all geo_fences again
                    setFromDbOnBootComplete();
                    myPreferences.setBoolean(MyAnnotations.BOOT_COMPLETED, false);
                    geoFencesLimitTv.setText(String.valueOf(geoFencesLimit - getGeoFenceLimit()));

                    dialog.dismiss();
                }).setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void setFromDbToGeoFenceAgain() {
        Cursor cursor = myDatabase.retrieveLocation();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String title = cursor.getString(1);
            String latitude = cursor.getString(2);
            String longitude = cursor.getString(3);
            String radius = cursor.getString(4);
            String expireFormatted = cursor.getString(6);
            String state = cursor.getString(8);

            long now = System.currentTimeMillis();
            if (isLocationEnabled(context)) {
                if (state.matches(MyAnnotations.UN_DONE)) {
                    if (now < Long.parseLong(expireFormatted)) {
                        LatLng location = new LatLng(Double.parseDouble(latitude),
                                Double.parseDouble(longitude));
                        //just add mark and circles
                        addMarkAndMoveCamera(location, title);
                        addCircleOfGeofence(location, Float.parseFloat(radius));
                    } else {
                        myDatabase.update(id, MyAnnotations.DONE);
                    }
                }
            } else {
                //ask to enable location if location is OFF
                showDialog(getString(R.string.location),
                        getString(R.string.you_need_to_allow_location));
            }
        }

    }


    private void setFromDbOnBootComplete() {
        Cursor cursor = myDatabase.retrieveLocation();
        long howManyAdded = 0;
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String title = cursor.getString(1);
            String latitude = cursor.getString(2);
            String longitude = cursor.getString(3);
            String radius = cursor.getString(4);
            String type = cursor.getString(5);
            String expireFormatted = cursor.getString(6);
            String expireTime = cursor.getString(7);
            String state = cursor.getString(8);
            String date = cursor.getString(9);
            String enterProfilerId = cursor.getString(10);
            String exitProfileId = cursor.getString(11);
            long now = timeUtils.getNowMillis();


            if (isLocationEnabled(context)) {
                if (state.matches(MyAnnotations.UN_DONE)) {
                    if (now < Long.parseLong(expireFormatted)) {
                        if (howManyAdded < geoFencesLimit) {
                            //insert as new because we have limits of 100 geofences.
                            long insert = myDatabase.insert(title, latitude, longitude, radius,
                                    geoFenceType, expireFormatted, expireTime, state, date,
                                    enterProfilerId, exitProfileId);

                            if (insert != -1) {
                                LatLng latLng = new LatLng(Double.parseDouble(latitude),
                                        Double.parseDouble(longitude));
                                geoFenceFun((int) insert, title, latLng, Float.parseFloat(radius),
                                        type, expireTime);
                                howManyAdded++;
                            } else {
                                Toast.makeText(getContext(),
                                        getString(R.string.Profiler_not_inserted),
                                        Toast.LENGTH_SHORT).show();
                            }
                            //done old geofence
                            myDatabase.update(id, MyAnnotations.DONE);

                        } else {
                            Toast.makeText(getContext(), "Limit reached",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        //done old geofence
                        myDatabase.update(id, MyAnnotations.DONE);
                    }

                }
            } else {
                //ask to enable location if location is OFF
                showDialog(getString(R.string.location),
                        getString(R.string.you_need_to_allow_location));
            }
        }

    }


    public void addGeoProfilerDialog(LatLng latLng) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_input_dialog_layout,
                null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(view).setCancelable(true);
        CardView circleSize_cv = view.findViewById(R.id.circleSize_cv);
        circle_tv = view.findViewById(R.id.circle_tv);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        input_et = view.findViewById(R.id.input_et);
        TextView dialogTitle_tv = view.findViewById(R.id.dialogTitle_tv);
        dialogTitle_tv.setText(getResources().getString(R.string.location_profiler));
        input_et.setImeOptions(EditorInfo.IME_ACTION_DONE);
        input_et.setSingleLine(true);

        CardView geoType_cl = view.findViewById(R.id.geoType_cl);
        geoFenceType_tv = view.findViewById(R.id.geoFenceType_tv);

        onInter_cv = view.findViewById(R.id.onInter_cv);
        ConstraintLayout onEntered_cl = view.findViewById(R.id.onEntered_cl);
        ConstraintLayout onExit_cl = view.findViewById(R.id.onExit_cl);
        onInter_tv = view.findViewById(R.id.materialTextView2);
        onEnterProfile_tv = view.findViewById(R.id.onEnterProfile_tv);

        onExit_cv = view.findViewById(R.id.onExit_cv);
        onExit_tv = view.findViewById(R.id.materialTextView3);
        onExitProfile_tv = view.findViewById(R.id.onExitProfile_tv);

        CardView timePicker_cv = view.findViewById(R.id.timePicker_cv);
        expirationTime_tv = view.findViewById(R.id.expirationTime_tv);

        TextView negative_tv = view.findViewById(R.id.negative_tv);
        TextView positive_tv = view.findViewById(R.id.positive_tv);

        circleSize_cv.setOnClickListener(v -> {
            PopupWindow popUpWindow = circleSizePopupMenu();
            popUpWindow.showAsDropDown(circleSize_cv, 0, 0);

        });
        geoType_cl.setOnClickListener(v -> {
            PopupWindow popUpWindow = geoFencePopupMenu();
            popUpWindow.showAsDropDown(geoType_cl, 0, 0);

        });

        timePicker_cv.setOnClickListener(v -> {
            PopupWindow popUpWindow = geoExpireTimePopUp();
            popUpWindow.showAsDropDown(timePicker_cv, 0, 0);

        });
        onEntered_cl.setOnClickListener(v -> profileDialog(MyAnnotations.START_PROFILE_ID));
        onExit_cl.setOnClickListener(v -> profileDialog(MyAnnotations.END_PROFILE_ID));


        positive_tv.setOnClickListener(v -> {
            String date = timeUtils.getCurrentFormattedDate()
                    + " " +
                    timeUtils.getCurrentFormattedTime();
            String title = input_et.getText().toString();
            geoFenceType = geoFenceType_tv.getText().toString();
            long expirationEnd = System.currentTimeMillis() + Long.parseLong(geoExpireTime);


            if (!ifOneFieldEmpty()) {
                if (isLocationEnabled(context)) {

                    long insert = myDatabase.insert(title, String.valueOf(latLng.latitude)
                            , String.valueOf(latLng.longitude), String.valueOf(geoFenceCircle),
                            geoFenceType, String.valueOf(expirationEnd), geoExpireTime,
                            MyAnnotations.UN_DONE, date, profileStartId, profileEndId);

                    if (insert != -1) {
                        geoFenceFun((int) insert, title, latLng, geoFenceCircle,
                                geoFenceType, geoExpireTime);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(),
                                getString(R.string.Profiler_not_inserted),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //ask to enable location if location is OFF
                    showDialog(getString(R.string.location),
                            getString(R.string.you_need_to_allow_location));

                }
            } else {
                Toast.makeText(getContext(), getString(R.string.please_fill_all_fields),
                        Toast.LENGTH_SHORT).show();
            }


        });

        negative_tv.setOnClickListener(v -> dialog.dismiss());

    }

    public boolean ifOneFieldEmpty() {
        if (geoFenceType.matches(MyAnnotations.ENTER)) {
            return input_et.getText().toString().isEmpty() ||
                    circle_tv.getText().toString().isEmpty() ||
                    geoFenceType_tv.getText().toString().isEmpty() ||
                    onEnterProfile_tv.getText().toString().isEmpty() ||
                    expirationTime_tv.getText().toString().isEmpty();
        } else if (geoFenceType.matches(MyAnnotations.EXIT)) {
            return input_et.getText().toString().isEmpty() ||
                    circle_tv.getText().toString().isEmpty() ||
                    geoFenceType_tv.getText().toString().isEmpty() ||
                    onExitProfile_tv.getText().toString().isEmpty() ||
                    expirationTime_tv.getText().toString().isEmpty();
        } else
            return input_et.getText().toString().isEmpty() ||
                    circle_tv.getText().toString().isEmpty() ||
                    geoFenceType_tv.getText().toString().isEmpty() ||
                    onEnterProfile_tv.getText().toString().isEmpty() ||
                    onExitProfile_tv.getText().toString().isEmpty() ||
                    expirationTime_tv.getText().toString().isEmpty();
    }

    public boolean checkGeoFenceChangeDate() {
        Cursor cursor = myDatabase.getPerDay();
        if (cursor.getCount() == 0) {
            long i = myDatabase.insert(String.valueOf(timeUtils.get12AmMillis()));
            myPreferences.addLong(MyAnnotations.GEO_FENCE_LIMIT, (long) 0);
            return true;
        }

        while (cursor.moveToNext()) {
            long time = Long.parseLong(cursor.getString(1));
            long currentTime = timeUtils.getNowMillis();
            String stime = timeUtils.getFormattedDateAndTime(time);
            String scurrentTime = timeUtils.getFormattedDateAndTime(currentTime);
            if (currentTime > time) {

                String date12crrentTime =
                        timeUtils.getFormattedDateAndTime(timeUtils.get12AmMillis());
                myDatabase.updatePerDay("0", String.valueOf(timeUtils.get12AmMillis()));
                myPreferences.addLong(MyAnnotations.GEO_FENCE_LIMIT, (long) 0);
                return true;
            }
        }
        return false;
    }


    public long getGeoFenceChangeTime() {
        long time = 0;
        Cursor cursor = myDatabase.getPerDay();
        if (cursor.getCount() == 0) {
            return 0;
        } else

            while (cursor.moveToNext()) {
                time = Long.parseLong(cursor.getString(1));
            }

        return time;

    }

    public long getGeoFenceLimit() {

        return myPreferences.getLong(MyAnnotations.GEO_FENCE_LIMIT, 0);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }

    private PopupWindow circleSizePopupMenu() {
        PopupWindow circleSizePopupMenu = new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.circle_size_popup_menu_view, null);

        ConstraintLayout walk_cl = view.findViewById(R.id.byWalk_cl);
        ConstraintLayout cycle_cl = view.findViewById(R.id.byCycle_cl);
        ConstraintLayout bus_cl = view.findViewById(R.id.byBus_cl);
        ConstraintLayout car_cl = view.findViewById(R.id.byCar_cl);


        circleSizePopupMenu.setFocusable(true);
        circleSizePopupMenu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        circleSizePopupMenu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        circleSizePopupMenu.setContentView(view);
        circleSizePopupMenu.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        circleSizePopupMenu.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        walk_cl.setOnClickListener(v -> {
            circle_tv.setText(MyAnnotations.BY_WALK);
            geoFenceCircle = NoAnnotation.WALK;

            circleSizePopupMenu.dismiss();
        });
        cycle_cl.setOnClickListener(v -> {
            circle_tv.setText(MyAnnotations.BY_CYCLE);
            geoFenceCircle = NoAnnotation.CYCLE;

            circleSizePopupMenu.dismiss();

        });
        bus_cl.setOnClickListener(v -> {
            circle_tv.setText(MyAnnotations.BY_BUS);
            geoFenceCircle = NoAnnotation.BUS;
            circleSizePopupMenu.dismiss();

        });
        car_cl.setOnClickListener(v -> {
            circle_tv.setText(MyAnnotations.BY_CAR);
            geoFenceCircle = NoAnnotation.CAR;
            circleSizePopupMenu.dismiss();

        });
        return circleSizePopupMenu;
    }


//    private PopupWindow ringingModePopupMenu(boolean entered) {
//
//        PopupWindow ringingPopupWindow = new PopupWindow(context);
//
//        LayoutInflater inflater = (LayoutInflater)
//                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.ringging_mode_view, null);
//
//        ConstraintLayout ringing_cl = view.findViewById(R.id.ringing_cl);
//        ConstraintLayout silent_cl = view.findViewById(R.id.silent_cl);
//        ConstraintLayout vibrate_cl = view.findViewById(R.id.vibrate_cl);
//
//        ringingPopupWindow.setFocusable(true);
//        ringingPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        ringingPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        ringingPopupWindow.setContentView(view);
//        ringingPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        ringingPopupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//
//
//        
//        return ringingPopupWindow;
//    }

    private PopupWindow geoFencePopupMenu() {
        PopupWindow geoFencePopupMenu = new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.geo_fence_popup_view, null);

        NumberPicker numberPicker = view.findViewById(R.id.geo_fenceType_np);

        String[] type = new String[]{MyAnnotations.ENTER, MyAnnotations.EXIT, MyAnnotations.BOTH};
        numberPicker.setMaxValue(3);
        numberPicker.setMinValue(1);

        geoFencePopupMenu.setFocusable(true);
        geoFencePopupMenu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        geoFencePopupMenu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        geoFencePopupMenu.setContentView(view);
        geoFencePopupMenu.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            geoFencePopupMenu.setElevation(5.0f);
        }
        geoFencePopupMenu.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        numberPicker.setDisplayedValues(type);
        setNumberPickerTextColor(numberPicker);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            int s = picker.getValue();
            if (s == 1) {
                onInter_cv.setVisibility(View.VISIBLE);
                onInter_tv.setVisibility(View.VISIBLE);
                onExit_cv.setVisibility(View.GONE);
                onExit_tv.setVisibility(View.GONE);

                geoFenceType = MyAnnotations.ENTER;
                geoFenceType_tv.setText(MyAnnotations.ENTER);
            } else if (s == 2) {
                onInter_cv.setVisibility(View.GONE);
                onInter_tv.setVisibility(View.GONE);
                onExit_cv.setVisibility(View.VISIBLE);
                onExit_tv.setVisibility(View.VISIBLE);
                geoFenceType = MyAnnotations.EXIT;
                geoFenceType_tv.setText(MyAnnotations.EXIT);
            } else {
                onInter_cv.setVisibility(View.VISIBLE);
                onInter_tv.setVisibility(View.VISIBLE);
                onExit_cv.setVisibility(View.VISIBLE);
                onExit_tv.setVisibility(View.VISIBLE);
                geoFenceType = MyAnnotations.BOTH;
                geoFenceType_tv.setText(MyAnnotations.BOTH);
            }
        });
        return geoFencePopupMenu;
    }

    private PopupWindow geoExpireTimePopUp() {
        PopupWindow geoFencePopupMenu = new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.geo_fence_popup_view, null);

        NumberPicker numberPicker = view.findViewById(R.id.geo_fenceType_np);

        numberPicker.setMaxValue(3);
        numberPicker.setMinValue(1);

        numberPicker.setMaxValue(24);
        numberPicker.setMinValue(1);
        String[] values = new String[24];
        for (int i = 0; i < values.length; i++) {
            if (i == 0) {

                values[i] = i + 1 + " hr";
            } else {
                values[i] = i + 1 + " hrs";
            }

        }
        geoFencePopupMenu.setFocusable(true);
        geoFencePopupMenu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        geoFencePopupMenu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        geoFencePopupMenu.setContentView(view);
        geoFencePopupMenu.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            geoFencePopupMenu.setElevation(5.0f);
        }
        geoFencePopupMenu.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        numberPicker.setDisplayedValues(values);
        setNumberPickerTextColor(numberPicker);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            int s = picker.getValue();
            geoExpireTime = String.valueOf(picker.getValue() *
                    DateUtils.HOUR_IN_MILLIS);
            if (s == 1) {
                expirationTime_tv.setText(s + " hr");

            } else {
                expirationTime_tv.setText(s + " hrs");
            }
        });
        geoExpireTime = String.valueOf(numberPicker.getValue() *
                DateUtils.HOUR_IN_MILLIS);
        return geoFencePopupMenu;
    }

    public void setNumberPickerTextColor(NumberPicker numberPicker) {
        //change color of of number picker
        int color = R.attr.textWhiteGray;
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField =
                            numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                } catch (NoSuchFieldException e) {
                    Log.e("setNumberPickerColor1", "" + e);
                } catch (IllegalAccessException e) {
                    Log.e("setNumberPickerColor2", "" + e);
                } catch (IllegalArgumentException e) {
                    Log.e("setNumberPickerColor3", "" + e);
                }
            }
        }
    }

    public void profileDialog(String profile) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.start_end_profile_dialog_layout,
                        null, false);
        RecyclerView recyclerView = view.findViewById(R.id.startEndProfile_rv);

        recyclerView.setLayoutManager(layoutManager);
        ArrayList<ProfilesModel> list = new ArrayList<>();
        ProfilesModel profilesModel;
        Cursor cursor = myDatabase.retrieveProfile();
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
        ProfilesAdapter profilesAdapter = new ProfilesAdapter(getContext(), list, myDatabase,
                true, true);
        recyclerView.setAdapter(profilesAdapter);
        profilesAdapter.notifyDataSetChanged();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(view).setCancelable(true);
        profilesAdapter.setSendDataWithKey(this, profile);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        context = getContext();

    }

    @Override
    public void data(String key, String data, String title) {
        if (key.matches(MyAnnotations.START_PROFILE_ID)) {
            profileStartId = data;
            onEnterProfile_tv.setText(title);


        } else {
            profileEndId = data;
            onExitProfile_tv.setText(title);
        }
        if (dialog.isShowing() && dialog != null) {
            dialog.dismiss();
        }
    }


}