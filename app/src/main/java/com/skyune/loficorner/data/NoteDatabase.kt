package com.example.cleannote.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skyune.loficorner.data.NoteDatabaseDao
import com.skyune.loficorner.model.Data


@Database(entities = [Data::class], version = 7, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDatabaseDao
}