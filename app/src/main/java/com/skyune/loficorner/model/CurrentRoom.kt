package com.skyune.loficorner.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_room")
data class CurrentRoom(
    @PrimaryKey val id: Int = 1,
    val imageId: Int,
)