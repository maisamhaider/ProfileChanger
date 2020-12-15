package com.example.profilechanger.annotations;

import android.text.format.DateUtils;

public @interface NoAnnotation {
    int HOUR_IN_MILLISECONDS = (int) DateUtils.HOUR_IN_MILLIS;
    float WALK = 60;
    float CYCLE = 100;
    float BUS = 200;
    float CAR = 250;
}
