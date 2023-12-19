package com.example.distancecoupleapp.presentation.user

data class UserState(
    var userName: String = "",
    var isUploading: Boolean = false,
    var selectedImageUri: String? = null,
    var imageState: String = "loading"
)