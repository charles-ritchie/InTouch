package com.signify.intouch.data;

/**
 * Created by critchie on 20/01/2015.
 */

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by critchie on 15/12/2014.
 */
public class DataStore {
    private static final String PREFS = "com.signify.intouch.datastore";
    private static DataStore mInstance = null;
    public static SharedPreferences mPref;

    private DataStore(Context context) {
        mPref = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static DataStore getInstance(Context context){
        if (mInstance == null){
            mInstance = new DataStore(context);
        }
        return mInstance;
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
