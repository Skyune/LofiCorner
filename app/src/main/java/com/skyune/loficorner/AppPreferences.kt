package com.skyune.loficorner

import android.content.SharedPreferences
import com.skyune.loficorner.ui.theme.Theme

class AppPreferences(private val sharedPreferences: SharedPreferences) {

    var selectedItemId: String?
        get() = sharedPreferences.getString("selectedItemId", null)
        set(value) = sharedPreferences.edit().putString("selectedItemId", value).apply()

    var selectedButtonIndexId: Int
        get() = sharedPreferences.getInt("selectedButtonIndexId", 0)
        set(value) = sharedPreferences.edit().putInt("selectedButtonIndexId", value).apply()

    var selectedTheme: Theme
        get() = Theme.values()[sharedPreferences.getInt("selectedTheme", 0)]
        set(value) = sharedPreferences.edit().putInt("selectedTheme", value.ordinal).apply()

    var selectedRoomId: Int
        get() = sharedPreferences.getInt("selectedRoomId", 0)
        set(value) = sharedPreferences.edit().putInt("selectedRoomId", value).apply()

    var selectedRoomImage: Int
        get() = sharedPreferences.getInt("selectedRoomImage", 0)
        set(value) = sharedPreferences.edit().putInt("selectedRoomImage", value).apply()

}