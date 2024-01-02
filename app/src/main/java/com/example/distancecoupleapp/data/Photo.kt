package com.example.distancecoupleapp.data

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Parcelize
@Stable
data class Photo(
    val imageUrl: String,
    val owner: String,
    val description: String,
    val id: String,
    val timestamp: Long
): Parcelable {
    constructor() : this("", "", "", "", 0L)
}