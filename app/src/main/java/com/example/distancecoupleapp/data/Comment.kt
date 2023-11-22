package com.example.distancecoupleapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val user: String,
    val text: String,
    val timestamp: Long
): Parcelable {
    constructor() : this("", "",0L)
}

