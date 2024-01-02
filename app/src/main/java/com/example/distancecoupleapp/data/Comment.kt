package com.example.distancecoupleapp.data

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Parcelize
@Stable
data class Comment(
    val user: String,
    val text: String,
    val timestamp: Long
): Parcelable {
    constructor() : this("", "",0L)
}

