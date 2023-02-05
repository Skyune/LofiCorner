package com.skyune.loficorner.model

import com.google.gson.annotations.SerializedName

data class Artwork(
    @SerializedName("150x150")
    val `small`: String,
)