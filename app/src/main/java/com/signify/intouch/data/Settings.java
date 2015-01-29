package com.signify.intouch.data;

import android.content.Context;
import android.net.Uri;

/**
 * Created by critchie on 22/01/2015.
 */
public class Settings {

    private static DataStore mStore;
    private static Settings mInstance;

    private Settings(Context context) {
        mStore = DataStore.getInstance(context);
    }

    public static Settings getInstance(Context context){
        if (mInstance == null){
            mInstance = new Settings(context);
        }
        return mInstance;
    }

    public boolean getFirstRun() {
        return mStore.getBool("first_run");
    }

    public void setFirstRun(boolean firstRun) {
        mStore.saveBool("first_run", firstRun);
    }

    public Uri getContactUri() {
        return Uri.parse(mStore.getString("contact_uri"));
    }

    public void setContactUri(Uri contactUri) {
        mStore.saveString("contact_uri", contactUri.toString());
    }

    public String[] getTimes() {
        String[] times = new String[2];
        times[0] = mStore.getString("times_start");
        times[1] = mStore.getString("times_finish");
        return times;
    }

    public void setTimes(String[] times) {
        mStore.saveString("times_start", times[0]);
        mStore.saveString("times_finish", times[1]);
    }

    public Boolean[] getActiveDays() {
        Boolean[] returnArr = new Boolean[7];
        for(int i=0; i<7; i++) {
            returnArr[i] = mStore.getBool("active_day_"+i);
        }
        return returnArr;
    }

    public void setActiveDays(Boolean[] days) {
        for(int i=0; i<7; i++) {
            mStore.saveBool("active_day_"+i, days[i]);
        }
    }

    public Boolean getHibernate() {
        return mStore.getBool("hibernate");
    }

    public void setHibernate(Boolean hibernate) {
            mStore.saveBool("hibernate", hibernate);
    }

    public String getDateToday() {
        return mStore.getString("date_today");
    }

    public void setDateToday(String today) {
        mStore.saveString("date_today", today);
    }

    public Boolean getContactedToday() {
        return mStore.getBool("contacted_today");
    }

    public void setContactedToday(Boolean contactedToday) {
        mStore.saveBool("contacted_today", contactedToday);
    }

    public String[] getSweetSpots() {
        String[] returnArr = new String[6];
        for(int i=0; i<6; i++) {
            returnArr[i] = mStore.getString("sweet_spot_"+i);
        }
        return returnArr;
    }

    public void setSweetSpots(String[] times) {
        for(int i=0; i<6; i++) {
            mStore.saveString("sweet_spot_"+i, times[i]);
        }
    }
}
