package com.example.distancecoupleapp.presentation.camera

import android.graphics.Bitmap
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.ui.graphics.vector.ImageVector

data class CameraState (
    var flashIcon: ImageVector = Icons.Default.FlashOff,
    var isLoading: Boolean = false,
    //image displayed after taking a photo
    //default value is bitmap with params
    var image: Bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),
    //change elements on the screen
    var isPhotoTaken: Boolean = false,
    //image URL from firebase data store
    var imageUrl: String = "",
    var descriptionTextField: String = "",
    var cameraIconRotation: Float = 0f,
    var isTakingPhoto: Boolean = false
)