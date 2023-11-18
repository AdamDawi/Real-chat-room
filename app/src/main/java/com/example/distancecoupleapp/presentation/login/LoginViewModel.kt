package com.example.distancecoupleapp.presentation.login

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginViewModel: ViewModel(){
    private var auth: FirebaseAuth = Firebase.auth
    var loginState by mutableStateOf(LoginState())
    private set

    init {
        checkIfLogged()
    }

    private fun checkIfLogged(){
        if (auth.currentUser != null) {

            loginState = loginState.copy(isLogged = true)
        }
    }
    fun changeEmailState(newEmail: String){
        loginState = loginState.copy(email = newEmail)
    }

    fun changePasswordState(newPassword: String){
        loginState = loginState.copy(password = newPassword)
    }

    fun createAccount(context: Context){
        if(loginState.email.isNotEmpty() && loginState.password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(loginState.email, loginState.password)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI
                        Log.d(TAG, "createUserWithEmail:success")
                        Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show()
                        loginState = loginState.copy(isLogged = true)
                    } else {
                        // If sign in fails, display a message to the user.
                        if(!loginState.email.contains('@') || !loginState.email.contains('.'))
                            Toast.makeText(context, "The email address is badly formatted", Toast.LENGTH_SHORT).show()
                        else if(loginState.password.length<6)
                            Toast.makeText(context, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
                        else Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    }
                }
        }

    }


}