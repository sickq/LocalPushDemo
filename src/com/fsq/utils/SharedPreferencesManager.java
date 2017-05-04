package com.fsq.utils;

import org.json.JSONArray;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesManager {
	private SharedPreferencesManager() {
	}
	
	private static SharedPreferencesManager instance;
	
	public static synchronized SharedPreferencesManager getInstance() {
		if(instance == null)
			instance = new SharedPreferencesManager();
		return instance;
	}
	
	public static void savePrefs(Context context, String key, String saveContent){
		
		Log.i("TAG", "savePrefs:" + saveContent);
		
        SharedPreferences preferences = context.getSharedPreferences(key, 0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, saveContent);
		editor.commit();
	}
	
	public static String getPrefsStr(Context context, String key){
		SharedPreferences preferences = context.getSharedPreferences(key, 0);
		String content = preferences.getString(key, "");
		return content;
	}
	
}
