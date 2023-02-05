package com.skyune.loficorner.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rld.justlisten.datalayer.models.UserModel
import kotlinx.serialization.SerialName
import javax.annotation.Nullable

@Entity
data class Data(

    //
    val artwork: Artwork?,
    val description: String?,
//
    var playlist_name: String?,
    val duration: Int?,
//    val favorite_count: Int?,
    //val genre: String,
    @PrimaryKey val id: String,
    val mood: String?,
//    val permalink: String,
//    val play_count: Int,
//    val release_date: String,
    //val remix_of: RemixOf,
//    val repost_count: Int,
//    val tags: String,
    var title: String?,
    val user: User?
)