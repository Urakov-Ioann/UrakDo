package com.example.urakdo;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences sharedPreferences;
    public SharedPref(Context context){
        sharedPreferences = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }
    public void setNigthModeState(boolean state){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NigthMode", state);
        editor.commit();
    }
    public boolean loadNigthState(){
        boolean state = sharedPreferences.getBoolean("NigthMode", false);
        return state;
    }
}
