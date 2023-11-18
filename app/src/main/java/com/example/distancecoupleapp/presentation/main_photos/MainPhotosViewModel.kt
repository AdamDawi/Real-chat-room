package com.example.distancecoupleapp.presentation.main_photos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.distancecoupleapp.common.Constants
import com.google.firebase.auth.FirebaseAuth

class MainPhotosViewModel: ViewModel() {
    private val auth: FirebaseAuth = Constants.auth

    var mainPhotosState by mutableStateOf(MainPhotosState())
        private set

    fun getUserName() {
        mainPhotosState = mainPhotosState.copy(name = auth.currentUser?.displayName.toString())
    }
}