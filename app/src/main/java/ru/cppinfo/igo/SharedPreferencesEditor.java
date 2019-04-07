package ru.cppinfo.igo;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesEditor {
    private Context context;
    private SharedPreferences settigns;
    private SharedPreferences.Editor editor;


    public SharedPreferencesEditor(Context context) {
        this.context = context;
        initSettings();
    }

    private void initSettings() {
        settigns = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        editor = settigns.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putBool(String key, boolean value) {
        editor = settigns.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getString(String key, String defValue) {
        return settigns.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return settigns.getBoolean(key, defValue);
    }

    public int getInteger(String key, int defValue) {
        return settigns.getInt(key, defValue);
    }


}
