package com.skyune.loficorner.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.skyune.loficorner.model.Artwork

    class ArtworkTypeConverter {
        @TypeConverter
        fun fromArtwork(artwork: Artwork): String {
            return Gson().toJson(artwork)
        }

        @TypeConverter
        fun toArtwork(artwork: String): Artwork {
            return Gson().fromJson(artwork, Artwork::class.java)
        }
    }
