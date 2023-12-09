package com.example.distancecoupleapp.presentation.user

import androidx.lifecycle.ViewModel
import com.example.distancecoupleapp.data.FirebaseManager
import com.google.firebase.auth.FirebaseAuth

class UserViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseManager().getFirebaseAuth()
    fun signOut(navigateToLoginScreen: () -> Unit) {
        auth.signOut()
        navigateToLoginScreen()

    }

}