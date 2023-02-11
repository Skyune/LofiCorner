package com.skyune.loficorner.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.skyune.loficorner.model.User


    class UserTypeConverter {
        @TypeConverter
        fun fromUser(user: User): String {
            return Gson().toJson(user)
        }

        @TypeConverter
        fun toUser(user: String): User {
            return Gson().fromJson(user, User::class.java)
        }
    }

