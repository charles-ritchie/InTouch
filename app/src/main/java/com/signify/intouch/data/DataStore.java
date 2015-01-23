package com.signify.intouch.data;

/**
 * Created by critchie on 20/01/2015.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


/**
 * Created by critchie on 15/12/2014.
 */
public class DataStore {
    private static final String PREFS = "com.signify.intouch.datastore";
    private static DataStore instance = null;
    public static SharedPreferences mPref;

    private DataStore(Context context) {
        this.mPref = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static DataStore getInstance(Context context){
        if (instance == null){
            instance = new DataStore(context);
        }
        return instance;
    }

    public void saveString(String key, String value){
        this.mPref.edit().putString(key, value).commit();
    }

    public String getString(String key){
        return this.mPref.getString(key, null);
    }

    public void clearString(String toclear){
        this.mPref.edit().remove(toclear).commit();
    }

    public void saveBool(String key, Boolean value){
        this.mPref.edit().putBoolean(key, value).commit();
    }

    public Boolean getBool(String key){
        return this.mPref.getBoolean(key, true);
    }

    public void clearBool(String toclear){
        this.mPref.edit().remove(toclear).commit();
    }

    public void clearAllEntries(){
        this.mPref.edit().clear().commit();
    }
}
