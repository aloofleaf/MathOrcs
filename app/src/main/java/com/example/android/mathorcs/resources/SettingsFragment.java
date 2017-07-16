package com.example.android.mathorcs.resources;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import com.example.android.mathorcs.R;

import java.util.List;

/**
 * Created by kskotheim on 6/26/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.settings_values);

        SharedPreferences sharedP = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();

        int n = preferenceScreen.getPreferenceCount();
        for(int i=0;i<n;i++){
            Preference p = preferenceScreen.getPreference(i);

            if(!(p instanceof CheckBoxPreference)){
                String value = sharedP.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }
        }
    }
    private void setPreferenceSummary(Preference p, String value){
        if(p instanceof ListPreference){
            ListPreference listPref = (ListPreference) p;
            int prefIndex = listPref.findIndexOfValue(value);
            if(prefIndex>=0){
                listPref.setSummary(listPref.getEntries()[prefIndex]);
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedP, String key) {
        Preference p = findPreference(key);
        if(null != p){
            if(!(p instanceof CheckBoxPreference)){
                String value = sharedP.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }
        }
    }
}
