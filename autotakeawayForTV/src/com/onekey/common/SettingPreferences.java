package com.onekey.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SettingPreferences {

	SharedPreferences m_preferences;

	public SettingPreferences(Activity activity) {
		m_preferences = activity.getSharedPreferences("Settings",
				Context.MODE_PRIVATE);
	}

	public static final String Preference_Token = "token";
	public String GetToken() {
		return m_preferences.getString(Preference_Token, "");
	}

	public void SetToken(String token) {
		m_preferences.edit().putString(Preference_Token, token)
				.commit();
	}
	
	
}
