package com.example.distancecoupleapp.presentation.user

import android.net.Uri

data class UserState(
    var userName: String = "",
    var isUploading: Boolean = false,
    var selectedImageUri: Uri? = null
)