package com.skyune.loficorner.model

import com.google.gson.annotations.SerializedName

data class Artwork(
    val `1000x1000`: String,
    @SerializedName("150x150")
    val `small`: String,
    val `480x480`: String
)