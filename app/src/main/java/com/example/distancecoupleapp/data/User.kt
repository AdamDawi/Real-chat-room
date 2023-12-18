package com.example.distancecoupleapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String,
    val email: String,
    val id: String,
    val picture: String
): Parcelable {
    constructor() : this("", "", "", "")
}

