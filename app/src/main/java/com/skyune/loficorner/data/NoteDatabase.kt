package com.example.cleannote.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.skyune.loficorner.data.ArtworkTypeConverter
import com.skyune.loficorner.data.NoteDatabaseDao
import com.skyune.loficorner.data.UserTypeConverter
import com.skyune.loficorner.model.CurrentRoom
import com.skyune.loficorner.model.Data
import com.skyune.loficorner.model.TimePassed


@Database(entities = [Data::class, TimePassed::class, CurrentRoom::class], version = 47, exportSchema = false)
@TypeConverters(ArtworkTypeConverter::class, UserTypeConverter::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDatabaseDao
}