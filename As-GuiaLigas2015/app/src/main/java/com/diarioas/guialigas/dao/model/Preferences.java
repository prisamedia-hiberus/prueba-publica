package com.diarioas.guialigas.dao.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by luismiguel on 10/3/16.
 */
public class Preferences {

    public static final String PREFS_NAME = "AS-GUIA-LIGAS-PREFERENCES";
    public static final String LAST_DATABASE_VERSION_PREF = "lastDatabaseVersion";



    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mEditor;

    public Preferences(Context context) {
        this.mSharedPrefs = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        this.mEditor = mSharedPrefs.edit();
    }

    public void clear() {
        mEditor.clear().commit();
    }

    /**
     * Storage last database app version
     * @param version
     */
    public void setLastDatabaseVersionPref(int version){
        mEditor.putInt(LAST_DATABASE_VERSION_PREF, version);
        mEditor.commit();
    }

    /**
     * Get last database version
     * @return
     */
    public int getLastDatabaseVersion(){
        return mSharedPrefs.getInt(LAST_DATABASE_VERSION_PREF, -1);
    }

}
