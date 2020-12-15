package com.example.profilechanger.models;

public class TimeBaseProfiler {

    String TIME_ID;
    String         TIME_TITLE;
    String TIME_PROFILE_TITLE;
    String         TIME_START_TIME;
    String TIME_END_TIME;
    String         TIME_STATE;
    String TIME_DATE;
    String         TIME_REPEAT;
    String TIME_DAYS;
    String         TIME_PROFILE_START;
    String TIME_PROFILE_END;

    public TimeBaseProfiler(String TIME_ID, String TIME_TITLE, String TIME_PROFILE_TITLE,
                            String TIME_START_TIME, String TIME_END_TIME, String TIME_STATE,
                            String TIME_DATE, String TIME_REPEAT, String TIME_DAYS,
                            String TIME_PROFILE_START, String TIME_PROFILE_END) {
        this.TIME_ID = TIME_ID;
        this.TIME_TITLE = TIME_TITLE;
        this.TIME_PROFILE_TITLE = TIME_PROFILE_TITLE;
        this.TIME_START_TIME = TIME_START_TIME;
        this.TIME_END_TIME = TIME_END_TIME;
        this.TIME_STATE = TIME_STATE;
        this.TIME_DATE = TIME_DATE;
        this.TIME_REPEAT = TIME_REPEAT;
        this.TIME_DAYS = TIME_DAYS;
        this.TIME_PROFILE_START = TIME_PROFILE_START;
        this.TIME_PROFILE_END = TIME_PROFILE_END;
    }

    public TimeBaseProfiler() {
    }

    public String getTIME_ID() {
        return TIME_ID;
    }

    public void setTIME_ID(String TIME_ID) {
        this.TIME_ID = TIME_ID;
    }

    public String getTIME_TITLE() {
        return TIME_TITLE;
    }

    public void setTIME_TITLE(String TIME_TITLE) {
        this.TIME_TITLE = TIME_TITLE;
    }

    public String getTIME_PROFILE_TITLE() {
        return TIME_PROFILE_TITLE;
    }

    public void setTIME_PROFILE_TITLE(String TIME_PROFILE_TITLE) {
        this.TIME_PROFILE_TITLE = TIME_PROFILE_TITLE;
    }

    public String getTIME_START_TIME() {
        return TIME_START_TIME;
    }

    public void setTIME_START_TIME(String TIME_START_TIME) {
        this.TIME_START_TIME = TIME_START_TIME;
    }

    public String getTIME_END_TIME() {
        return TIME_END_TIME;
    }

    public void setTIME_END_TIME(String TIME_END_TIME) {
        this.TIME_END_TIME = TIME_END_TIME;
    }

    public String getTIME_STATE() {
        return TIME_STATE;
    }

    public void setTIME_STATE(String TIME_STATE) {
        this.TIME_STATE = TIME_STATE;
    }

    public String getTIME_DATE() {
        return TIME_DATE;
    }

    public void setTIME_DATE(String TIME_DATE) {
        this.TIME_DATE = TIME_DATE;
    }

    public String getTIME_REPEAT() {
        return TIME_REPEAT;
    }

    public void setTIME_REPEAT(String TIME_REPEAT) {
        this.TIME_REPEAT = TIME_REPEAT;
    }

    public String getTIME_DAYS() {
        return TIME_DAYS;
    }

    public void setTIME_DAYS(String TIME_DAYS) {
        this.TIME_DAYS = TIME_DAYS;
    }

    public String getTIME_PROFILE_START() {
        return TIME_PROFILE_START;
    }

    public void setTIME_PROFILE_START(String TIME_PROFILE_START) {
        this.TIME_PROFILE_START = TIME_PROFILE_START;
    }

    public String getTIME_PROFILE_END() {
        return TIME_PROFILE_END;
    }

    public void setTIME_PROFILE_END(String TIME_PROFILE_END) {
        this.TIME_PROFILE_END = TIME_PROFILE_END;
    }
}
