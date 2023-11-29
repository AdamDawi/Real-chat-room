package com.example.distancecoupleapp.presentation.camera

import android.graphics.Bitmap
import androidx.compose.ui.graphics.vector.ImageVector

data class CameraState (
    var imageVector: ImageVector? = null,
    var isLoading: Boolean = false,
    var image: Bitmap? = null
)