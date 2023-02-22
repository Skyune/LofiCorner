package com.skyune.loficorner

import android.content.SharedPreferences

class AppPreferences(private val sharedPreferences: SharedPreferences) {

    var selectedItemId: String?
        get() = sharedPreferences.getString("selectedItemId", null)
        set(value) = sharedPreferences.edit().putString("selectedItemId", value).apply()
}