package com.example.profilechanger.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.profilechanger.annotations.MyAnnotations;

public class MyPreferences {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MyPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MyAnnotations.MY_PREFERENCES,
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setDataString(String key, String data) {
        editor.putString(key, data).commit();
    }
    public void setBoolean(String key, boolean data) {
        editor.putBoolean(key, data).commit();
    }
    public void setInt(String key, int data) {
        editor.putInt(key, data).commit();
    }
    public String getString(String key,String defaultData)
    {
        return sharedPreferences.getString(key,defaultData);
    }
    public boolean getBoolean(String key,boolean defaultData)
    {
        return sharedPreferences.getBoolean(key,defaultData);
    }
    public void addLong(String key,Long data)
    {
        editor.putLong(key,data).commit();
    }
    public long getLong(String key,long defaultVal)
    {
        return  sharedPreferences.getLong(key,defaultVal);
    }
    public int getInt(String key,int defaultVal)
    {
        return  sharedPreferences.getInt(key,defaultVal);
    }

}
