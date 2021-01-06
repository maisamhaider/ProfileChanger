package com.example.profilechanger.annotations;

import android.text.format.DateUtils;

public @interface NoAnnotation {
    long HOUR_IN_MILLISECONDS =  DateUtils.HOUR_IN_MILLIS;
    float WALK = 100;
    float CYCLE = 150;
    float BUS = 250;
    float CAR = 300;
}
