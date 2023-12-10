package com.example.distancecoupleapp.presentation.user

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.distancecoupleapp.data.FirebaseManager
import com.google.firebase.auth.FirebaseAuth

class UserViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseManager().getFirebaseAuth()
    fun signOut(navigateToLoginScreen: () -> Unit) {
        auth.signOut()
        navigateToLoginScreen()
    }

    fun getUserName(): String{
        if(auth.currentUser !=null){
            return auth.currentUser!!.displayName!!
        }
        else{
            Log.e("Get user name", "current user is null")
        }
        return ""
    }

}