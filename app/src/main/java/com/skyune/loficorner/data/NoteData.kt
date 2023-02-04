package com.example.cleannote.data

import com.skyune.loficorner.model.CurrentSong
import com.skyune.loficorner.model.Data
import java.sql.Types.NULL

//dummy data for now
class NotesDataSource{
    fun loadNotes(): List<CurrentSong> {
        return listOf(
            CurrentSong(NULL,"noPJL")
        )
    }
}