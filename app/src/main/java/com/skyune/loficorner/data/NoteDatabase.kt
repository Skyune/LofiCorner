package com.example.cleannote.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.skyune.loficorner.data.ArtworkTypeConverter
import com.skyune.loficorner.data.NoteDatabaseDao
import com.skyune.loficorner.data.UserTypeConverter
import com.skyune.loficorner.model.Data


@Database(entities = [Data::class], version = 13, exportSchema = false)
@TypeConverters(ArtworkTypeConverter::class, UserTypeConverter::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDatabaseDao
}