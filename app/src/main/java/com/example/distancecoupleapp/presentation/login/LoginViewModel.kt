package com.example.distancecoupleapp.presentation.login

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

class LoginViewModel: ViewModel(){
    private var auth: FirebaseAuth = FirebaseManager().getFirebaseAuth()
    private val database: DatabaseReference = FirebaseManager().getFirebaseDatabaseReference()
    var loginState by mutableStateOf(LoginState())
    private set

    // Initialize the ViewModel and check if the user is already logged in
    init {
        changeIsLoadingState(true)
        checkIfLogged()
        changeIsLoadingState(false)
    }

    // Check if the user is already logged in and update the state accordingly
    private fun checkIfLogged(){
        if (auth.currentUser != null) {
            loginState = loginState.copy(isLogged = true)
        }
    }
    // Methods to update specific fields in the login state
    private fun changeIsLoadingState(s: Boolean){
        loginState = loginState.copy(isLoading = s)
    }

    fun changeEmailState(newEmail: String){
        loginState = loginState.copy(email = newEmail)
    }

    fun changePasswordState(newPassword: String){
        loginState = loginState.copy(password = newPassword)
    }

    fun changeNameState(newName: String){
        loginState = loginState.copy(name = newName)
    }

    fun changeIsRegisteringState(isRegistering: Boolean){
        loginState = loginState.copy(isRegistering = isRegistering)
    }

    fun changeIsLoggedState(isLogged: Boolean){
        loginState = loginState.copy(isLogged = isLogged)
    }

    fun createAccount(context: Context){
        changeIsLoadingState(true)
        if(loginState.email.isNotEmpty()
            && loginState.password.isNotEmpty()
            && (!loginState.isRegistering
            || (loginState.isRegistering
            && loginState.name.isNotEmpty()))
        ) {
            auth.createUserWithEmailAndPassword(loginState.email, loginState.password)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI
                        Log.d(TAG, "createUserWithEmail:success")
                        updateUserName(loginState.name)
                        addUserToDatabase(loginState.name)
                        Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show()
                        loginState = loginState.copy(isLogged = true, email = "", password = "", name = "")
                    } else {
                        // If sign in fails, display a message to the user.
                        if(!loginState.email.contains('@') || !loginState.email.contains('.'))
                            Toast.makeText(context, "The email address is badly formatted", Toast.LENGTH_SHORT).show()
                        else if(loginState.password.length<6)
                            Toast.makeText(context, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
                        else {
                            Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT)
                                .show()
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        }
                    }
                }
        }
        else Toast.makeText(context, "Field is empty", Toast.LENGTH_SHORT).show()
        changeIsLoadingState(false)
    }

    fun signIn(context: Context){
        changeIsLoadingState(true)
        if(loginState.email.isNotEmpty() && loginState.password.isNotEmpty() ){
            auth.signInWithEmailAndPassword(loginState.email, loginState.password)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI
                        Log.d(TAG, "signInWithEmail:success")
                        //changing login state because we need to navigate to next screen and we want to clear text fields
                        loginState = loginState.copy(isLogged = true, email = "", password = "", name = "")
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        else Toast.makeText(context, "Field is empty", Toast.LENGTH_SHORT).show()
        changeIsLoadingState(false)
    }

    // Update the username in the Firebase user profile
    private fun updateUserName(name: String){
        if(auth.currentUser!=null)
        {
            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }
            auth.currentUser!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                    }
                }
        }else Log.e("Update profile", "Current user is null. Can't have updated name.")
    }

    // Add the user to the Firebase Realtime Database
    private fun addUserToDatabase(name: String){
        if(auth.currentUser!=null)
        {
            val currentUserId = auth.currentUser!!.uid
            val user = User(name, auth.currentUser?.email ?: "Error", currentUserId, "")

            val userValues = user.toMap()
            val childUpdates = HashMap<String, Any>()
            childUpdates["/users/$currentUserId"] = userValues

            database.updateChildren(childUpdates)
        }else Log.e("Add user to database", "Current user is null. Can't add to database.")
    }

    // Convert User object to a map for database storage
    private fun User.toMap(): Map<String, Any> {
        return mapOf(
            "username" to username,
            "email" to email,
            "picture" to picture
        )
    }
}