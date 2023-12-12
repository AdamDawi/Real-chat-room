package com.example.distancecoupleapp.presentation.user

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.distancecoupleapp.data.FirebaseManager
import com.example.distancecoupleapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference

class UserViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseManager().getFirebaseAuth()
    private val database: DatabaseReference = FirebaseManager().getFirebaseDatabaseReference()

    var userState by mutableStateOf(UserState())
        private set
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

    fun changeUsernameState(newName: String){
        userState = userState.copy(userName = newName)
    }

    fun changeUsername(newName: String, context: Context){
        val user = auth.currentUser
        if(user!=null){
            val profileUpdates = userProfileChangeRequest {
                displayName = newName
            }

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                    }
                }

            val currentUserId = auth.currentUser!!.uid
            val userData = User(newName, auth.currentUser?.email ?: "Error", currentUserId)

            val userValues = userData.toMap()
            val childUpdates = HashMap<String, Any>()
            childUpdates["/users/$currentUserId"] = userValues
            database.updateChildren(childUpdates)

            Toast.makeText(context, "Username changed ", Toast.LENGTH_SHORT).show()
        }else{
            Log.e("change username", "current user is null")
        }

    }

    private fun User.toMap(): Map<String, Any> {
        return mapOf(
            "username" to username,
            "email" to email,
        )
    }

}