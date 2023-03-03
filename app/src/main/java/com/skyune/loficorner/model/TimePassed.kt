package com.skyune.loficorner.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rld.justlisten.datalayer.models.UserModel
import kotlinx.serialization.SerialName
import javax.annotation.Nullable

@Entity(tableName = "time_passed")
data class TimePassed(
    @PrimaryKey val id: Int = 1,
    val time: Long,
    val taskName: String
)