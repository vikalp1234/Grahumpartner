package com.app.graham.partner.shopdetails

import android.content.SharedPreferences

class ApplicationPrefs {

    private val mPreferences: SharedPreferences = App.self().getSharedPreferences(
        Constants.APPLICATION_PREFS, 0
    )

    fun isNotFirstTime(): Boolean {
        return mPreferences.getBoolean(Constants.IS_FIRST_TIME, false)
    }

    fun setNotFirstTime(b: Boolean) {
        val mEditor = mPreferences.edit()
        mEditor.putBoolean(Constants.IS_FIRST_TIME, b).apply()
    }
}