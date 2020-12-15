package com.example.profilechanger.models;

public class ProfilesModel {
    String id;
    String profileName;
    String PROFILE_TITLE;
    String RINGER_MODE;
    String RINGER_LEVEL;
    String MEDIA_LEVEL;
    String NOTIFICATION_LEVEL;
    String SYSTEM_LEVEL;
    String VIBRATE;
    String TOUCH_SOUND;
    String LOCK_SCREEN_SOUND;
    String DIAL_PAD_SOUND;

    public ProfilesModel(String id, String profileName, String PROFILE_TITLE, String RINGER_MODE,
                         String RINGER_LEVEL, String MEDIA_LEVEL, String NOTIFICATION_LEVEL,
                         String SYSTEM_LEVEL, String VIBRATE,String TOUCH_SOUND,
                         String LOCK_SCREEN_SOUND, String DIAL_PAD_SOUND) {
        this.id = id;
        this.profileName = profileName;
        this.PROFILE_TITLE = PROFILE_TITLE;
        this.RINGER_MODE = RINGER_MODE;
        this.RINGER_LEVEL = RINGER_LEVEL;
        this.MEDIA_LEVEL = MEDIA_LEVEL;
        this.NOTIFICATION_LEVEL = NOTIFICATION_LEVEL;
        this.SYSTEM_LEVEL = SYSTEM_LEVEL;
        this.DIAL_PAD_SOUND = DIAL_PAD_SOUND;
        this.LOCK_SCREEN_SOUND = LOCK_SCREEN_SOUND;
        this.TOUCH_SOUND = TOUCH_SOUND;
        this.VIBRATE = VIBRATE;
    }

    public ProfilesModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getPROFILE_TITLE() {
        return PROFILE_TITLE;
    }

    public void setPROFILE_TITLE(String PROFILE_TITLE) {
        this.PROFILE_TITLE = PROFILE_TITLE;
    }

    public String getRINGER_MODE() {
        return RINGER_MODE;
    }

    public void setRINGER_MODE(String RINGER_MODE) {
        this.RINGER_MODE = RINGER_MODE;
    }

    public String getRINGER_LEVEL() {
        return RINGER_LEVEL;
    }

    public void setRINGER_LEVEL(String RINGER_LEVEL) {
        this.RINGER_LEVEL = RINGER_LEVEL;
    }

    public String getMEDIA_LEVEL() {
        return MEDIA_LEVEL;
    }

    public void setMEDIA_LEVEL(String MEDIA_LEVEL) {
        this.MEDIA_LEVEL = MEDIA_LEVEL;
    }

    public String getNOTIFICATION_LEVEL() {
        return NOTIFICATION_LEVEL;
    }

    public void setNOTIFICATION_LEVEL(String NOTIFICATION_LEVEL) {
        this.NOTIFICATION_LEVEL = NOTIFICATION_LEVEL;
    }

    public String getSYSTEM_LEVEL() {
        return SYSTEM_LEVEL;
    }

    public void setSYSTEM_LEVEL(String SYSTEM_LEVEL) {
        this.SYSTEM_LEVEL = SYSTEM_LEVEL;
    }

    public String getDIAL_PAD_SOUND() {
        return DIAL_PAD_SOUND;
    }

    public void setDIAL_PAD_SOUND(String DIAL_PAD_SOUND) {
        this.DIAL_PAD_SOUND = DIAL_PAD_SOUND;
    }

    public String getLOCK_SCREEN_SOUND() {
        return LOCK_SCREEN_SOUND;
    }

    public void setLOCK_SCREEN_SOUND(String LOCK_SCREEN_SOUND) {
        this.LOCK_SCREEN_SOUND = LOCK_SCREEN_SOUND;
    }

    public String getTOUCH_SOUND() {
        return TOUCH_SOUND;
    }

    public void setTOUCH_SOUND(String TOUCH_SOUND) {
        this.TOUCH_SOUND = TOUCH_SOUND;
    }

    public String getVIBRATE() {
        return VIBRATE;
    }

    public void setVIBRATE(String VIBRATE) {
        this.VIBRATE = VIBRATE;
    }
}
