package com.cloverinfosoft.studentattendance.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

	public static final String SHARED_PREFERENCES_NAME = "StudentAttendancePref";

	/**
	 * Instance
	 */
	private static PreferencesManager preferencesManager = null;

	/**
	 * Shared Preferences
	 */
	private SharedPreferences sharedPreferences;


	private static String UserId = "UserId";
	private static String UserName = "UserName";
	private static String Role = "Role";
	private static String Classname = "Classname";


	public String getClassname() {
		return sharedPreferences.getString(Classname, "");
	}

	public void setClassname(String s) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(Classname, s);
		editor.commit();
	}
	public String getRole() {
		return sharedPreferences.getString(Role, "");
	}

	public void setRole(String s) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(Role, s);
		editor.commit();
	}


	public String getUserId() {
		return sharedPreferences.getString(UserId, "");
	}

	public void setUserId(String userId) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(UserId, userId);
		editor.commit();
	}


	public String getUserName() {
		return sharedPreferences.getString(UserName, "");
	}

	public void setUserName(String s) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(UserName, s);
		editor.commit();
	}


	public void clearPrefrences() {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("ACCESS_TOKEN", "");
		editor.putString("ACCESS_TOKEN_SECRET", "");
		editor.clear();
		editor.commit();
	}


	private PreferencesManager(Context context) {
		sharedPreferences = context.getSharedPreferences(
				SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
	}

	public static PreferencesManager getInstance(Context context) {
		if (preferencesManager == null) {
			preferencesManager = new PreferencesManager(context);
		}
		return preferencesManager;
	}


}
